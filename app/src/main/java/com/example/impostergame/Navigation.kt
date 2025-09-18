package com.example.impostergame

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.impostergame.Screens.*
import com.google.firebase.analytics.FirebaseAnalytics

@Composable
fun Navigation(){
    val navController = rememberNavController()
    val gameSetupViewModel: GameSetupViewModel = viewModel()

    // ✅ Create PlayViewModel with factory
    val context = LocalContext.current
    val analytics = FirebaseAnalytics.getInstance(context)
    val factory = remember { PlayViewModelFactory(analytics) }
    val playViewModel: PlayViewModel = viewModel(factory = factory)


    NavHost(navController = navController, startDestination = "splash"){
        composable("splash"){
            SplashScreen(navController = navController)
        }

        composable("home"){ HomeScreen(navController = navController, viewModel = gameSetupViewModel) }

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
            GameOverScreen(navController = navController, winner = winner, playViewModel = playViewModel, gameSetupViewModel = gameSetupViewModel )
        }




    }
}