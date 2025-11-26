package com.example.recuperatorioappsmoviles.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.recuperatorioappsmoviles.feature.GameStatus
import com.example.recuperatorioappsmoviles.viewModels.GameViewModel

@Composable
fun PlayScreen(
    viewModel: GameViewModel
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Encabezado
        Text(text = "Adivina (0 - ${state.config.rangeEnd})", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // Mensaje dinámico según el sealed class
        when (val status = state.status) {
            is GameStatus.Active -> Text("Intentos: ${state.attemptsTaken} / ${state.config.maxAttempts}")
            is GameStatus.Victory -> Text(status.message, color = MaterialTheme.colorScheme.primary)
            is GameStatus.GameOver -> Text(status.message, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Input y Botón (Solo visibles si está Activo)
        if (state.status is GameStatus.Active) {
            OutlinedTextField(
                value = state.currentInput,
                onValueChange = { viewModel.onInputChange(it) },
                label = { Text("Tu número") }
            )
            Button(onClick = { viewModel.makeGuess() }) {
                Text("Probar suerte")
            }
        } else {
            // Botón de Reinicio
            Button(onClick = { viewModel.resetGame() }) {
                Text("Jugar de nuevo")
            }
        }
    }
}