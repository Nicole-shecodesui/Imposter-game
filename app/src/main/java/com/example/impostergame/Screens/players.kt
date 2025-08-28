package com.example.impostergame.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.impostergame.GameSetupViewModel

@Composable
fun players(navController: NavController,
            viewModel: GameSetupViewModel ){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFC8FAD0)) // light green background
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Spacer(modifier = Modifier.height(28.dp))

        // Top Bar
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .size(28.dp)
                    .clickable { navController.popBackStack()  },
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Players",
                color = Color.Black,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Players list
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFa8c8a8), shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            viewModel.players.forEachIndexed { index, playerName ->

                var showDialog by remember { mutableStateOf(false) }
                var newName by remember { mutableStateOf(playerName) }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (playerName.isNotBlank()) playerName else "Player ${index + 1}",
                        color = Color(0xFF2d5a41),
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                    )

                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Player",
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                showDialog = true
                            },
                        tint = Color(0xFF2d5a41)
                    )
                }


                // Add divider between players, but not after the last one
                if (index < viewModel.players.lastIndex) {
                    Divider(color = Color(0xFF5A5D5A), thickness = 2.dp)
                }
                // Edit Player Dialog
                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text("Edit Player") },
                        text = {
                            OutlinedTextField(
                                value = newName,
                                onValueChange = { newName = it },
                                label = { Text("Player Name") },
                                singleLine = true
                            )
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                if (newName.isNotBlank()) {
                                    viewModel.editPlayer(index, newName)
                                }
                                showDialog = false
                            }) {
                                Text("Save")
                            }
                        },
                        dismissButton = {
                            Row {
                                TextButton(onClick = {
                                    viewModel.removePlayer(index)  // Remove player action
                                    showDialog = false
                                }) {
                                    Text("Remove Player", color = Color.Red)  // Highlight remove button in red
                                }
                                Spacer(modifier = Modifier.width(8.dp)) // Optional spacing
                                TextButton(onClick = { showDialog = false }) {
                                    Text("Cancel")
                                }
                            }
                        }
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(24.dp))

        // Add Player Button
        Button(
            onClick = { viewModel.addPlayer() },
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "Add player", color = Color.Black)
        }
    }

}

