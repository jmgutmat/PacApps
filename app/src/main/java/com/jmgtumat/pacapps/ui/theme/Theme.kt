package com.jmgtumat.pacapps.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

const val stronglyDeemphasizedAlpha = 0.6f
const val slightlyDeemphasizedAlpha = 0.87f

/*private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF64B5F6), // Azul suave
    onPrimary = Color(0xFF000000), // Negro
    primaryContainer = crema, // Blanco
    onPrimaryContainer = Color(0xFF000000), // Negro
    secondary = Color(0xFF00BFA5), // Verde menta
    onSecondary = crema, // Blanco
    secondaryContainer = Color(0xFF64B5F6), // Azul suave
    onSecondaryContainer = Color(0xFF000000), // Negro
    tertiary = Color(0xFF8D6E63), // Marrón claro
    onTertiary = Color(0xFF8D6E63), // Marrón claro
    tertiaryContainer = crema, // Blanco
    onTertiaryContainer = Color(0xFF000000), // Negro
    error = Color(0xFFB71C1C), // Rojo oscuro
    onError = Color(0xFFFFFFFF), // Blanco
    errorContainer = Color(0xFFFFFFFF), // Blanco
    onErrorContainer = Color(0xFF000000), // Negro
    background = crema, // Blanco
    onBackground = Color(0xFF8D6E63), // Marrón claro
    surface = Color(0xFF8D6E63), // Marrón claro
    onSurface = crema, // Blanco
    surfaceVariant = crema, // Blanco
    onSurfaceVariant = crema, // Negro
    outline = Color(0xFF8D6E63), // Marrón claro
    inverseSurface = Color(0xFF2E2E2E), // Gris oscuro
    inverseOnSurface = crema, // Blanco
    inversePrimary = Color(0xFF2E2E2E), // Gris oscuro
    surfaceTint = crema, // Crema
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF64B5F6), // Azul suave
    onPrimary = Color(0xFF000000), // Negro
    primaryContainer = Color(0xFFFFFFFF), // Blanco
    onPrimaryContainer = Color(0xFF000000), // Negro
    secondary = Color(0xFF00BFA5), // Verde menta
    onSecondary = Color(0xFFFFFFFF), // Blanco
    secondaryContainer = Color(0xFF64B5F6), // Azul suave
    onSecondaryContainer = Color(0xFF000000), // Negro
    tertiary = Color(0xFF8D6E63), // Marrón claro
    onTertiary = Color(0xFF8D6E63), // Marrón claro
    tertiaryContainer = Color(0xFFFFFFFF), // Blanco
    onTertiaryContainer = Color(0xFF000000), // Negro
    error = Color(0xFFB71C1C), // Rojo oscuro
    onError = Color(0xFFFFFFFF), // Blanco
    errorContainer = Color(0xFFFFFFFF), // Blanco
    onErrorContainer = Color(0xFF000000), // Negro
    background = Color(0xFF2E2E2E), // Gris oscuro
    onBackground = Color(0xFFFFFFFF), // Blanco
    surface = Color(0xFF8D6E63), // Marrón claro
    onSurface = Color(0xFFFFFFFF), // Blanco
    surfaceVariant = Color(0xFFFFFFFF), // Blanco
    onSurfaceVariant = Color(0xFF000000), // Negro
    outline = Color(0xFF8D6E63), // Marrón claro
    inverseSurface = Color(0xFFFFFFFF), // Blanco
    inverseOnSurface = Color(0xFF000000), // Negro
    inversePrimary = Color(0xFFFFFFFF), // Blanco
    surfaceTint = Color(0xFF3E2723), // Marrón oscuro
)*/


/*private val DarkColorScheme = darkColorScheme(
    primary = raisinblack2,
    secondary = vandike,
    tertiary = alabaster,
    background = raisinblack,
    surface = vandike,

    onPrimary = alabaster,
    onSecondary = white,
    onTertiary = black,
    onBackground = white,
    onSurface = white,

    // Puedes elegir estos colores según tu preferencia
    error = rojo,
    onError = white,
    errorContainer = alabaster,
    onErrorContainer = black,
    outline = alabaster,
    outlineVariant = vandike,
    scrim = black.copy(alpha = 0.3f),
    surfaceBright = vandike,
    surfaceContainer = raisinblack,
    surfaceContainerHigh = raisinblack2,
    surfaceContainerHighest = raisinblack,
    surfaceContainerLow = raisinblack.copy(alpha = 0.1f),
    surfaceContainerLowest = transparent,
    surfaceDim = vandike.copy(alpha = 0.1f),
)


private val LightColorScheme = lightColorScheme(
    primary = crema,
    secondary = vandike,
    tertiary = white,
    background = alabaster,
    surface = vandike,

    onPrimary = black,
    onSecondary = white,
    onTertiary = vandike,
    onBackground = vandike,
    onSurface = white,

    // Puedes elegir estos colores según tu preferencia
    error = rojo,
    onError = white,
    errorContainer = alabaster,
    onErrorContainer = black,
    outline = vandike,
    outlineVariant = alabaster,
    scrim = black.copy(alpha = 0.3f),
    surfaceBright = white,
    surfaceContainer = alabaster,
    surfaceContainerHigh = white,
    surfaceContainerHighest = alabaster,
    surfaceContainerLow = alabaster.copy(alpha = 0.1f),
    surfaceContainerLowest = transparent,
    surfaceDim = vandike.copy(alpha = 0.1f),
)*/

private val LightColors = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    error = md_theme_light_error,
    onError = md_theme_light_onError,
    errorContainer = md_theme_light_errorContainer,
    onErrorContainer = md_theme_light_onErrorContainer,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    outline = md_theme_light_outline,
    inverseSurface = md_theme_light_inverseSurface,
    inverseOnSurface = md_theme_light_inverseOnSurface,
    inversePrimary = md_theme_light_inversePrimary,
    surfaceTint = md_theme_light_surfaceTint,
)

private val DarkColors = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    onError = md_theme_dark_onError,
    errorContainer = md_theme_dark_errorContainer,
    onErrorContainer = md_theme_dark_onErrorContainer,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    outline = md_theme_dark_outline,
    inverseSurface = md_theme_dark_inverseSurface,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inversePrimary = md_theme_dark_inversePrimary,
    surfaceTint = md_theme_dark_surfaceTint,
)

@Composable
fun PacAppsTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (!useDarkTheme) {
        //LightColorScheme
        LightColors
    } else {
        //DarkColorScheme
        DarkColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}