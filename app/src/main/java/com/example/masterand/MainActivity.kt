package com.example.masterand

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseInQuad
import androidx.compose.animation.core.EaseInSine
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.EaseOutQuad
import androidx.compose.animation.core.EaseOutSine
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.masterand.ui.theme.MasterAndTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MasterAndTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .navigationBarsPadding()
                        .imePadding(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Column(
        modifier = Modifier
            .imePadding()
            .imeNestedScroll()
    ) {
        NavigationGraph(navController)
    }
}

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "loginScreen",
        enterTransition = {
            fadeIn() + slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(500, easing = EaseInQuad))
        },
        exitTransition = {
            fadeOut() + slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(500, easing = EaseOutQuad))
        }
    ) {

        composable("loginScreen") {
            ProfileScreenInitial(
                onStartButton = { colorAmount, playerId ->
                    navController.navigate("gameScreen/$colorAmount/$playerId")
                },
                onResultsButton = {
                    navController.navigate("resultsScreen/0/0/0")
                }
            )
        }

        composable(
            "gameScreen/{colorsAmount}/{playerId}",
            arguments = listOf(
                navArgument("colorsAmount") { type = NavType.IntType },
                navArgument("playerId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val colorsAmountInput = backStackEntry.arguments?.getInt("colorsAmount")!!
            val playerIdInput = backStackEntry.arguments?.getLong("playerId")!!
            GameScreen(
                colorsAmountInput,
                playerIdInput,
                onLogoutButton = {
                    navController.popBackStack("loginScreen", inclusive = true, saveState = false)
                    navController.navigate("loginScreen")
                },
                onResultsButton = { recentScore ->
                    navController.navigate("resultsScreen/$recentScore/$colorsAmountInput/$playerIdInput")
                },
                onRestartButton = { colorsAmount, playerId ->
                    navController.popBackStack("gameScreen", inclusive = true, saveState = false)
                    navController.navigate("gameScreen/$colorsAmount/$playerId")
                }
            )
        }

        composable(
            "resultsScreen/{recentScore}/{colorAmount}/{playerId}",
            arguments = listOf(
                navArgument("recentScore") { type = NavType.IntType },
                navArgument("colorAmount") { type = NavType.IntType },
                navArgument("playerId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val recentScoreInput = backStackEntry.arguments?.getInt("recentScore")!!
            val colorAmountInput = backStackEntry.arguments?.getInt("colorAmount")!!
            val playerIdInput = backStackEntry.arguments?.getLong("playerId")!!
            Scores(
                recentScoreInput,
                colorAmount = colorAmountInput,
                playerId = playerIdInput,
                onLogoutButton = {
                    navController.popBackStack("loginScreen", inclusive = true, saveState = false)
                    navController.navigate("loginScreen")
                },
                onRestartButton = { colorsAmount, playerId ->
                    navController.popBackStack("gameScreen", inclusive = true, saveState = false)
                    navController.navigate("gameScreen/$colorsAmount/$playerId")
                }
            )
        }
    }
}

@Preview
@Composable
fun MainActivityPreview() {
    MasterAndTheme {
        Surface(
            color = Color.White
        ) {
            MainScreen()
        }
    }
}