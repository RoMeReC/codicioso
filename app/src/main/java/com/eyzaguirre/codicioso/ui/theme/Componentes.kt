package com.eyzaguirre.codicioso.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eyzaguirre.codicioso.ui.theme.*

// Botón principal con gradiente amarillo
@Composable
fun BotonPrincipal(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    habilitado: Boolean = true
) {
    val escala by animateFloatAsState(
        targetValue = if (habilitado) 1f else 0.95f,
        label = "escala"
    )
    Box(
        modifier = modifier
            .scale(escala)
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (habilitado)
                    Brush.horizontalGradient(
                        colors = listOf(AmarilloFiesta, NaranjaAlerta)
                    )
                else
                    Brush.horizontalGradient(
                        colors = listOf(Color.Gray, Color.DarkGray)
                    )
            )
            .height(56.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onClick,
            enabled = habilitado,
            modifier = Modifier.fillMaxSize(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            ),
            elevation = ButtonDefaults.buttonElevation(0.dp)
        ) {
            Text(
                text = texto,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = FondoOscuro
            )
        }
    }
}

// Botón secundario con borde
@Composable
fun BotonSecundario(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = AmarilloFiesta
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(52.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = color
        ),
        border = androidx.compose.foundation.BorderStroke(2.dp, color)
    ) {
        Text(
            text = texto,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// Tarjeta con gradiente
@Composable
fun TarjetaGradiente(
    modifier: Modifier = Modifier,
    colores: List<Color> = listOf(FondoTarjeta, FondoSuperficie),
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.linearGradient(colors = colores))
            .padding(20.dp)
    ) {
        Column(content = content)
    }
}

// Título principal de pantalla con animación de entrada
@Composable
fun TituloPantalla(
    emoji: String,
    titulo: String,
    subtitulo: String = ""
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(600)) + slideInVertically(
            initialOffsetY = { -40 },
            animationSpec = tween(600)
        )
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = emoji,
                fontSize = 56.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = titulo,
                style = MaterialTheme.typography.displayMedium,
                color = AmarilloFiesta,
                textAlign = TextAlign.Center
            )
            if (subtitulo.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitulo,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoGrisClaro,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// Tarjeta de puntaje de jugador
@Composable
fun TarjetaJugador(
    nombre: String,
    puntaje: Int,
    posicion: Int,
    esActual: Boolean = false,
    modifier: Modifier = Modifier
) {
    val escala by animateFloatAsState(
        targetValue = if (esActual) 1.03f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "escalaJugador"
    )

    val colorFondo = when {
        esActual -> Brush.horizontalGradient(
            listOf(Color(0xFF7B6000), Color(0xFF4A3800))
        )
        else -> Brush.horizontalGradient(
            listOf(FondoTarjeta, FondoSuperficie)
        )
    }

    Box(
        modifier = modifier
            .scale(escala)
            .clip(RoundedCornerShape(16.dp))
            .background(colorFondo)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "${posicion + 1}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = if (esActual) AmarilloFiesta else TextoGrisClaro
                )
                Column {
                    Text(
                        text = nombre,
                        fontSize = 20.sp,
                        fontWeight = if (esActual) FontWeight.Bold else FontWeight.Normal,
                        color = TextoBlanco
                    )
                    if (esActual) {
                        Text(
                            text = "← jugando ahora",
                            fontSize = 13.sp,
                            color = AmarilloFiesta
                        )
                    }
                }
            }
            Text(
                text = "$puntaje",
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                color = if (esActual) AmarilloFiesta else TextoBlanco
            )
        }
    }
}

// Badge de SOBRE
@Composable
fun BadgeSobre(cantidad: Int) {
    val pulso by animateFloatAsState(
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulsoSobre"
    )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(MoradoSobre, Color(0xFFD500F9))
                )
            )
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Text(
            text = "🔥 SOBRE x$cantidad",
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            color = TextoBlanco
        )
    }
}