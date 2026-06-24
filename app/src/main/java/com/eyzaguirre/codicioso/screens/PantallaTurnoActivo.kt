package com.eyzaguirre.codicioso.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eyzaguirre.codicioso.JuegoViewModel
import com.eyzaguirre.codicioso.logic.SeleccionDados

@Composable
fun PantallaTurnoActivo(
    viewModel: JuegoViewModel,
    onTurnoTerminado: () -> Unit,
    onUltimaRonda: () -> Unit
) {
    val estado by viewModel.estado.collectAsState()
    val turno = estado.turnoEnCurso
    val jugador = viewModel.jugadorEnTurno()

    // Estado local de la selección actual
    var tricasSeleccionadas by remember { mutableStateOf(listOf<Int>()) }
    var unosSueltos by remember { mutableStateOf(0) }
    var cincosSueltos by remember { mutableStateOf(0) }
    var errorMensaje by remember { mutableStateOf("") }
    var mostrarConfirmacionPlantarse by remember { mutableStateOf(false) }

    // Cuando el turno termina, navegar
    LaunchedEffect(estado.turnoEnCurso) {
        if (estado.turnoEnCurso == null) {
            if (estado.ultimaRonda) {
                onUltimaRonda()
            } else {
                onTurnoTerminado()
            }
        }
    }

    if (turno == null || jugador == null) return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // Encabezado: jugador actual
        Text(
            text = jugador.nombre,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Turno activo",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Indicador de SOBREs
        if (turno.cantidadSobres > 0) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiary
                )
            ) {
                Text(
                    text = "🔥 SOBRE x${turno.cantidadSobres}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    color = MaterialTheme.colorScheme.onTertiary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Puntaje acumulado del turno
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Puntaje acumulado en turno",
                    fontSize = 14.sp
                )
                Text(
                    text = "${turno.puntajeAcumulado} pts",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Dados disponibles: ${turno.dadosDisponibles}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Sección: separar tricas
        Text(
            text = "¿Qué tricas salieron?",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Toca los valores de las tricas que separas:",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Botones para agregar tricas (valores 1-6)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items((1..6).toList()) { valor ->
                val yaAgregada = tricasSeleccionadas.contains(valor)
                OutlinedButton(
                    onClick = {
                        tricasSeleccionadas = if (yaAgregada) {
                            tricasSeleccionadas - valor
                        } else {
                            tricasSeleccionadas + valor
                        }
                    },
                    colors = if (yaAgregada) ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ) else ButtonDefaults.outlinedButtonColors()
                ) {
                    Text("Trica $valor")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Unos y cincos sueltos
        Text(
            text = "Unos y cincos sueltos:",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Contador de unos sueltos
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Unos sueltos", fontSize = 13.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedButton(
                        onClick = { if (unosSueltos > 0) unosSueltos-- },
                        modifier = Modifier.size(40.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) { Text("-") }
                    Text(
                        text = "$unosSueltos",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                    OutlinedButton(
                        onClick = { unosSueltos++ },
                        modifier = Modifier.size(40.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) { Text("+") }
                }
            }

            // Contador de cincos sueltos
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Cincos sueltos", fontSize = 13.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedButton(
                        onClick = { if (cincosSueltos > 0) cincosSueltos-- },
                        modifier = Modifier.size(40.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) { Text("-") }
                    Text(
                        text = "$cincosSueltos",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                    OutlinedButton(
                        onClick = { if (cincosSueltos > 0) cincosSueltos-- },
                        modifier = Modifier.size(40.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) { Text("+") }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (errorMensaje.isNotEmpty()) {
            Text(
                text = errorMensaje,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón: registrar lanzamiento
        Button(
            onClick = {
                val seleccion = SeleccionDados(
                    tricas = tricasSeleccionadas,
                    unosSueltos = unosSueltos,
                    cincosSueltos = cincosSueltos
                )
                val dadosUsados = tricasSeleccionadas.size * 3 +
                        unosSueltos + cincosSueltos

                if (dadosUsados > turno.dadosDisponibles) {
                    errorMensaje =
                        "Estás usando más dados de los disponibles (${turno.dadosDisponibles})"
                } else {
                    errorMensaje = ""
                    viewModel.registrarSeleccion(seleccion)
                    // Resetear selección para el siguiente lanzamiento
                    tricasSeleccionadas = listOf()
                    unosSueltos = 0
                    cincosSueltos = 0
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("Registrar lanzamiento", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón: sin jugada válida (turno perdido)
        OutlinedButton(
            onClick = {
                viewModel.registrarSeleccion(SeleccionDados())
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text("Sin jugada válida (0 pts)", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón: plantarse
        if (turno.puntajeAcumulado > 0) {
            OutlinedButton(
                onClick = { mostrarConfirmacionPlantarse = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    "Plantarse (guardar ${turno.puntajeAcumulado} pts)",
                    fontSize = 16.sp
                )
            }
        }
    }

    // Diálogo de confirmación para plantarse
    if (mostrarConfirmacionPlantarse) {
        AlertDialog(
            onDismissRequest = { mostrarConfirmacionPlantarse = false },
            title = { Text("¿Plantarse?") },
            text = {
                Text(
                    "¿Confirmas guardar ${turno.puntajeAcumulado} pts " +
                            "y pasar el turno al siguiente jugador?"
                )
            },
            confirmButton = {
                Button(onClick = {
                    mostrarConfirmacionPlantarse = false
                    viewModel.plantarse()
                }) {
                    Text("Sí, plantarse")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = {
                    mostrarConfirmacionPlantarse = false
                }) {
                    Text("Seguir jugando")
                }
            }
        )
    }
}