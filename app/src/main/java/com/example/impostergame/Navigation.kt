package com.example.impostergame

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.impostergame.Screens.*

@Composable
fun Navigation(){
    val navController = rememberNavController()
    val gameSetupViewModel: GameSetupViewModel = viewModel()
    val playViewModel: PlayViewModel = viewModel()

    NavHost(navController = navController, startDestination = "splash"){
        composable("splash"){
            SplashScreen(navController = navController)
        }

        composable("home"){ HomeScreen(navController = navController) }

        composable("howtoPlayScreen") { HowtoPlay(navController = navController) }

        composable("gameSetup") { GameSetup(navController = navController, viewModel = gameSetupViewModel) }

        composable("players") { players(navController = navController, viewModel = gameSetupViewModel) }

        composable("play") {
            PlayScreen(
                navController = navController,
                gameSetupViewModel = gameSetupViewModel,
                playViewModel = playViewModel
            )
        }

        composable("voting") {
            VotingScreen(
                navController = navController,
                playViewModel = playViewModel,
                gameSetupViewModel = gameSetupViewModel
            )
        }
        // ✅ New: Game Over Screen with winner argument

        composable(
            route = "gameOver/winner={winner}",
            arguments = listOf(navArgument("winner") { type = NavType.StringType })
        ) { backStackEntry ->
            val winner = backStackEntry.arguments?.getString("winner") ?: ""
            GameOverScreen(navController = navController, winner = winner, playViewModel = playViewModel)
        }




    }
}