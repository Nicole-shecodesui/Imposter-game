package com.example.impostergame.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.impostergame.R


@Composable
fun HomeScreen(navController: NavController) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val imageSize = screenWidth * 0.6f  // proportional image size
    val buttonWidth = screenWidth * 0.7f
    val buttonHeight = screenHeight * 0.07f
    val titleFontSize = screenWidth.value * 0.08f
    val subtitleFontSize = screenWidth.value * 0.05f
    val footerFontSize = screenWidth.value * 0.035f

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
                "Share with friends" to "shareScreen",
                "Feedback" to "feedbackScreen"
            )
            buttons.forEach { (label, route) ->
                Button(
                    onClick = { navController.navigate(route) },
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

        // Footer
        Text(
            text = "Made with ❤️ by Angwenyi",
            fontSize = footerFontSize.sp,
            textAlign = TextAlign.Center,
            color = Color.Black
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController())
}