package com.eyzaguirre.codicioso.ui.screens

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.eyzaguirre.codicioso.JuegoViewModel

@Composable
fun PantallaRegistroJugadores(
    viewModel: JuegoViewModel,
    onNavegar: () -> Unit
) {
    val estado by viewModel.estado.collectAsState()
    var nombreTexto by remember { mutableStateOf("") }
    var errorMensaje by remember { mutableStateOf("") }

    val jugadoresRegistrados = estado.jugadores
    val totalEsperados = viewModel.cantidadJugadoresEsperados
    val todosRegistrados = jugadoresRegistrados.size == totalEsperados

    // Estado para drag & drop
    var draggingIndex by remember { mutableStateOf(-1) }
    var targetIndex by remember { mutableStateOf(-1) }

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

        if (!todosRegistrados) {
            Text(
                text = "Jugador ${jugadoresRegistrados.size + 1} de $totalEsperados",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            Text(
                text = "Mantén presionado el ícono ≡ y arrastra para cambiar el orden",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (!todosRegistrados) {
            OutlinedTextField(
                value = nombreTexto,
                onValueChange = { nombreTexto = it },
                label = { Text("Nombre del jugador") },
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

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    when {
                        nombreTexto.isBlank() -> {
                            errorMensaje = "Ingresa el nombre del jugador"
                        }
                        jugadoresRegistrados.any {
                            it.nombre.equals(nombreTexto.trim(), ignoreCase = true)
                        } -> {
                            errorMensaje = "Ya existe un jugador con ese nombre"
                        }
                        else -> {
                            errorMensaje = ""
                            viewModel.agregarJugador(nombreTexto.trim())
                            nombreTexto = ""
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Agregar jugador", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        if (jugadoresRegistrados.isNotEmpty()) {
            Text(
                text = "Orden de juego:",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                itemsIndexed(
                    jugadoresRegistrados,
                    key = { _, jugador -> jugador.id }
                ) { indice, jugador ->
                    val isDragging = draggingIndex == indice
                    val isTarget = targetIndex == indice

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .zIndex(if (isDragging) 1f else 0f)
                            .graphicsLayer {
                                alpha = if (isDragging) 0.7f else 1f
                                scaleX = if (isDragging) 1.03f else 1f
                                scaleY = if (isDragging) 1.03f else 1f
                            },
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = if (isDragging) 8.dp else 2.dp
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = when {
                                isDragging -> MaterialTheme.colorScheme.primaryContainer
                                isTarget -> MaterialTheme.colorScheme.secondaryContainer
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            }
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DragHandle,
                                    contentDescription = "Arrastrar",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.pointerInput(indice) {
                                        detectDragGesturesAfterLongPress(
                                            onDragStart = {
                                                draggingIndex = indice
                                                targetIndex = indice
                                            },
                                            onDrag = { change, dragAmount ->
                                                change.consume()
                                                val itemHeightPx = 80.dp.toPx()
                                                val newTarget = (targetIndex +
                                                        (dragAmount.y / itemHeightPx)
                                                            .toInt())
                                                    .coerceIn(
                                                        0,
                                                        jugadoresRegistrados.size - 1
                                                    )
                                                if (newTarget != targetIndex) {
                                                    targetIndex = newTarget
                                                }
                                            },
                                            onDragEnd = {
                                                if (draggingIndex != -1 &&
                                                    targetIndex != -1 &&
                                                    draggingIndex != targetIndex
                                                ) {
                                                    viewModel.moverJugador(
                                                        draggingIndex,
                                                        targetIndex
                                                    )
                                                }
                                                draggingIndex = -1
                                                targetIndex = -1
                                            },
                                            onDragCancel = {
                                                draggingIndex = -1
                                                targetIndex = -1
                                            }
                                        )
                                    }
                                )
                                Text(
                                    text = "${indice + 1}. ${jugador.nombre}",
                                    fontSize = 16.sp,
                                    fontWeight = if (isDragging)
                                        FontWeight.Bold else FontWeight.Normal
                                )
                            }
                            Text(
                                text = if (indice == 0) "Inicia" else "Sigue",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
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

            Button(
                onClick = { onNavegar() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Comenzar juego", fontSize = 16.sp)
            }
        }
    }
}