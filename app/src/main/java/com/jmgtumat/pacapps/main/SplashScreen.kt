package com.jmgtumat.pacapps.main

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.jmgtumat.pacapps.R
import com.jmgtumat.pacapps.navigation.AppScreens
import com.jmgtumat.pacapps.ui.theme.PacAppsTheme
import kotlinx.coroutines.delay

/**
 * Pantalla de presentación que se muestra al inicio de la aplicación.
 * @param navController Controlador de navegación para gestionar las transiciones entre pantallas.
 */
@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(key1 = true){
        // Retraso de 2 segundos antes de navegar a la pantalla principal.
        delay(2000)
        navController.popBackStack()
        navController.navigate(AppScreens.MainScreen.route){
            // Al navegar a la pantalla principal, se elimina la pantalla de presentación del back stack.
            popUpTo(AppScreens.SplashScreen.route) { inclusive = true }
        }
    }
    Splash()
}

/**
 * Componente de composición para la pantalla de presentación.
 */
@Composable
fun Splash() {
    Image(
        painter = painterResource(id = R.drawable.inicio),
        contentDescription = "Imagen inicio",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}

/**
 * Vista previa de la pantalla de presentación.
 */
@Preview(
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
    device = "id:Nexus One"
)
@Composable
fun SplashScreenPreview() {
    PacAppsTheme() {
        Splash()
    }
}
