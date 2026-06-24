package com.eyzaguirre.codicioso.ui.screens

import androidx.compose.foundation.layout.*
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
fun PantallaConfiguracion(
    viewModel: JuegoViewModel,
    onNavegar: () -> Unit
) {
    var puntajeLimiteTexto by remember { mutableStateOf("10000") }
    var cantidadJugadoresTexto by remember { mutableStateOf("2") }
    var errorMensaje by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🎲 CODICIOSO",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Configuración de la partida",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(40.dp))

        OutlinedTextField(
            value = puntajeLimiteTexto,
            onValueChange = { puntajeLimiteTexto = it },
            label = { Text("Puntaje límite") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = cantidadJugadoresTexto,
            onValueChange = { cantidadJugadoresTexto = it },
            label = { Text("Cantidad de jugadores (2-8)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                val limite = puntajeLimiteTexto.toIntOrNull()
                val cantidad = cantidadJugadoresTexto.toIntOrNull()

                when {
                    limite == null || limite < 1000 -> {
                        errorMensaje = "El puntaje límite debe ser al menos 1000"
                    }
                    cantidad == null || cantidad < 2 || cantidad > 8 -> {
                        errorMensaje = "La cantidad de jugadores debe ser entre 2 y 8"
                    }
                    else -> {
                        errorMensaje = ""
                        viewModel.configurarJuego(limite)
                        viewModel.prepararRegistro(cantidad)
                        onNavegar()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("Iniciar registro de jugadores", fontSize = 16.sp)
        }
    }
}