package com.example.impostergame.Screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.impostergame.PlayViewModel
import kotlinx.coroutines.delay

@Composable
fun GameOverScreen(
    navController: NavController,
    playViewModel: PlayViewModel,
    winner: String,
    imposters: List<String> = emptyList() // Pass imposters list here
) {


    val imposters = playViewModel.getAllImposters()

    var highlightButtons by remember { mutableStateOf(false) }

    BackHandler {
        highlightButtons = true
    }


    // Pulse animation
    val infiniteTransition = rememberInfiniteTransition()
    val pulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (highlightButtons) 1.1f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(500),
            repeatMode = RepeatMode.Reverse
        )
    )


    val playAgainColor by animateColorAsState(
        targetValue = if (highlightButtons) Color(0xFFFFD700) else Color(0xFF7B2D2D)
    )

    val homeColor by animateColorAsState(
        targetValue = if (highlightButtons) Color(0xFFFFD700) else Color(0xFF7B2D2D)
    )

    // Reset highlight after 2 seconds
    LaunchedEffect(highlightButtons) {
        if (highlightButtons) {
            delay(2000)
            highlightButtons = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFd8ffde)) // light green background
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Title
        Text(
            text = "Game Over!!!",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(60.dp))

        // Winner text with emoji
        Text(
            text = if (winner == "Imposters") "🤡   IMPOSTER${if (imposters.size > 1) "S" else ""}  ${if (imposters.size > 1) "WIN" else "WINS"}     🤡" else "🛡️   INNOCENTS WIN   🛡️",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = if (winner == "Imposters") Color.Red else Color(0xFF0F9D58),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Imposter list box
        if (imposters.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .background(Color(0xFFa49fff), shape = RoundedCornerShape(1.dp))
                    .padding(34.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "The imposter${if (imposters.size > 1) "s" else ""} ${if (imposters.size > 1) "were :" else "was :"}  ",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                imposters.forEach {
                    Text(
                        text = it,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Buttons
        Button(
            onClick = { navController.navigate("gameSetup") { popUpTo("gameOver") { inclusive = true } } },
            colors = ButtonDefaults.buttonColors(containerColor = playAgainColor),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(50.dp)
                .scale(pulse) // Apply pulsing
        ) {
            Text("Play Again", color = Color.White, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("home") { popUpTo("gameOver") { inclusive = true } } },
            colors = ButtonDefaults.buttonColors(containerColor = homeColor),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(50.dp)
                .scale(pulse) // Apply pulsing
        ) {
            Text("Home", color = Color.White, fontSize = 18.sp)
        }
    }
}


