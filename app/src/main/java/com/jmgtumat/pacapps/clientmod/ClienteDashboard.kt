package com.jmgtumat.pacapps.clientmod

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController

/**
 * Composable que muestra el contenido principal del dashboard del cliente.
 *
 * @param navController el controlador de navegación.
 * @param contenido el contenido a mostrar en el dashboard.
 */
@Composable
fun ClienteDashboard(
    navController: NavController,
    contenido: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding()
        ) {
            contenido(innerPadding)
        }
    }
}

/**
 * Composable que muestra la barra de navegación inferior del cliente.
 *
 * @param navController el controlador de navegación.
 */
@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        ClienteScreen.Profile,
        ClienteScreen.History,
        ClienteScreen.NewAppointment,
        ClienteScreen.Home
    )

    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = null) },
                label = { Text(screen.title) },
                selected = navController.currentDestination?.route == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

/**
 * Clase sellada que representa las diferentes pantallas del cliente.
 *
 * @param route la ruta de navegación de la pantalla.
 * @param title el título de la pantalla.
 * @param icon el ícono asociado a la pantalla.
 */
sealed class ClienteScreen(val route: String, val title: String, val icon: ImageVector) {
    object Profile : ClienteScreen("/profile_screen", "Perfil", Icons.Default.Person)
    object History : ClienteScreen("/clientmod_history_screen", "Historial", Icons.Default.History)
    object NewAppointment : ClienteScreen("/new_appointments_screen", "Nueva Cita", Icons.Default.Add)
    object Home : ClienteScreen("/client_home_screen", "Inicio", Icons.Default.Home)
}
