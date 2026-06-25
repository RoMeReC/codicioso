package com.eyzaguirre.codicioso.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EsquemaColores = darkColorScheme(
    primary = AmarilloFiesta,
    onPrimary = FondoOscuro,
    primaryContainer = Color(0xFF7B6000),
    onPrimaryContainer = AmarilloFiesta,
    secondary = RojoCodicioso,
    onSecondary = TextoBlanco,
    secondaryContainer = RojoOscuro,
    onSecondaryContainer = TextoBlanco,
    tertiary = MoradoSobre,
    onTertiary = TextoBlanco,
    tertiaryContainer = Color(0xFF4A0072),
    onTertiaryContainer = TextoBlanco,
    error = NaranjaAlerta,
    onError = TextoBlanco,
    errorContainer = Color(0xFF7A3300),
    onErrorContainer = NaranjaAlerta,
    background = FondoOscuro,
    onBackground = TextoBlanco,
    surface = FondoTarjeta,
    onSurface = TextoBlanco,
    surfaceVariant = FondoSuperficie,
    onSurfaceVariant = TextoGrisClaro,
    outline = AmarilloFiesta.copy(alpha = 0.5f)
)

@Composable
fun CodiciosoTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = EsquemaColores,
        typography = Tipografia,
        content = content
    )
}