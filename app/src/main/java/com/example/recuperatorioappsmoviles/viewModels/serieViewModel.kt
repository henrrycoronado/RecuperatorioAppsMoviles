package com.example.recuperatorioappsmoviles.viewModels

import androidx.lifecycle.ViewModel
import com.example.recuperatorioappsmoviles.feature.SerieState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SerieViewModel : ViewModel() {

    // MutableStateFlow permite actualizar el estado internamente.
    private val _state = MutableStateFlow(SerieState())

    // Exponemos como StateFlow (inmutable) para que la UI solo pueda leer (Pattern Observer).
    val state: StateFlow<SerieState> = _state.asStateFlow()

    fun onInputChange(newValue: String) {
        _state.update { it.copy(nInput = newValue) }
    }

    fun onErrorShown() {
        _state.update { it.copy(errorMessage = null) }
    }

    /**
     * Lógica principal del examen.
     * Valida la entrada y genera la serie híbrida.
     */
    fun generateSerie() {
        val nStr = _state.value.nInput
        val n = nStr.toIntOrNull()

        // 1. Validación: Debe ser entero y mayor a 0
        if (n == null || n <= 0) {
            _state.update {
                it.copy(errorMessage = "Por favor, ingresa un número entero positivo mayor a 0.")
            }
            return
        }

        // 2. Generación de la serie
        val numbers = generateHybridSequence(n)

        // 3. Actualización de estado con éxito
        _state.update {
            it.copy(
                resultString = numbers.joinToString(", "),
                errorMessage = null
            )
        }
    }

    // Algoritmo Iterativo Híbrido (No recursivo)
    private fun generateHybridSequence(n: Int): List<Long> {
        val sequence = mutableListOf<Long>()

        // Variables para lógica impar (Serie multiplicativa)
        var lastOddValue: Long = 1
        var oddMultiplier: Long = 1

        for (i in 0 until n) {
            if (i % 2 == 0) {
                // POSICIONES PARES (0, 2, 4...): Lógica Fibonacci
                // Calculamos Fibonacci del índice actual 'i'
                sequence.add(calculateFibonacciIterative(i))
            } else {
                // POSICIONES IMPARES (1, 3, 5...): Lógica Multiplicativa
                // Secuencia: 1, 2, 5, 16...
                // Lógica: Valor = (Anterior * k) + 1
                if (i == 1) {
                    sequence.add(1)
                    lastOddValue = 1
                } else {
                    val nextVal = (lastOddValue * oddMultiplier) + 1
                    sequence.add(nextVal)
                    lastOddValue = nextVal
                    oddMultiplier++ // Incrementamos el multiplicador (k)
                }
            }
        }
        return sequence
    }

    // Helper para Fibonacci Iterativo (Evita StackOverflow)
    private fun calculateFibonacciIterative(n: Int): Long {
        if (n <= 1) return n.toLong()
        var a = 0L
        var b = 1L
        for (k in 2..n) {
            val temp = a + b
            a = b
            b = temp
        }
        return b
    }
}