package com.eyzaguirre.codicioso

import com.eyzaguirre.codicioso.logic.SeleccionDados
import com.eyzaguirre.codicioso.logic.calcularPuntos
import com.eyzaguirre.codicioso.logic.calcularDadosUsados
import com.eyzaguirre.codicioso.logic.detectarSobre
import com.eyzaguirre.codicioso.logic.procesarLanzamiento
import org.junit.Test
import org.junit.Assert.*

class LogicaJuegoTest {

    // ── PRUEBAS DE PUNTOS ──────────────────────────────────────

    @Test
    fun `trica de uno vale 1000`() {
        val seleccion = SeleccionDados(tricas = listOf(1))
        assertEquals(1000, calcularPuntos(seleccion))
    }

    @Test
    fun `trica de cinco vale 500`() {
        val seleccion = SeleccionDados(tricas = listOf(5))
        assertEquals(500, calcularPuntos(seleccion))
    }

    @Test
    fun `trica de seis vale 600`() {
        val seleccion = SeleccionDados(tricas = listOf(6))
        assertEquals(600, calcularPuntos(seleccion))
    }

    @Test
    fun `trica de cuatro vale 400`() {
        val seleccion = SeleccionDados(tricas = listOf(4))
        assertEquals(400, calcularPuntos(seleccion))
    }

    @Test
    fun `un uno suelto vale 100`() {
        val seleccion = SeleccionDados(unosSueltos = 1)
        assertEquals(100, calcularPuntos(seleccion))
    }

    @Test
    fun `dos unos sueltos valen 200`() {
        val seleccion = SeleccionDados(unosSueltos = 2)
        assertEquals(200, calcularPuntos(seleccion))
    }

    @Test
    fun `un cinco suelto vale 50`() {
        val seleccion = SeleccionDados(cincosSueltos = 1)
        assertEquals(50, calcularPuntos(seleccion))
    }

    @Test
    fun `combinacion trica de uno y dos cincos sueltos vale 1100`() {
        val seleccion = SeleccionDados(
            tricas = listOf(1),
            cincosSueltos = 2
        )
        assertEquals(1100, calcularPuntos(seleccion))
    }

    @Test
    fun `combinacion del ejemplo del juego vale 1400`() {
        // trica de 3 (300) + trica de 1 (1000) + dos cincos sueltos (100)
        val seleccion = SeleccionDados(
            tricas = listOf(3, 1),
            cincosSueltos = 2
        )
        assertEquals(1400, calcularPuntos(seleccion))
    }

    // ── PRUEBAS DE DADOS USADOS ────────────────────────────────

    @Test
    fun `una trica usa 3 dados`() {
        val seleccion = SeleccionDados(tricas = listOf(2))
        assertEquals(3, calcularDadosUsados(seleccion))
    }

    @Test
    fun `dos tricas usan 6 dados`() {
        val seleccion = SeleccionDados(tricas = listOf(2, 4))
        assertEquals(6, calcularDadosUsados(seleccion))
    }

    @Test
    fun `trica mas dos sueltos usa 5 dados`() {
        val seleccion = SeleccionDados(
            tricas = listOf(3),
            unosSueltos = 1,
            cincosSueltos = 1
        )
        assertEquals(5, calcularDadosUsados(seleccion))
    }

    // ── PRUEBAS DE SOBRE ───────────────────────────────────────

    @Test
    fun `usar todos los dados disponibles es sobre`() {
        assertTrue(detectarSobre(dadosDisponibles = 5, dadosUsadosEnSeleccion = 5))
    }

    @Test
    fun `no usar todos los dados no es sobre`() {
        assertFalse(detectarSobre(dadosDisponibles = 10, dadosUsadosEnSeleccion = 6))
    }

    @Test
    fun `sobre con 10 dados de una sola vez`() {
        // 3 tricas (9 dados) + 1 uno suelto (1 dado) = 10 dados = SOBRE
        val seleccion = SeleccionDados(
            tricas = listOf(2, 4, 6),
            unosSueltos = 1
        )
        val resultado = procesarLanzamiento(seleccion, dadosDisponibles = 10)
        assertTrue(resultado.esSobre)
    }

    // ── PRUEBAS DE TURNO PERDIDO ───────────────────────────────

    @Test
    fun `seleccion vacia es turno perdido`() {
        val seleccion = SeleccionDados()
        val resultado = procesarLanzamiento(seleccion, dadosDisponibles = 10)
        assertFalse(resultado.esValido)
        assertEquals(0, resultado.puntos)
    }
}