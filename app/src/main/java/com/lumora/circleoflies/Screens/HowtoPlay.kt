package com.lumora.circleoflies.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun HowtoPlay(navController: NavController){
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val titleFontSize = screenWidth.value * 0.07f
    val sectionTitleFontSize = screenWidth.value * 0.05f
    val bodyFontSize = screenWidth.value * 0.045f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFd8ffde))
            .padding(16.dp)
    ) {
        // Back Arrow
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
        }

        // Scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Title
            Text(
                text = "How to Play Circle of Lies!",
                fontSize = titleFontSize.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                color = Color.Black,
                style = TextStyle(textDecoration = TextDecoration.Underline)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Goal note
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFCDB6), RoundedCornerShape(10.dp))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.Black,
                                textDecoration = TextDecoration.Underline,
                                fontWeight = FontWeight.Bold,
                                fontSize = sectionTitleFontSize.sp // responsive font size
                            )
                        ) {
                            append("Goal\n")
                        }
                        withStyle(
                            style = SpanStyle(
                                color = Color.Black,
                                fontSize = bodyFontSize.sp // responsive font size for body
                            )
                        ) {
                            append(": Civilians catch imposters.\n: Imposters survive till the end.")
                        }
                    },
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(12.dp))


            // Setup section
            Text(
                text = "Setup",
                fontSize = sectionTitleFontSize.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Add players and choose how many imposters. Everyone gets a secret role!",
                fontSize = bodyFontSize.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Roles section
            Text(
                text = "Roles",
                fontSize = sectionTitleFontSize.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Innocents: You get a word to describe\nImposters: You don't get the word - fake it!",
                fontSize = bodyFontSize.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))

            // How to Play section
            Text(
                text = "How to Play",
                fontSize = sectionTitleFontSize.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            val gameNotes = listOf(
                "Everyone describes the word in their own way.",
                "After everyone speaks, vote on who you think is the imposter and eliminate them.",
                "Keep going until you find the imposter or the imposter wins!"
            )


            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp) // space between bullets
            ) {
                gameNotes.forEach { instruction ->
                    Row(verticalAlignment = Alignment.Top) {
                        Text(
                            text = "•",
                            fontSize = bodyFontSize.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = instruction,
                            fontSize = bodyFontSize.sp,
                            color = Color.Black
                        )
                    }
                }
            }


            // Game Notes section
            Text(
                text = "Game Notes",
                fontSize = sectionTitleFontSize.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            val gameInstructions = listOf(
                "Civilians Win: Catch all the imposters and you win the game!",
                "Imposters Win: If only 2 players remain (1 civilian + 1 imposter), the imposter wins instantly.",
                "Minimum Civilians: There must always be at least 2 civilians in the game.",
                "Imposter Limit: Imposters can never be more than the total players minus 2.",
                "Adjust Difficulty: Change the number of imposters to make the game easier or harder.",
                "Best Play: The game is most fun with at least 3 players.",
                "Strategy Tip: Be sneaky and creative with your descriptions to trick others!"


            )


            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp) // space between bullets
            ) {
                gameInstructions.forEach { instruction ->
                    Row(verticalAlignment = Alignment.Top) {
                        Text(
                            text = "✅",
                            fontSize = bodyFontSize.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = instruction,
                            fontSize = bodyFontSize.sp,
                            color = Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Bottom note
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFffb6b6), RoundedCornerShape(50.dp))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Remember: Trust no one, suspect everyone and have a blast! 😈",
                    fontWeight = FontWeight.Bold,
                    fontSize = bodyFontSize.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HowtoPlayPreview() {
    HowtoPlay(navController = rememberNavController())
}