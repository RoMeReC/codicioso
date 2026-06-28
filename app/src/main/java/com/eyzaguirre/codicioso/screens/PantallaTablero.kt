package com.eyzaguirre.codicioso.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eyzaguirre.codicioso.JuegoViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.sp
import com.eyzaguirre.codicioso.ui.theme.AmarilloFiesta
import com.eyzaguirre.codicioso.ui.theme.FondoTarjeta
import com.eyzaguirre.codicioso.ui.theme.NaranjaAlerta
import com.eyzaguirre.codicioso.ui.theme.RojoCodicioso
import com.eyzaguirre.codicioso.ui.theme.RojoOscuro
import com.eyzaguirre.codicioso.ui.theme.TextoBlanco
import com.eyzaguirre.codicioso.ui.theme.TextoGrisClaro
import com.eyzaguirre.codicioso.ui.theme.VerdeExito

@Composable
fun PantallaTablero(
    viewModel: JuegoViewModel,
    onIniciarTurno: () -> Unit,
    onJuegoFinalizado: () -> Unit
) {
    val estado by viewModel.estado.collectAsState()
    val jugadores = estado.jugadores
    val jugadorActual = viewModel.jugadorEnTurno()
    var mostrarConfirmacionFinalizar by remember { mutableStateOf(false) }


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
            itemsIndexed(jugadores) { indice, jugador ->
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
                                fontSize = 18.sp,
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
                            text = if (estado.puntajesVisibles)
                                "${jugador.puntajeTotal} pts"
                            else
                                "🙈 ???",
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

        // Botón iniciar turno (ya existente)
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

        Spacer(modifier = Modifier.height(8.dp))

        // Botón finalizar juego anticipado
        OutlinedButton(
            onClick = { mostrarConfirmacionFinalizar = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            ),
            border = androidx.compose.foundation.BorderStroke(
                2.dp, MaterialTheme.colorScheme.error
            )
        ) {
            Text(
                text = "⛔ Finalizar juego ahora",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
    if (mostrarConfirmacionFinalizar) {
        AlertDialog(
            onDismissRequest = { mostrarConfirmacionFinalizar = false },
            containerColor = FondoTarjeta,
            title = {
                Text(
                    text = "⛔ ¿Finalizar el juego?",
                    color = NaranjaAlerta,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
            text = {
                Column {
                    Text(
                        text = "Se mostrarán los puntajes actuales y el juego terminará.",
                        color = TextoBlanco,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Puntajes actuales:",
                        color = AmarilloFiesta,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    estado.jugadores
                        .sortedByDescending { it.puntajeTotal }
                        .forEachIndexed { indice, jugador ->
                            val medallas = listOf("🥇", "🥈", "🥉")
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${medallas.getOrElse(indice) {
                                        "${indice + 1}."
                                    }} ${jugador.nombre}",
                                    color = TextoBlanco,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = "${jugador.puntajeTotal} pts",
                                    color = AmarilloFiesta,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }
                        }
                }
            },
            confirmButton = {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(RojoCodicioso, RojoOscuro)
                            )
                        )
                ) {
                    TextButton(
                        onClick = {
                            mostrarConfirmacionFinalizar = false
                            viewModel.finalizarJuegoAhora()
                            onJuegoFinalizado()
                        }
                    ) {
                        Text(
                            "⛔ Sí, finalizar",
                            color = TextoBlanco,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { mostrarConfirmacionFinalizar = false },
                    border = androidx.compose.foundation.BorderStroke(
                        2.dp, VerdeExito
                    )
                ) {
                    Text(
                        "🎲 Seguir jugando",
                        color = VerdeExito,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        )
    }
}