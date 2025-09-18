package com.example.impostergame

import android.os.Bundle
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.analytics.FirebaseAnalytics

class PlayViewModel(private val analytics: FirebaseAnalytics) : ViewModel() {
    var currentPlayerIndex = mutableStateOf(0)
        private set

    var isWordRevealed = mutableStateOf(false)
        private set

    var currentWord = mutableStateOf("Press and hold to reveal word")
        private set

    private val allImposters = mutableSetOf<String>()


    val activePlayers = mutableStateListOf<String>()

    // Maps player names to their words
    val playerWords = mutableStateMapOf<String, String>()

    // Imposters tracked by names
    private val imposterNames = mutableSetOf<String>()

    private var roundWord: String = "Word"

    var gameOver = mutableStateOf(false)
        private set
    var winner = mutableStateOf("")
        private set

    fun startRound(selectedCategory: String, players: List<String>, numberOfImposters: Int) {
        // Clean names (map empty to Player 1, 2, ...)
        val cleanPlayers = players.mapIndexed { index, name ->
            name.takeIf { it.isNotBlank() } ?: "Player ${index + 1}" }

        // Reset active players for voting
        activePlayers.clear()
        activePlayers.addAll(cleanPlayers)


        // Pick imposters
        imposterNames.clear()
        imposterNames.addAll(cleanPlayers.shuffled().take(numberOfImposters))

        allImposters.clear()
        allImposters.addAll(imposterNames)

        // Pick word for non-imposters
        val wordList = WordRepository.wordPools[selectedCategory] ?: listOf("Word")
        roundWord = wordList.random()

        // Assign words
        playerWords.clear()
        cleanPlayers.forEach { player ->
            playerWords[player] = if (imposterNames.contains(player))
                "You are the imposter"
            else {
                roundWord
            }
        }

        currentPlayerIndex.value = 0
        isWordRevealed.value = false
        currentWord.value = "Press and hold to reveal word"


        // reset game state
        gameOver.value = false
        winner.value = ""


        val bundle = Bundle().apply {
            putString("category", selectedCategory)
            putLong("players_count", players.size.toLong())
            putLong("imposters_count", numberOfImposters.toLong())
        }
        analytics.logEvent("round_start", bundle)



    }

    fun isPlayerImposter(playerName: String): Boolean {
        return playerName in imposterNames
    }

    fun remainingImpostersCount(): Int = imposterNames.size


    fun eliminatePlayer(playerName: String) {
        activePlayers.remove(playerName)   // remove from button UI
        playerWords.remove(playerName)     // clean word
        imposterNames.remove(playerName)   // update imposters
        checkGameOver()
    }

    fun onPress(currentPlayer: String) {
        // currentPlayer is the cleaned display name
        currentWord.value = playerWords[currentPlayer] ?: roundWord
        isWordRevealed.value = true
    }



    fun onRelease(totalPlayers: Int): Boolean {
        isWordRevealed.value = false
        currentWord.value = "Press and hold to reveal word"
        return if (currentPlayerIndex.value < totalPlayers - 1) {
            currentPlayerIndex.value += 1
            false
        } else {
            true
        }
    }


    private fun checkGameOver() {
        val remainingImposters = activePlayers.count { it in imposterNames }
        val remainingCivilians = activePlayers.size - remainingImposters

        when {
            remainingImposters == 0 -> {
                winner.value = "Innocents"
                gameOver.value = true
            }
            remainingImposters == 1 && activePlayers.size == 2 -> {
                winner.value = "Imposters"
                gameOver.value = true
            }
        }
        if (gameOver.value) {
            val remainingImposters = activePlayers.count { it in imposterNames }
            val bundle = Bundle().apply {
                putString("winner", winner.value)
                putLong("remaining_players", activePlayers.size.toLong())
                putLong("remaining_imposters", remainingImposters.toLong())
            }
            analytics.logEvent("game_over", bundle)
        }


    }

    fun remainingPlayersCount(): Int = activePlayers.size


    // 🔹 New: Return imposters list for GameOverScreen
    fun getAllImposters(): List<String> = allImposters.toList()


    fun resetRound() {
        currentPlayerIndex.value = 0
        isWordRevealed.value = false
        currentWord.value = "Press and hold to reveal word"
        activePlayers.clear()
        playerWords.clear()
        imposterNames.clear()
        allImposters.clear()
        gameOver.value = false
        winner.value = ""
    }

}

// 🔹 Factory to create PlayViewModel with FirebaseAnalytics
class PlayViewModelFactory(private val analytics: FirebaseAnalytics) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlayViewModel(analytics) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
