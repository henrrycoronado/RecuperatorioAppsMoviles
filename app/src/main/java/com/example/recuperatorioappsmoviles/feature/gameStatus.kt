package com.example.recuperatorioappsmoviles.feature
import com.example.recuperatorioappsmoviles.models.GameConfig

sealed class GameStatus {
    data object Active : GameStatus()
    data class Victory(val message: String = "¡Ganaste!") : GameStatus()
    data class GameOver(val message: String = "Perdiste :(") : GameStatus()
}

data class NumberMasterState(
    val currentInput: String = "",
    val attemptsTaken: Int = 0,
    val targetNumber: Int = (0..10).random(),
    val config: GameConfig = GameConfig(),
    val status: GameStatus = GameStatus.Active
)
