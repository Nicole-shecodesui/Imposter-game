package com.example.impostergame.Screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.impostergame.BannerAdView
import com.example.impostergame.GameSetupViewModel
import com.example.impostergame.PlayViewModel
import kotlinx.coroutines.launch

@Composable
fun VotingScreen(
    navController: NavController,
    playViewModel: PlayViewModel,
    gameSetupViewModel: GameSetupViewModel

) {
    val activePlayers = playViewModel.activePlayers // UI will react to changes


    var selectedPlayer by remember { mutableStateOf<String?>(null) }


    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    var showBackDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var showDialog by remember { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("") }
    var dialogMessage by remember { mutableStateOf("") }

    BackHandler {
        showBackDialog = true
    }

    if (showBackDialog) {
        AlertDialog(
            onDismissRequest = { showBackDialog = false },
            title = { Text("Exit Voting?", fontWeight = FontWeight.Bold, color = Color.White) },
            text = { Text("Going back will reset the current game. Are you sure?", color = Color.White) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showBackDialog = false
                        navController.navigate("gameSetup") {
                            popUpTo("voting") { inclusive = true }
                        }
                    }
                ) { Text("Yes", color = Color.Cyan) }
            },
            dismissButton = {
                TextButton(onClick = { showBackDialog = false }) {
                    Text("Cancel", color = Color.Cyan)
                }
            },
            containerColor = Color(0xFF2C2C2C),
            shape = RoundedCornerShape(16.dp)
        )
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(dialogTitle, fontWeight = FontWeight.Bold, color = Color.White) },
            text = { Text(dialogMessage, color = Color.White) },
            confirmButton = {
                TextButton(
                    onClick = { showDialog = false }
                ) {
                    Text("Continue", color = Color.Cyan)
                }
            },
            containerColor = Color(0xFF2C2C2C), // dark background
            shape = RoundedCornerShape(16.dp)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD6FFE0)) // light green background
            .padding(screenWidth * 0.04f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(screenHeight * 0.09f))

        Text(
            text = "VOTING TIME!!",
            fontWeight = FontWeight.Bold,
            fontSize = (screenWidth.value * 0.07f).sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(screenHeight * 0.02f))

        Row {
            Text(
                "Who do you think is the ",
                fontSize = (screenWidth.value * 0.05f).sp,
                color = Color.Black
            )
            Text(
                "IMPOSTER?",
                color = Color(0xFFff3131),
                fontWeight = FontWeight.Bold,
                fontSize = (screenWidth.value * 0.05f).sp
            )
        }

        Spacer(modifier = Modifier.height(screenHeight * 0.03f))

        // Player Buttons with animated color change
        activePlayers.forEachIndexed { index, player ->
            val displayName = player.takeIf { it.isNotBlank() } ?: "Player ${index + 1}"
            val isSelected = displayName == selectedPlayer
            val backgroundColor by animateColorAsState(
                targetValue = if (isSelected) Color(0xFFFFC1C1) else Color(0xFF9FA8FF),
                animationSpec = tween(durationMillis = 300),
                label = ""
            )

            Button(
                onClick = { selectedPlayer = displayName },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(vertical = screenHeight * 0.007f),
                colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
                shape = RoundedCornerShape(screenWidth * 0.02f)
            ) {
                Text(displayName, color = Color.Black, fontSize = (screenWidth.value * 0.045f).sp)
            }
        }

        Spacer(modifier = Modifier.height(screenHeight * 0.03f))

        SnackbarHost(hostState = snackbarHostState)

        Button(
            onClick = {

                if (selectedPlayer == null) {
                    // Show snackbar if no player selected
                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = "Please select a player to eliminate!",
                            actionLabel = "Dismiss", // adds a manual dismiss button
                            duration = SnackbarDuration.Short
                        )

                        when (result) {
                            SnackbarResult.Dismissed -> println("Auto-dismissed after Short duration")
                            SnackbarResult.ActionPerformed -> println("User tapped Dismiss")
                        }
                    }


                    return@Button
                }


                selectedPlayer?.let { player ->
                    // Check if player was an imposter before removing
                    val wasImposter = playViewModel.isPlayerImposter(player)

                    // Remove from voting UI (and update counts)
                    playViewModel.eliminatePlayer(player)

                    // Count remaining imposters
                    val impostersLeft = playViewModel.remainingImpostersCount()
                    val playersLeft = playViewModel.remainingPlayersCount()


                    // Show dialog
                    when {
                        // Civilian victory
                        wasImposter && impostersLeft == 0 -> {
                            navController.navigate("gameOver/winner=Civilians")
                        }

                        // Imposter victory (only 2 left: 1 imposter + 1 civilian)
                        playersLeft == 2 && impostersLeft == 1 -> {
                            navController.navigate("gameOver/winner=Imposters")
                        }

                        else -> {
                            dialogTitle = "$player Eliminated"
                            dialogMessage = if (wasImposter) {
                                "Imposter eliminated! Keep going."
                            } else {
                                "$player was innocent 😔\nThe imposter is still hiding!!"
                            }
                            showDialog = true
                        }


                    }

                    selectedPlayer = null
                }
            },
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .height(screenHeight * 0.06f),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7B2D2D)),
            shape = RoundedCornerShape(screenWidth * 0.03f)
        ) {
            Text("Eliminate", color = Color.White, fontSize = (screenWidth.value * 0.045f).sp)
        }


    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Made with ❤️ by Angwenyi",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(6.dp)
                )
            BannerAdView()
        }
    }


}



