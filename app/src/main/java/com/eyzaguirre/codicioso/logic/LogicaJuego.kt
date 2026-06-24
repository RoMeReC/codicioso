package com.eyzaguirre.codicioso.logic

data class ResultadoLanzamiento(
    val puntos: Int,
    val dadosUsados: Int,
    val esSobre: Boolean,
    val esValido: Boolean
)

data class SeleccionDados(
    val tricas: List<Int> = emptyList(),
    val unosSueltos: Int = 0,
    val cincosSueltos: Int = 0
)

fun calcularPuntos(seleccion: SeleccionDados): Int {
    var puntos = 0

    // Puntos por tricas
    for (valor in seleccion.tricas) {
        puntos += when (valor) {
            1 -> 1000
            5 -> 500
            else -> valor * 100
        }
    }

    // Puntos por unos sueltos
    puntos += seleccion.unosSueltos * 100

    // Puntos por cincos sueltos
    puntos += seleccion.cincosSueltos * 50

    return puntos
}

fun calcularDadosUsados(seleccion: SeleccionDados): Int {
    val dadosDeTricas = seleccion.tricas.size * 3
    val dadosSueltos = seleccion.unosSueltos + seleccion.cincosSueltos
    return dadosDeTricas + dadosSueltos
}

fun esSeleccionValida(seleccion: SeleccionDados): Boolean {
    return seleccion.tricas.isNotEmpty() ||
            seleccion.unosSueltos > 0 ||
            seleccion.cincosSueltos > 0
}

fun detectarSobre(
    dadosDisponibles: Int,
    dadosUsadosEnSeleccion: Int
): Boolean {
    return dadosDisponibles == dadosUsadosEnSeleccion
}

fun procesarLanzamiento(
    seleccion: SeleccionDados,
    dadosDisponibles: Int
): ResultadoLanzamiento {
    val esValido = esSeleccionValida(seleccion)

    if (!esValido) {
        return ResultadoLanzamiento(
            puntos = 0,
            dadosUsados = 0,
            esSobre = false,
            esValido = false
        )
    }

    val puntos = calcularPuntos(seleccion)
    val dadosUsados = calcularDadosUsados(seleccion)
    val esSobre = detectarSobre(dadosDisponibles, dadosUsados)

    return ResultadoLanzamiento(
        puntos = puntos,
        dadosUsados = dadosUsados,
        esSobre = esSobre,
        esValido = true
    )
}