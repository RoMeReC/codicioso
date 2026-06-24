package com.eyzaguirre.codicioso.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eyzaguirre.codicioso.JuegoViewModel

@Composable
fun PantallaTablero(
    viewModel: JuegoViewModel,
    onIniciarTurno: () -> Unit,
    onJuegoFinalizado: () -> Unit
) {
    val estado by viewModel.estado.collectAsState()
    val jugadores = estado.jugadores
    val jugadorActual = viewModel.jugadorEnTurno()

    LaunchedEffect(estado.juegoFinalizado) {
        if (estado.juegoFinalizado) onJuegoFinalizado()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "🎲 CODICIOSO",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Ronda ${estado.rondaActual}",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Alerta de última ronda
        if (estado.ultimaRonda) {
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "⚠️ ¡ÚLTIMA RONDA! Alguien alcanzó el límite.",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(12.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tabla de puntajes
        Text(
            text = "Puntajes",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(jugadores) { jugador ->
                val esElTurno = jugador.id == jugadorActual?.id
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (esElTurno)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = jugador.nombre,
                                fontSize = 16.sp,
                                fontWeight = if (esElTurno)
                                    FontWeight.Bold else FontWeight.Normal
                            )
                            if (esElTurno) {
                                Text(
                                    text = "← turno actual",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                        Text(
                            text = "${jugador.puntajeTotal} pts",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (esElTurno)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Límite de puntos
        Text(
            text = "Límite: ${estado.puntajelimite} pts",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón iniciar turno
        if (jugadorActual != null) {
            Button(
                onClick = {
                    viewModel.iniciarTurno()
                    onIniciarTurno()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    "Iniciar turno de ${jugadorActual.nombre}",
                    fontSize = 16.sp
                )
            }
        }
    }
}