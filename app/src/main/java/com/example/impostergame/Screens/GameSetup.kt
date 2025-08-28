package com.example.impostergame.Screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.impostergame.GameSetupViewModel
import com.example.impostergame.R
import kotlinx.coroutines.launch

@Composable
fun GameSetup(navController: NavController, viewModel: GameSetupViewModel ) {
    val configuration = LocalConfiguration.current
    val screenWidth = remember { configuration.screenWidthDp }
    val screenHeight = remember { configuration.screenHeightDp }


    var showExitDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Scaled sizes
    val titleFontSize = remember(screenWidth) { (screenWidth * 0.07).sp }
    val sectionFontSize = remember(screenWidth) { (screenWidth * 0.045).sp }
    val categoryFontSize = (screenWidth * 0.04).sp
    val startButtonFontSize = (screenWidth * 0.05).sp
    val paddingSize = (screenWidth * 0.07).dp
    val sectionPadding = (screenWidth * 0.04).dp
    val buttonHeight = (screenHeight * 0.07).dp
    val iconSize = remember(screenWidth) { screenWidth.dp * 0.05f }



    val categories = listOf(
        "Animals", "Food & Drinks",
        "Movies & TV", "Everyday Objects",
        "Music", "Sports",
        "Clothes", "Kenyan Things"
    )

    // ✅ Catch phone back press
    BackHandler {
        showExitDialog = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD8FFDE))
            .padding(paddingSize),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(paddingSize))


        Text(
            text = "Set up Game",
            fontSize = titleFontSize,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            modifier = Modifier.padding(vertical = paddingSize)
        )


        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFa8c8a8)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = screenWidth.dp * 0.02f),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),

        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = screenHeight.dp * 0.015f)
            ) {
                // Players Section
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("players") }
                        .padding(horizontal = screenWidth.dp * 0.04f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Players",
                        fontSize = sectionFontSize,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1A4A1A)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Text(
                            text = viewModel.numberOfPlayers.toString(),
                            fontSize = sectionFontSize,
                            color = Color(0xFF1A4A1A)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.right), // Your drawable here
                            contentDescription = "Players Arrow",
                            modifier = Modifier
                                .size(iconSize)
                                .padding(start = screenWidth.dp * 0.01f),
                        )
                    }
                }

                // Divider
                Divider(
                    color = Color.Gray,
                    thickness = 2.dp,
                    modifier = Modifier.padding(vertical = screenHeight.dp * 0.01f)
                )

                // Number of Imposters Section
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = screenWidth.dp * 0.04f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Number of Imposters",
                        fontSize = sectionFontSize,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1A4A1A)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = viewModel.numberOfImposters.toString(),
                            fontSize = sectionFontSize,
                            color = Color(0xFF1A4A1A),
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Column(
                            verticalArrangement = Arrangement.spacedBy(2.dp), // controls arrow gap
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.up),
                                contentDescription = "Increase",
                                modifier = Modifier
                                    .size(14.dp)
                                    .clickable { viewModel.increaseImposters() },
                                tint = Color(0xFF1A4A1A)
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.down),
                                contentDescription = "Decrease",
                                modifier = Modifier
                                    .size(14.dp)
                                    .clickable { viewModel.decreaseImposters() },
                                tint = Color(0xFF1A4A1A)
                            )
                        }

                    }
                }
            }
        }




        Spacer(modifier = Modifier.height(paddingSize))

        // Category Title
        Text(
            text = "Select category",
            fontSize = sectionFontSize,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = sectionPadding),
            textAlign = TextAlign.Start
        )

        // Category Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(sectionPadding / 2),
            verticalArrangement = Arrangement.spacedBy(sectionPadding / 2),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories.size) { index ->
                val category = categories[index]
                Button(
                    onClick = { viewModel.selectedCategory = category },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(buttonHeight),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (viewModel.selectedCategory == category)
                            Color(0xFF722f37) else Color(0xFF2D5A41)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp) // Control inner padding

                ) {
                    Text(
                        text = category,
                        color = Color.White,
                        fontSize = categoryFontSize,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Start Button
        Button(
            onClick = { if (viewModel.selectedCategory.isNotEmpty()) {
                navController.navigate("play")
            } else {
                // Show snackbar notification
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Please select a category!",
                        duration = SnackbarDuration.Short // Options: Short, Medium, Long
                    )                }
            }},
            modifier = Modifier
                .width(screenWidth.dp * 0.6f)
                .height(buttonHeight)
                .padding(horizontal = screenWidth.dp * 0.1f),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF722f37)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Start",
                color = Color.White,
                fontSize = startButtonFontSize,
                fontWeight = FontWeight.Medium
            )
        }


        // ✅ Popup confirmation
        if (showExitDialog) {
            AlertDialog(
                onDismissRequest = { showExitDialog = false },
                title = { Text("Go back?") },
                text = { Text("This will reset your players and imposters.") },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.resetGame()   // <-- reset players & imposters
                        showExitDialog = false
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    }) {
                        Text("Yes, reset")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showExitDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        // Add SnackbarHost somewhere in your layout
        SnackbarHost(
            hostState = snackbarHostState
        ) { data ->
            Snackbar(
                snackbarData = data,
                containerColor = Color(0xFF2A2929), // Background color
                contentColor = Color.White            // Text color
            )
        }


        Spacer(modifier = Modifier.height(paddingSize))
    }
}


