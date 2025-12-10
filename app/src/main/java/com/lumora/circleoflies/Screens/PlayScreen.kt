package com.lumora.circleoflies.Screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lumora.circleoflies.GameSetupViewModel
import com.lumora.circleoflies.PlayViewModel
import com.lumora.circleoflies.R


@Composable
fun PlayScreen(
    navController: NavController,
    gameSetupViewModel: GameSetupViewModel,
    playViewModel: PlayViewModel
) {

    // Reset and assign words when screen loads
    LaunchedEffect(Unit) {
        playViewModel.startRound(
            selectedCategory = gameSetupViewModel.selectedCategory,
            players = gameSetupViewModel.players,
            numberOfImposters = gameSetupViewModel.numberOfImposters
        )
    }


    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    val isHeld = playViewModel.isWordRevealed.value

    val players = gameSetupViewModel.players // This comes directly from setup

    val currentIndex = playViewModel.currentPlayerIndex.value

    val currentPlayerName = players
        .getOrNull(currentIndex)
        ?.takeIf { it.isNotBlank() }
        ?: "Player ${currentIndex + 1}"

    val playerCounter = "${currentIndex + 1}/${players.size}"

    val animatedWidth by animateDpAsState(
        targetValue = if (isHeld) screenWidth * 1f else screenWidth * 0.8f,
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
        label = "btnWidth"
    )
    val animatedHeight by animateDpAsState(
        targetValue = if (isHeld) screenHeight * 0.25f else screenHeight * 0.12f,
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
        label = "btnHeight"
    )

    var showExitDialog by remember { mutableStateOf(false) }

    BackHandler(enabled = true) {
        showExitDialog = true
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Game in progress") },
            text = { Text("Are you sure you want to quit?") },
            confirmButton = {
                TextButton(onClick = {
                    showExitDialog = false
                    // reset and go home

                    navController.navigate("gameSetup") {
                        popUpTo("gameSetup") { inclusive = true }
                    }
                }) {
                    Text("Quit", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("Cancel")
                }
            },
            containerColor = Color.Black,
            titleContentColor = Color.White,
            textContentColor = Color.White
        )
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                if (isHeld) Color(0xFFA8A7A7) else Color(0xFFD6FFE0) // Gray when held
            ),
        contentAlignment = Alignment.Center
    ) {


        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Top icon
            Icon(
                painter = painterResource(id = R.drawable.blackmask), // replace with your mask drawable
                contentDescription = null,
                modifier = Modifier
                    .padding(top = screenHeight * 0.06f)
                    .size(screenWidth * 0.2f),
                tint = Color.Unspecified
            )



            Spacer(modifier = Modifier.height(screenHeight * 0.07f))

            // Player's turn text
            Text(
                text = "$currentPlayerName’s Turn",
                fontSize = screenHeight.value.sp * 0.04f,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(screenHeight * 0.17f))

            Box(
                contentAlignment = Alignment.Center
            ) {
            // Press and hold button
            Box(
                modifier = Modifier
                    .width(animatedWidth)
                    .height(animatedHeight)
                    .clip(GenericShape { size, _ ->
                        addOval(androidx.compose.ui.geometry.Rect(0f, 0f, size.width, size.height))
                    })
                    .background(Color(0xFF7B2D2D))
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                // Pass the current player's name to onPress
                                val currentPlayerName = players.getOrNull(playViewModel.currentPlayerIndex.value)
                                    ?.takeIf { it.isNotBlank() } ?: "Player ${playViewModel.currentPlayerIndex.value + 1}"

                                playViewModel.onPress(currentPlayerName)

                                tryAwaitRelease()

                                playViewModel.onRelease(totalPlayers = players.size).let { finished ->
                                    if (finished) {
                                        navController.navigate("voting")
                                    }
                                }
                            }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isHeld)
                        playViewModel.currentWord.value
                    else
                        "Press and hold\n to reveal word",
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontSize = screenHeight.value.sp * 0.025f,
                    modifier = Modifier.offset(
                        y = if (isHeld) (-80).dp else 0.dp // shift up when held
                    )

                )
            }


            }

            Spacer(modifier = Modifier.height(screenHeight * 0.2f))

            // Player counter at bottom
            Text(
                text = playerCounter,
                fontSize = screenHeight.value.sp * 0.025f,
                color = Color.Black,
                modifier = Modifier.padding(bottom = screenHeight * 0.03f),

            )
        }



    }
}



