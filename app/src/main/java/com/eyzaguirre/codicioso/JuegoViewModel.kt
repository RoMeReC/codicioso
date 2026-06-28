package com.eyzaguirre.codicioso

import androidx.lifecycle.ViewModel
import com.eyzaguirre.codicioso.logic.SeleccionDados
import com.eyzaguirre.codicioso.logic.procesarLanzamiento
import com.eyzaguirre.codicioso.model.EstadoJuego
import com.eyzaguirre.codicioso.model.Jugador
import com.eyzaguirre.codicioso.model.Turno
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class JuegoViewModel : ViewModel() {

    private val _estado = MutableStateFlow(EstadoJuego())
    var cantidadJugadoresEsperados: Int = 2
        private set

    private var _cantidadJugadoresEsperados: Int = 2
        set(value) {
            field = value
            cantidadJugadoresEsperados = value
        }
    val estado: StateFlow<EstadoJuego> = _estado.asStateFlow()

    // ── CONFIGURACIÓN ──────────────────────────────────────────

    fun configurarJuego(
        puntajeLimit: Int,
        modoUltimaRonda: Boolean,
        puntajesVisibles: Boolean
    ) {
        _estado.value = _estado.value.copy(
            puntajelimite = puntajeLimit,
            modoUltimaRonda = modoUltimaRonda,
            puntajesVisibles = puntajesVisibles
        )
    }

    // ── REGISTRO DE JUGADORES ──────────────────────────────────
    fun prepararRegistro(cantidadJugadores: Int) {
        _estado.value = _estado.value.copy(
            jugadores = emptyList()
        )
        _cantidadJugadoresEsperados = cantidadJugadores
    }

    fun agregarJugador(nombre: String) {
        val jugadoresActuales = _estado.value.jugadores.toMutableList()
        val nuevoJugador = Jugador(
            id = jugadoresActuales.size + 1,
            nombre = nombre
        )
        jugadoresActuales.add(nuevoJugador)
        _estado.value = _estado.value.copy(jugadores = jugadoresActuales)
    }

    fun moverJugador(desde: Int, hasta: Int) {
        val lista = _estado.value.jugadores.toMutableList()
        lista.add(hasta, lista.removeAt(desde))
        _estado.value = _estado.value.copy(jugadores = lista)
    }

    // ── TURNO ──────────────────────────────────────────────────

    fun iniciarTurno() {
        val jugadorActual = jugadorEnTurno() ?: return
        _estado.value = _estado.value.copy(
            turnoEnCurso = Turno(jugadorId = jugadorActual.id)
        )
    }

    fun registrarPuntajeLanzamiento(puntos: Int) {
        val turno = _estado.value.turnoEnCurso ?: return
        val nuevoAcumulado = turno.puntajeAcumulado + puntos
        _estado.value = _estado.value.copy(
            turnoEnCurso = turno.copy(
                puntajeAcumulado = nuevoAcumulado
            )
        )
    }

    fun registrarSobre() {
        val turno = _estado.value.turnoEnCurso ?: return
        _estado.value = _estado.value.copy(
            turnoEnCurso = turno.copy(
                dadosDisponibles = 10,
                cantidadSobres = turno.cantidadSobres + 1
            )
        )
    }

    fun registrarTurnoPerdido() {
        val turno = _estado.value.turnoEnCurso ?: return
        _estado.value = _estado.value.copy(
            turnoEnCurso = turno.copy(
                perdido = true,
                puntajeAcumulado = 0,
                finalizado = true
            )
        )
        cerrarTurno()
    }

    fun plantarse() {
        val turno = _estado.value.turnoEnCurso ?: return
        _estado.value = _estado.value.copy(
            turnoEnCurso = turno.copy(finalizado = true)
        )
        cerrarTurno()
    }

    fun cerrarTurno() {
        val turno = _estado.value.turnoEnCurso ?: return
        val jugadores = _estado.value.jugadores.toMutableList()
        val indice = jugadores.indexOfFirst { it.id == turno.jugadorId }

        if (indice != -1 && !turno.perdido) {
            jugadores[indice] = jugadores[indice].copy(
                puntajeTotal = jugadores[indice].puntajeTotal + turno.puntajeAcumulado
            )
        }

        // Verificar si alguien alcanzó el límite
        val limite = _estado.value.puntajelimite
        val modoUltimaRonda = _estado.value.modoUltimaRonda
        val alcanzoLimite = jugadores.find { it.puntajeTotal >= limite }

        var ultimaRonda = _estado.value.ultimaRonda
        var jugadorQueAlcanza = _estado.value.jugadorQueAlcanzaLimite
        var juegoFinalizado = false

        if (alcanzoLimite != null && !ultimaRonda) {
            if (modoUltimaRonda) {
                ultimaRonda = true
                jugadorQueAlcanza = alcanzoLimite.id
            } else {
                juegoFinalizado = true
            }
        }

        // Pasar al siguiente jugador
        val totalJugadores = jugadores.size
        val siguienteTurno = (_estado.value.turnoActual + 1) % totalJugadores
        var nuevaRonda = _estado.value.rondaActual

        if (siguienteTurno == 0) {
            nuevaRonda++
            if (ultimaRonda && !juegoFinalizado) {
                juegoFinalizado = true
            }
        }

        _estado.value = _estado.value.copy(
            jugadores = jugadores,
            turnoActual = siguienteTurno,
            rondaActual = nuevaRonda,
            turnoEnCurso = null,
            ultimaRonda = ultimaRonda,
            jugadorQueAlcanzaLimite = jugadorQueAlcanza,
            juegoFinalizado = juegoFinalizado
        )
    }

    // ── HELPERS ────────────────────────────────────────────────

    fun jugadorEnTurno(): Jugador? {
        val estado = _estado.value
        return estado.jugadores.getOrNull(estado.turnoActual)
    }

    fun reiniciarJuego() {
        _estado.value = EstadoJuego()
    }

    fun finalizarJuegoAhora() {
        _estado.value = _estado.value.copy(
            juegoFinalizado = true,
            turnoEnCurso = null
        )
    }
}