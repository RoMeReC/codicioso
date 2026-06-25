package com.eyzaguirre.codicioso.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
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
import com.eyzaguirre.codicioso.ui.BotonPrincipal
import com.eyzaguirre.codicioso.ui.TituloPantalla
import com.eyzaguirre.codicioso.ui.theme.*

@Composable
fun PantallaConfiguracion(
    viewModel: JuegoViewModel,
    onNavegar: () -> Unit
) {
    var puntajeLimiteTexto by remember { mutableStateOf("10000") }
    var cantidadJugadoresTexto by remember { mutableStateOf("2") }
    var modoUltimaRonda by remember { mutableStateOf(true) }
    var puntajesVisibles by remember { mutableStateOf(true) }
    var errorMensaje by remember { mutableStateOf("") }
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { visible = true }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoOscuro)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TituloPantalla(
                emoji = "🎲",
                titulo = "CODICIOSO",
                subtitulo = "El juego de los dados"
            )

            Spacer(modifier = Modifier.height(40.dp))

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(800, delayMillis = 300)) +
                        slideInVertically(
                            initialOffsetY = { 60 },
                            animationSpec = tween(800, delayMillis = 300)
                        )
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    // Campo puntaje límite
                    OutlinedTextField(
                        value = puntajeLimiteTexto,
                        onValueChange = { puntajeLimiteTexto = it },
                        label = {
                            Text("Puntaje límite", color = TextoGrisClaro)
                        },
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
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo cantidad de jugadores
                    OutlinedTextField(
                        value = cantidadJugadoresTexto,
                        onValueChange = { cantidadJugadoresTexto = it },
                        label = {
                            Text(
                                "Cantidad de jugadores (2-8)",
                                color = TextoGrisClaro
                            )
                        },
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
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Opción: Modo fin de juego
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(FondoTarjeta)
                            .padding(16.dp)
                    ) {
                        Column {
                            Text(
                                text = "🏁 Modo de fin de juego",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = AmarilloFiesta
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Opción Última Ronda
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            if (modoUltimaRonda)
                                                Brush.linearGradient(
                                                    listOf(AmarilloFiesta, NaranjaAlerta)
                                                )
                                            else
                                                Brush.linearGradient(
                                                    listOf(FondoSuperficie, FondoSuperficie)
                                                )
                                        )
                                ) {
                                    TextButton(
                                        onClick = { modoUltimaRonda = true },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = "⏳",
                                                fontSize = 24.sp
                                            )
                                            Text(
                                                text = "Última\nRonda",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (modoUltimaRonda)
                                                    FondoOscuro else TextoGrisClaro,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                                // Opción Ganador Directo
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            if (!modoUltimaRonda)
                                                Brush.linearGradient(
                                                    listOf(AmarilloFiesta, NaranjaAlerta)
                                                )
                                            else
                                                Brush.linearGradient(
                                                    listOf(FondoSuperficie, FondoSuperficie)
                                                )
                                        )
                                ) {
                                    TextButton(
                                        onClick = { modoUltimaRonda = false },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = "🏆",
                                                fontSize = 24.sp
                                            )
                                            Text(
                                                text = "Ganador\nDirecto",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (!modoUltimaRonda)
                                                    FondoOscuro else TextoGrisClaro,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (modoUltimaRonda)
                                    "Al alcanzar el límite, todos juegan una ronda más."
                                else
                                    "El primero en alcanzar el límite gana inmediatamente.",
                                fontSize = 13.sp,
                                color = TextoGrisClaro,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Opción: Visibilidad de puntajes
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(FondoTarjeta)
                            .padding(16.dp)
                    ) {
                        Column {
                            Text(
                                text = "👁 Visibilidad de puntajes",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = AmarilloFiesta
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Opción Visibles
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            if (puntajesVisibles)
                                                Brush.linearGradient(
                                                    listOf(AzulDado, VerdeExito)
                                                )
                                            else
                                                Brush.linearGradient(
                                                    listOf(FondoSuperficie, FondoSuperficie)
                                                )
                                        )
                                ) {
                                    TextButton(
                                        onClick = { puntajesVisibles = true },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text("👀", fontSize = 24.sp)
                                            Text(
                                                text = "Puntajes\nVisibles",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (puntajesVisibles)
                                                    TextoBlanco else TextoGrisClaro,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                                // Opción Ocultos
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            if (!puntajesVisibles)
                                                Brush.linearGradient(
                                                    listOf(AzulDado, VerdeExito)
                                                )
                                            else
                                                Brush.linearGradient(
                                                    listOf(FondoSuperficie, FondoSuperficie)
                                                )
                                        )
                                ) {
                                    TextButton(
                                        onClick = { puntajesVisibles = false },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text("🙈", fontSize = 24.sp)
                                            Text(
                                                text = "Puntajes\nOcultos",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (!puntajesVisibles)
                                                    TextoBlanco else TextoGrisClaro,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (puntajesVisibles)
                                    "Todos pueden ver el puntaje acumulado de cada jugador."
                                else
                                    "Los puntajes se revelan solo al final del juego.",
                                fontSize = 13.sp,
                                color = TextoGrisClaro,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (errorMensaje.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(NaranjaAlerta.copy(alpha = 0.2f))
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = errorMensaje,
                                color = NaranjaAlerta,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    BotonPrincipal(
                        texto = "🎯 Iniciar registro de jugadores",
                        onClick = {
                            val limite = puntajeLimiteTexto.toIntOrNull()
                            val cantidad = cantidadJugadoresTexto.toIntOrNull()
                            when {
                                limite == null || limite < 1000 -> {
                                    errorMensaje =
                                        "El puntaje límite debe ser al menos 1000"
                                }
                                cantidad == null || cantidad < 2 || cantidad > 8 -> {
                                    errorMensaje =
                                        "La cantidad de jugadores debe ser entre 2 y 8"
                                }
                                else -> {
                                    errorMensaje = ""
                                    viewModel.configurarJuego(
                                        limite,
                                        modoUltimaRonda,
                                        puntajesVisibles
                                    )
                                    viewModel.prepararRegistro(cantidad)
                                    onNavegar()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}