package com.example.impostergame.Screens

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.impostergame.BannerAdView
import com.example.impostergame.GameSetupViewModel
import com.example.impostergame.R
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


@Composable
fun HomeScreen(navController: NavController, viewModel: GameSetupViewModel ) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val imageSize = screenWidth * 0.6f  // proportional image size
    val buttonWidth = screenWidth * 0.7f
    val buttonHeight = screenHeight * 0.07f
    val titleFontSize = screenWidth.value * 0.08f
    val subtitleFontSize = screenWidth.value * 0.05f
    val footerFontSize = screenWidth.value * 0.035f

    val context = LocalContext.current


    var showExitDialog by remember { mutableStateOf(false) }

    var showFeedbackDialog by remember { mutableStateOf(false) }

    var showConfirmation by remember { mutableStateOf(false) }


    if (showFeedbackDialog) {
        FeedbackDialog(
            onDismiss = { showFeedbackDialog = false },
            onSubmit = { feedback ->

                val db = Firebase.firestore
                val feedbackData = hashMapOf(
                    "message" to feedback,
                    "timestamp" to System.currentTimeMillis()
                )

                db.collection("feedback")
                    .add(feedbackData)
                    .addOnSuccessListener {
                        println("Feedback saved!")
                        // TODO: Show a confirmation dialog/snackbar here
                    }
                    .addOnFailureListener { e ->
                        println("Error saving feedback: $e")
                    }

                showFeedbackDialog = false

                showConfirmation = true
            }
        )
    }

    if (showConfirmation) {
        AlertDialog(
            onDismissRequest = { showConfirmation = false },
            title = { Text("Thank you!") },
            text = { Text("Your feedback has been submitted.") },
            confirmButton = {
                TextButton(onClick = { showConfirmation = false }) {
                    Text("OK")
                }
            }
        )
    }

    BackHandler {
        showExitDialog = true
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFd8ffde))
            .padding(top = 60.dp, bottom = 30.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title section
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Circle of Lies",
                fontSize = titleFontSize.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                color = Color.Black
            )
            Text(
                text = "Imposter Game",
                fontSize = subtitleFontSize.sp,
                fontStyle = FontStyle.Italic,
                color = Color.Black
            )
            // Mask Image
            Image(
                painter = painterResource(id = R.drawable.blackmask),
                contentDescription = "Mask Image",
                modifier = Modifier.size(imageSize)
            )
        }



        // Buttons
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val buttons = listOf(
                "Start new Game" to "gameSetup",
                "How to play" to "howtoPlayScreen",
                "Share with friends" to "share",
                "Feedback" to "feedback"
            )
            buttons.forEach { (label, route) ->
                Button(
                    onClick = {
                        when (route) {
                            "feedback" -> {
                                showFeedbackDialog = true
                            }
                            "share" -> {
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        "Hey! 🎮 Join me in playing the Imposter game. Download it here: <your_link_here>"
                                    )
                                }
                                context.startActivity(
                                    Intent.createChooser(shareIntent, "Share via")
                                )
                            }
                            else -> {
                                navController.navigate(route)
                            }
                        }
                    },
                    modifier = Modifier
                        .width(buttonWidth)
                        .height(buttonHeight),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF722f37))
                ) {
                    Text(
                        text = label,
                        fontSize = subtitleFontSize.sp,
                        color = Color.White
                    )
                }
            }


        }


        Spacer(modifier = Modifier.height(6.dp))

        // Footer
        Text(
            text = "Made with ❤️ by Angwenyi",
            fontSize = footerFontSize.sp,
            textAlign = TextAlign.Center,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(6.dp))

        BannerAdView()
    }

    // Exit confirmation dialog
    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Exit Game") },
            text = { Text("Are you sure you want to quit the game?") },
            confirmButton = {
                TextButton(onClick = {
                    showExitDialog = false
                    // Close the app
                    android.os.Process.killProcess(android.os.Process.myPid())
                }) {
                    Text("Quit", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}


@Composable
fun FeedbackDialog(
    onDismiss: () -> Unit,
    onSubmit: (String) -> Unit
) {
    var feedbackText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Feedback") },
        text = {
            Column {
                Text("We’d love to hear your thoughts!")

                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = feedbackText,
                    onValueChange = { feedbackText = it },
                    placeholder = { Text("Type your feedback here...") }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (feedbackText.isNotBlank()) {
                    onSubmit(feedbackText)
                    onDismiss()
                }
            }) {
                Text("Submit")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

