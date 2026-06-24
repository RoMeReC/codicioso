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
    val estado: StateFlow<EstadoJuego> = _estado.asStateFlow()

    // ── CONFIGURACIÓN ──────────────────────────────────────────

    fun configurarJuego(puntajeLimit: Int) {
        _estado.value = _estado.value.copy(
            puntajelimite = puntajeLimit
        )
    }

    // ── REGISTRO DE JUGADORES ──────────────────────────────────

    fun registrarDadoInicial(nombre: String, valorDado: Int) {
        val jugadoresActuales = _estado.value.jugadores.toMutableList()
        val nuevoJugador = Jugador(
            id = jugadoresActuales.size + 1,
            nombre = nombre,
            dadoInicial = valorDado
        )
        jugadoresActuales.add(nuevoJugador)
        _estado.value = _estado.value.copy(jugadores = jugadoresActuales)
    }

    fun ordenarJugadores() {
        val jugadoresOrdenados = _estado.value.jugadores
            .sortedByDescending { it.dadoInicial }
        _estado.value = _estado.value.copy(
            jugadores = jugadoresOrdenados,
            turnoActual = 0
        )
    }

    // ── TURNO ──────────────────────────────────────────────────

    fun iniciarTurno() {
        val jugadorActual = jugadorEnTurno() ?: return
        _estado.value = _estado.value.copy(
            turnoEnCurso = Turno(jugadorId = jugadorActual.id)
        )
    }

    fun registrarSeleccion(seleccion: SeleccionDados) {
        val turno = _estado.value.turnoEnCurso ?: return
        val resultado = procesarLanzamiento(seleccion, turno.dadosDisponibles)

        if (!resultado.esValido) {
            // Turno perdido
            _estado.value = _estado.value.copy(
                turnoEnCurso = turno.copy(
                    perdido = true,
                    puntajeAcumulado = 0,
                    finalizado = true
                )
            )
            return
        }

        val nuevoAcumulado = turno.puntajeAcumulado + resultado.puntos

        if (resultado.esSobre) {
            // SOBRE: reinicia dados, suma puntos, incrementa contador
            _estado.value = _estado.value.copy(
                turnoEnCurso = turno.copy(
                    puntajeAcumulado = nuevoAcumulado,
                    dadosDisponibles = 10,
                    cantidadSobres = turno.cantidadSobres + 1
                )
            )
        } else {
            // Lanzamiento normal: reduce dados disponibles
            val dadosRestantes = turno.dadosDisponibles - resultado.dadosUsados
            _estado.value = _estado.value.copy(
                turnoEnCurso = turno.copy(
                    puntajeAcumulado = nuevoAcumulado,
                    dadosDisponibles = dadosRestantes
                )
            )
        }
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
        val alcanzoLimite = jugadores.find { it.puntajeTotal >= limite }

        var ultimaRonda = _estado.value.ultimaRonda
        var jugadorQueAlcanza = _estado.value.jugadorQueAlcanzaLimite

        if (alcanzoLimite != null && !ultimaRonda) {
            ultimaRonda = true
            jugadorQueAlcanza = alcanzoLimite.id
        }

        // Pasar al siguiente jugador
        val totalJugadores = jugadores.size
        val siguienteTurno = (_estado.value.turnoActual + 1) % totalJugadores
        var nuevaRonda = _estado.value.rondaActual
        var juegoFinalizado = false

        if (siguienteTurno == 0) {
            nuevaRonda++
            if (ultimaRonda) {
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
}