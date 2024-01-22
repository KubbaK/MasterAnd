package com.example.masterand

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInCubic
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.masterand.R
import com.example.masterand.ViewModels.GameViewModel
import com.example.masterand.ui.theme.MasterAndTheme
import kotlinx.coroutines.launch
import java.util.Random

class GameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MasterAndTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GameScreen(5, 0, {}, {}, { _, _ -> })
                }
            }
        }
    }
}

private fun getColorSaver(): Saver<Color?, Any> {
    return run {
        mapSaver(
            save = { mapOf("value" to it?.value.toString()) },
            restore = {
                val stringValue = it["value"] as String
                if (stringValue == "null") {
                    null
                } else {
                    Color(stringValue.toULong())
                }
            }
        )
    }
}

private fun getColorListSaver(): Saver<List<Color?>, Any> {
    return listSaver<List<Color?>, Any>(
        save = { colorList ->
            println("d")
            val d = colorList.map { color: Color? ->
                color?.value?.toString() ?: "null"
            }
            println("d = $d")
            d
        },
        restore = { stringList ->
            println("h")
            val h = stringList.map {
                if (it == "null") {
                    return@map null
                } else {
                    return@map Color((it as String).toULong())
                }
            }
            println("h = $h")
            h
        }
    )
}

@Composable
fun GameScreen(
    colorsAmount: Int,
    playerId: Long,
    onLogoutButton: () -> Unit,
    onResultsButton: (recentScore: Int) -> Unit,
    onRestartButton: (colorsAmount: Int, playerId: Long) -> Unit,
    //viewModel: GameViewModel = viewModel(factory = MasterAndApplication.AppViewModelProvider.Factory)
    viewModel: GameViewModel = hiltViewModel()
) {
    //zakres współprogramów
    val coroutineScope = rememberCoroutineScope()

    val colorListSaver: Saver<List<Color?>, Any> = getColorListSaver()
    val availableColors = rememberSaveable(stateSaver = colorListSaver) {
        mutableStateOf(generateAvailableColors(colorsAmount))
    }
    val colorset = rememberSaveable(stateSaver = colorListSaver) {
        mutableStateOf(
            generateColorsetToGuess(availableColors.value.filterNotNull())
        )
    }
    val finished = rememberSaveable {
        mutableStateOf(false)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = "Score: ${viewModel.score.intValue}",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
        for (i in 0..<viewModel.score.intValue) {
            SelectableColorsRow(
                availableColors = availableColors.value.filterNotNull(),
                colorset = colorset.value.filterNotNull(),
                onSubmit = { allCorrect ->
                    if (!allCorrect) {
                        viewModel.score.value += 1
                    } else {
                        coroutineScope.launch {
                            viewModel.submitScore(playerId)
                            println("playerId = $playerId")
                            finished.value = true
                        }
                    }
                }
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(10.dp))
        if (finished.value) {
            Button(onClick = { onRestartButton(colorsAmount, playerId) }) {
                Text(text = "Start over")
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(onClick = { onResultsButton(viewModel.score.intValue) }) {
                Text(text = "Results")
            }
        }
        //Spacer(modifier = Modifier.fillMaxHeight())
        Button(onClick = { onLogoutButton() }) {
            Text(text = "Logout")
        }
        Spacer(modifier = Modifier.height(30.dp))
    }
}

private fun getAvailableColors(): java.util.ArrayList<Color> {
    return arrayListOf(
        Color(0xFFCC2222), // czerw
        Color(0xFFff8071), // róż
        Color(0xFFD87504), // pom
        Color(0xfff5d417), // żół
        Color(0xff4ca12c), // ziel
        Color(0xFF375000), // ciemnoziel
        Color(0xFF2A70E4), // niebieski
        Color(0xFF263294), // ciemnoniebieski
        Color(0xFF442261), // fioletowy
        Color.Magenta,
    )
}

private fun generateAvailableColors(amount: Int): List<Color> {
    val random = Random()

    val availableColors = getAvailableColors()
    val selectedColors = ArrayList<Color>(amount)

    for (number in 0..<amount) {
        val selectedColorIndex = random.nextInt(availableColors.size)
        val selectedColor = availableColors.removeAt(selectedColorIndex)
        selectedColors.add(selectedColor)
    }

    return selectedColors
}

private fun generateColorsetToGuess(availableColors: List<Color>): List<Color> {
    val random = Random()

    val colorsLeft = availableColors.toMutableList()
    val colorset = ArrayList<Color>(4)

    for (i in 0..<4) {
        val selectedColorIndex = random.nextInt(colorsLeft.size)
        val selectedColor = colorsLeft.removeAt(selectedColorIndex)
        colorset.add(selectedColor)
    }

    return colorset
}

@Composable
private fun SelectableColorsRow(
    availableColors: List<Color>,
    colorset: List<Color>,
    onSubmit: (allCorrect: Boolean) -> Unit
) {
    var rowVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { rowVisible = true }


    val saver = getColorListSaver()
    val selectedColors = rememberSaveable(stateSaver = saver) {
        mutableStateOf(listOf(null, null, null, null))
    }
    val allColorsSelected = rememberSaveable {
        mutableStateOf(false)
    }
    val submitted = rememberSaveable {
        mutableStateOf(false)
    }


    AnimatedVisibility(
        visible = rowVisible,
        enter = expandVertically(
            expandFrom = Alignment.Top,
            animationSpec = tween(1000, easing = EaseInOutCubic)
        )
    ) {
        Row(
            modifier = Modifier
                .height(64.dp)
                .padding(5.dp),
            Arrangement.spacedBy(5.dp)
        ) {
            for ((index, _) in colorset.withIndex()) {
                SelectableCircularButton(
                    availableColors,
                    onClick = {
                        val newList = selectedColors.value.toMutableList()
                        newList[index] = it
                        selectedColors.value = newList
                        println((colorset[index] == it))
                        allColorsSelected.value = selectedColors.value.none { sc -> sc == null }
                    },
                    enabled = !submitted.value,
                )
            }
            Spacer(modifier = Modifier.width(3.dp))
            IconButton(
                enabled = allColorsSelected.value && !submitted.value,
                onClick = {
                    submitted.value = true
                    onSubmit(selectedColors.value == colorset)
                },
                modifier = Modifier
                    .size(54.dp)
                    .padding(0.dp),
            ) {
                AnimatedVisibility(
                    visible = allColorsSelected.value && !submitted.value,
                    enter = scaleIn(animationSpec = tween(1000, easing = EaseInOutCubic)),
                    exit = scaleOut(animationSpec = tween(1000, easing = EaseInOutCubic))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_check_circle_24),
                        contentDescription = "Input error",
                        modifier = Modifier.requiredSize(64.dp),
                    )
                }
            }
            Spacer(modifier = Modifier.width(3.dp))
            ResultDots(
                colorset,
                if (submitted.value) selectedColors.value else listOf(null, null, null, null)
            )
        }
    }
}

@Composable
private fun ResultDots(colorset: List<Color>, selectedColors: List<Color?>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.size(54.dp)
    ) {
        ResultRow(colorset, selectedColors, 0)
        ResultRow(colorset, selectedColors, 2)
    }
}

@Composable
private fun ResultRow(colorset: List<Color>, selectedColors: List<Color?>, startIndex: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .height(25.dp)
            .fillMaxWidth()
    ) {
        ResultDot(colorset, selectedColors, startIndex)
        ResultDot(colorset, selectedColors, startIndex + 1)
    }
}

@Composable
private fun ResultDot(colorset: List<Color>, selectedColors: List<Color?>, index: Int) {
    val targetColor = dotResult(colorset, selectedColors, index)
    val animateColor by remember { mutableStateOf(false).apply { value = true } }
    val animatedColor by animateColorAsState(
        if (animateColor)
            targetColor
        else
            Color.White,
        animationSpec = tween(300, delayMillis = index * 300, easing = EaseInCubic),
        label = "resultDot$index"
    )

    Box(
        modifier = Modifier
            .size(25.dp)
            .border(width = 4.dp, MaterialTheme.colorScheme.outline, CircleShape)
            .background(animatedColor, CircleShape)
    )
}

private fun dotResult(colorset: List<Color>, selectedColors: List<Color?>, index: Int): Color {
    val selectedColor = selectedColors[index]
    if (colorset.none { it == selectedColor }) {
        return Color.Transparent
    }
    val correctColor = colorset[index]
    if (selectedColor == correctColor) {
        return Color.Red
    }
    return Color.Yellow
}

@Composable
private fun SelectableCircularButton(
    availableColors: List<Color>,
    onClick: (Color) -> Unit,
    enabled: Boolean
) {
    val currentColor = rememberSaveable(stateSaver = getColorSaver()) {
        mutableStateOf(null)
    }


    val animateColor by remember { mutableStateOf(false).apply { value = true } }
    val animatedColor by animateColorAsState(
        if (animateColor)
            currentColor.value ?: Color.White
        else
            Color.White,
        animationSpec = repeatable(
            3,
            animation = tween(160, easing = EaseInOutCubic)
        ),
        label = "selectableCircularButton",
    )


    OutlinedButton(
        onClick = {
            if (!enabled) {
                return@OutlinedButton
            }
            var nextIndex = availableColors.indexOf(currentColor.value) + 1
            if (nextIndex >= availableColors.size) {
                nextIndex = 0
            }
            currentColor.value = availableColors[nextIndex]
            onClick(availableColors[nextIndex])
        },
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(1.0f),
        shape = CircleShape,
        border = BorderStroke(5.dp, Color.Black),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = animatedColor
        ),
    ) {
    }
}

@Preview(showBackground = true)
@Composable
fun GamePreview() {
    MasterAndTheme {
        GameScreen(5, 0, {}, {}, { _, _ -> })
    }
}