package com.example.recuperatorioappsmoviles.viewModels

import androidx.lifecycle.ViewModel
import com.example.recuperatorioappsmoviles.feature.GameStatus
import com.example.recuperatorioappsmoviles.feature.NumberMasterState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {

    // Estado privado mutable
    private val _uiState = MutableStateFlow(NumberMasterState())
    // Estado público inmutable (Exigencia MVVM)
    val uiState = _uiState.asStateFlow()

    // Actualizar lo que escribe el usuario (State Hoisting)
    fun onInputChange(newInput: String) {
        _uiState.update { it.copy(currentInput = newInput) }
    }

    // Lógica del juego
    fun makeGuess() {
        val inputInt = _uiState.value.currentInput.toIntOrNull() ?: return

        _uiState.update { currentState ->
            val newAttempts = currentState.attemptsTaken + 1
            var newStatus = currentState.status

            if (inputInt == currentState.targetNumber) {
                newStatus = GameStatus.Victory("¡Acertaste el ${currentState.targetNumber}!")
            } else if (newAttempts >= currentState.config.maxAttempts) {
                newStatus = GameStatus.GameOver("Era el ${currentState.targetNumber}...")
            }

            // IMPORTANTE: Copy crea un nuevo objeto estado (Inmutabilidad)
            currentState.copy(
                attemptsTaken = newAttempts,
                status = newStatus,
                currentInput = "" // Limpiamos el input
            )
        }
    }

    // Reiniciar juego
    fun resetGame() {
        _uiState.update {
            it.copy(
                attemptsTaken = 0,
                targetNumber = (0..it.config.rangeEnd).random(),
                status = GameStatus.Active,
                currentInput = ""
            )
        }
    }

}