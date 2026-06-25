package com.eyzaguirre.codicioso.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eyzaguirre.codicioso.JuegoViewModel
import com.eyzaguirre.codicioso.ui.theme.*

@Composable
fun PantallaTurnoActivo(
    viewModel: JuegoViewModel,
    onTurnoTerminado: () -> Unit,
    onUltimaRonda: () -> Unit
) {
    val estado by viewModel.estado.collectAsState()
    val turno = estado.turnoEnCurso
    val jugador = viewModel.jugadorEnTurno()

    var puntajeTexto by remember { mutableStateOf("") }
    var errorMensaje by remember { mutableStateOf("") }
    var mostrarConfirmacionPlantarse by remember { mutableStateOf(false) }

    LaunchedEffect(estado.turnoEnCurso) {
        if (estado.turnoEnCurso == null) {
            if (estado.ultimaRonda) onUltimaRonda()
            else onTurnoTerminado()
        }
    }

    if (turno == null || jugador == null) return

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoOscuro)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Nombre del jugador
            Text(
                text = jugador.nombre,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                color = AmarilloFiesta,
                textAlign = TextAlign.Center
            )

            Text(
                text = "turno activo",
                fontSize = 15.sp,
                color = TextoGrisClaro
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Badge SOBRE
            AnimatedVisibility(visible = turno.cantidadSobres > 0) {
                Column {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(MoradoSobre, NaranjaAlerta)
                                )
                            )
                            .padding(horizontal = 24.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = "🔥 SOBRE x${turno.cantidadSobres}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = TextoBlanco
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            // Puntaje acumulado
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(FondoTarjeta, FondoSuperficie)
                        )
                    )
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Puntaje acumulado",
                        fontSize = 14.sp,
                        color = TextoGrisClaro
                    )
                    AnimatedContent(
                        targetState = turno.puntajeAcumulado,
                        transitionSpec = {
                            slideInVertically { -it } + fadeIn() togetherWith
                                    slideOutVertically { it } + fadeOut()
                        },
                        label = "puntaje"
                    ) { puntaje ->
                        Text(
                            text = "$puntaje pts",
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Black,
                            color = AmarilloFiesta
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Campo de ingreso de puntaje
            Text(
                text = "¿Cuántos puntos juntó este lanzamiento?",
                fontSize = 16.sp,
                color = TextoGrisClaro,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = puntajeTexto,
                onValueChange = {
                    puntajeTexto = it
                    errorMensaje = ""
                },
                label = { Text("Puntaje del lanzamiento", color = TextoGrisClaro) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AmarilloFiesta,
                    unfocusedBorderColor = TextoGrisClaro,
                    focusedTextColor = TextoBlanco,
                    unfocusedTextColor = TextoBlanco,
                    cursorColor = AmarilloFiesta
                ),
                shape = RoundedCornerShape(16.dp),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )

            if (errorMensaje.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = errorMensaje,
                    color = NaranjaAlerta,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón: Registrar lanzamiento (antes Plantarse)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(VerdeExito, AzulDado)
                        )
                    )
                    .height(56.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        val puntos = puntajeTexto.toIntOrNull()
                        when {
                            puntos == null || puntos < 0 -> {
                                errorMensaje = "Ingresa un puntaje válido"
                            }
                            puntos > 3100 -> {
                                errorMensaje = "El puntaje máximo por lanzamiento es 3100 pts"
                            }
                            else -> {
                                viewModel.registrarPuntajeLanzamiento(puntos)
                                mostrarConfirmacionPlantarse = true
                                puntajeTexto = ""
                            }
                        }
                    },
                    modifier = Modifier.fillMaxSize(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = androidx.compose.ui.graphics.Color.Transparent
                    ),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {
                    Text(
                        text = "✅ Registrar lanzamiento",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextoBlanco
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botón: SOBRE
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(MoradoSobre, NaranjaAlerta)
                        )
                    )
                    .height(56.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        val puntos = puntajeTexto.toIntOrNull()
                        if (puntos != null && puntos > 0) {
                            if (puntos > 3100) {
                                errorMensaje = "El puntaje máximo por lanzamiento es 3100 pts"
                                return@Button
                            }
                            viewModel.registrarPuntajeLanzamiento(puntos)
                            puntajeTexto = ""
                        }
                        viewModel.registrarSobre()
                    },
                    modifier = Modifier.fillMaxSize(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = androidx.compose.ui.graphics.Color.Transparent
                    ),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {
                    Text(
                        text = "🔥 SOBRE",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextoBlanco
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botón: Sin jugada válida
            OutlinedButton(
                onClick = { viewModel.registrarTurnoPerdido() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = NaranjaAlerta
                ),
                border = androidx.compose.foundation.BorderStroke(
                    2.dp, NaranjaAlerta
                )
            ) {
                Text(
                    text = "❌ Sin jugada válida (0 pts)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }

    // Diálogo: ¿seguir o plantarse?
    if (mostrarConfirmacionPlantarse) {
        AlertDialog(
            onDismissRequest = { mostrarConfirmacionPlantarse = false },
            containerColor = FondoTarjeta,
            title = {
                Text(
                    text = "¿Qué hace ${jugador.nombre}?",
                    color = AmarilloFiesta,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
            text = {
                Text(
                    text = "Tiene ${turno.puntajeAcumulado} pts acumulados.",
                    color = TextoBlanco,
                    fontSize = 16.sp
                )
            },
            confirmButton = {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(AmarilloFiesta, NaranjaAlerta)
                            )
                        )
                ) {
                    TextButton(onClick = {
                        mostrarConfirmacionPlantarse = false
                        viewModel.plantarse()
                    }) {
                        Text(
                            "🛑 Plantarse",
                            color = FondoOscuro,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { mostrarConfirmacionPlantarse = false },
                    border = androidx.compose.foundation.BorderStroke(
                        2.dp, VerdeExito
                    )
                ) {
                    Text(
                        "🎲 Seguir lanzando",
                        color = VerdeExito,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        )
    }
}