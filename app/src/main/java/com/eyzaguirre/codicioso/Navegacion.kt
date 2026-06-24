package com.eyzaguirre.codicioso

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eyzaguirre.codicioso.ui.screens.PantallaConfiguracion
import com.eyzaguirre.codicioso.ui.screens.PantallaRegistroJugadores
import com.eyzaguirre.codicioso.ui.screens.PantallaTablero
import com.eyzaguirre.codicioso.ui.screens.PantallaTurnoActivo
import com.eyzaguirre.codicioso.ui.screens.PantallaUltimaRonda
import com.eyzaguirre.codicioso.ui.screens.PantallaResultadoFinal

object Rutas {
    const val CONFIGURACION = "configuracion"
    const val REGISTRO_JUGADORES = "registro_jugadores"
    const val TABLERO = "tablero"
    const val TURNO_ACTIVO = "turno_activo"
    const val ULTIMA_RONDA = "ultima_ronda"
    const val RESULTADO_FINAL = "resultado_final"
}

@Composable
fun Navegacion(
    viewModel: JuegoViewModel,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Rutas.CONFIGURACION,
        modifier = modifier
    ) {
        composable(Rutas.CONFIGURACION) {
            PantallaConfiguracion(
                viewModel = viewModel,
                onNavegar = {
                    navController.navigate(Rutas.REGISTRO_JUGADORES)
                }
            )
        }

        composable(Rutas.REGISTRO_JUGADORES) {
            PantallaRegistroJugadores(
                viewModel = viewModel,
                onNavegar = {
                    navController.navigate(Rutas.TABLERO) {
                        popUpTo(Rutas.CONFIGURACION) { inclusive = false }
                    }
                }
            )
        }

        composable(Rutas.TABLERO) {
            PantallaTablero(
                viewModel = viewModel,
                onIniciarTurno = {
                    navController.navigate(Rutas.TURNO_ACTIVO)
                },
                onJuegoFinalizado = {
                    navController.navigate(Rutas.RESULTADO_FINAL) {
                        popUpTo(Rutas.TABLERO) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.TURNO_ACTIVO) {
            PantallaTurnoActivo(
                viewModel = viewModel,
                onTurnoTerminado = {
                    navController.popBackStack()
                },
                onUltimaRonda = {
                    navController.navigate(Rutas.ULTIMA_RONDA) {
                        popUpTo(Rutas.TABLERO) { inclusive = false }
                    }
                }
            )
        }

        composable(Rutas.ULTIMA_RONDA) {
            PantallaUltimaRonda(
                viewModel = viewModel,
                onContinuar = {
                    navController.navigate(Rutas.TABLERO) {
                        popUpTo(Rutas.TABLERO) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.RESULTADO_FINAL) {
            PantallaResultadoFinal(
                viewModel = viewModel,
                onNuevaPartida = {
                    navController.navigate(Rutas.CONFIGURACION) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}