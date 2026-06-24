package com.eyzaguirre.codicioso.model

data class EstadoJuego(
    var jugadores: List<Jugador> = emptyList(),
    var rondaActual: Int = 1,
    var turnoActual: Int = 0,
    var puntajelimite: Int = 10000,
    var ultimaRonda: Boolean = false,
    var jugadorQueAlcanzaLimite: Int? = null,
    var juegoFinalizado: Boolean = false,
    var turnoEnCurso: Turno? = null
)