package com.example.recuperatorioappsmoviles.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.recuperatorioappsmoviles.viewModels.SerieViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SerieScreen(
    viewModel: SerieViewModel
) {
    // Consumimos el StateFlow. Compose recompondrá la UI cada vez que el estado cambie.
    val state by viewModel.state.collectAsState()

    // Host para el Snackbar
    val snackbarHostState = remember { SnackbarHostState() }

    // Efecto secundario para mostrar Snackbar cuando hay error
    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.onErrorShown() // Reseteamos el error tras mostrarlo
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Examen Recuperatorio") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()), // Habilitar scroll vertical
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Generador de Serie Híbrida",
                style = MaterialTheme.typography.headlineSmall
            )

            // Input de Usuario
            OutlinedTextField(
                value = state.nInput,
                onValueChange = { viewModel.onInputChange(it) },
                label = { Text("Ingrese n (cantidad)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Botón de Acción
            Button(
                onClick = { viewModel.generateSerie() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Generar Serie")
            }

            // Tarjeta de Resultado
            if (state.resultString.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Resultado:",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = state.resultString,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}