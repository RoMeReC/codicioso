package com.eyzaguirre.codicioso.model

data class Turno(
    val jugadorId: Int,
    var puntajeAcumulado: Int = 0,
    var dadosDisponibles: Int = 10,
    var cantidadSobres: Int = 0,
    var perdido: Boolean = false,
    var finalizado: Boolean = false
)