package com.example.impostergame

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class GameSetupViewModel : ViewModel() {
    // Player list
    val players = mutableStateListOf<String>()

    // For directly accessing the number of players
    val numberOfPlayers get() = players.size

    // Selected category
    var selectedCategory by mutableStateOf("")


    init {
        repeat(3) { players.add("") } // just empty names
    }

    fun addPlayer(name: String = "") {
        if (players.size < 30) players.add(name)
    }

    fun editPlayer(index: Int, newName: String) {
        if (index in players.indices) {
            players[index] = newName
        }
    }

    fun removePlayer(index: Int) {
        if (players.size > 3 && index in players.indices) {
            players.removeAt(index)

            // ✅ Adjust imposters if they exceed allowed max
            if (numberOfImposters > players.size - 2) {
                numberOfImposters = (players.size - 2).coerceAtLeast(1)
            }

        }
    }

    // Number of imposters
    var numberOfImposters by mutableStateOf(1)
        private set

    fun increaseImposters() {
        if (numberOfImposters < players.size - 2) { // max imposters < total players
            numberOfImposters++
        }
    }

    fun decreaseImposters() {
        if (numberOfImposters > 1) {
            numberOfImposters--
        }
    }

    fun resetGame() {
        players.clear()
        repeat(3) { players.add("") } // back to 3 empty players
        numberOfImposters = 1
        selectedCategory = ""
    }

}