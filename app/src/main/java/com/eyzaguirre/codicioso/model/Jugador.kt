package com.eyzaguirre.codicioso.model

data class Jugador(
    val id: Int,
    val nombre: String,
    var puntajeTotal: Int = 0
)