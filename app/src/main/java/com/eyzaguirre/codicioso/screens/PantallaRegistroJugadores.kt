package com.eyzaguirre.codicioso.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eyzaguirre.codicioso.JuegoViewModel

@Composable
fun PantallaRegistroJugadores(
    viewModel: JuegoViewModel,
    onNavegar: () -> Unit
) {
    val estado by viewModel.estado.collectAsState()
    var nombreTexto by remember { mutableStateOf("") }
    var dadoTexto by remember { mutableStateOf("") }
    var errorMensaje by remember { mutableStateOf("") }

    val jugadoresRegistrados = estado.jugadores
    val totalEsperados = viewModel.cantidadJugadoresEsperados
    val todosRegistrados = jugadoresRegistrados.size == totalEsperados

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Registro de jugadores",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Jugador ${jugadoresRegistrados.size + 1} de $totalEsperados",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (!todosRegistrados) {
            OutlinedTextField(
                value = nombreTexto,
                onValueChange = { nombreTexto = it },
                label = { Text("Nombre del jugador") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = dadoTexto,
                onValueChange = { dadoTexto = it },
                label = { Text("Valor del dado (1-6)") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (errorMensaje.isNotEmpty()) {
                Text(
                    text = errorMensaje,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val dado = dadoTexto.toIntOrNull()
                    when {
                        nombreTexto.isBlank() -> {
                            errorMensaje = "Ingresa el nombre del jugador"
                        }
                        dado == null || dado < 1 || dado > 6 -> {
                            errorMensaje = "El dado debe ser un valor entre 1 y 6"
                        }
                        jugadoresRegistrados.any {
                            it.dadoInicial == dado
                        } -> {
                            errorMensaje =
                                "Ese valor ya fue sacado por otro jugador, vuelvan a lanzar"
                        }
                        else -> {
                            errorMensaje = ""
                            viewModel.registrarDadoInicial(
                                nombreTexto.trim(),
                                dado
                            )
                            nombreTexto = ""
                            dadoTexto = ""
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Registrar jugador", fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Lista de jugadores ya registrados
        if (jugadoresRegistrados.isNotEmpty()) {
            Text(
                text = "Jugadores registrados:",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(jugadoresRegistrados) { jugador ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = jugador.nombre,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Dado: ${jugador.dadoInicial}",
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }

        if (todosRegistrados) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "¡Todos registrados! El orden de juego se definirá por el mayor dado.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.ordenarJugadores()
                    onNavegar()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Comenzar juego", fontSize = 16.sp)
            }
        }
    }
}