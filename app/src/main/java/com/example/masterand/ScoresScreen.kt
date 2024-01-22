package com.example.masterand

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.masterand.ViewModels.ScoresViewModel
import com.example.masterand.ui.theme.MasterAndTheme
import kotlinx.coroutines.launch

class ResultsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MasterAndTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scores(3, 5, 0, {}, { _, _ -> })
                }
            }
        }
    }
}

@Composable
fun Scores(
    recentScore: Int,
    colorAmount: Int,
    playerId: Long,
    onLogoutButton: () -> Unit,
    onRestartButton: (colorAmount: Int, playerId: Long) -> Unit,
    //viewModel: HighScoresViewModel = viewModel(factory = MasterAndApplication.AppViewModelProvider.Factory)
    viewModel: ScoresViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(key1 = Unit) {
        coroutineScope.launch {
            viewModel.downloadResults()
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Results",
            fontSize = 72.sp
        )
        if (recentScore > 0) {
            Text(
                text = "Recent score: $recentScore",
                fontSize = 40.sp
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(10.dp)
        ) {
            val results = viewModel.results.value
            if (results != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.Black)
                        .height(2.dp)
                )
                for (result in results) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)) {
                        Text(
                            text = result.first,
                            fontSize = 26.sp,
                            modifier = Modifier.fillMaxWidth(0.5f)
                        )
                        Text(
                            text = result.second.toString(),
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth(0.5f),
                            textAlign = TextAlign.End
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.Black)
                            .height(2.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(10.dp))
        if (colorAmount > 0) {
            Button(onClick = { onRestartButton(colorAmount, playerId) }) {
                Text(text = "Restart")
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = { onLogoutButton() }) {
            Text(text = if (playerId > 0) { "Logout" } else { "Login" })
        }
        Spacer(modifier = Modifier.height(30.dp))
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MasterAndTheme {
        Scores(recentScore = 3, 5, 0,{}, { _, _ -> })
    }
}