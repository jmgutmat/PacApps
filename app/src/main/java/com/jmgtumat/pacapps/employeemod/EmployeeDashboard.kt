package com.jmgtumat.pacapps.employeemod

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.outlined.AirlineSeatReclineExtra
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.jmgtumat.pacapps.navigation.AppScreens

/**
 * Panel de control del empleado que muestra las diferentes funcionalidades disponibles.
 *
 * @param navController El controlador de navegación utilizado para la navegación entre pantallas.
 * @param content La interfaz de usuario composable que se debe mostrar en el panel de control.
 */
@Composable
fun EmpleadoDashboard(
    navController: NavController,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Tu título existente (opcional)
                Text("Dashboard del Empleado",
                    style = MaterialTheme.typography.titleLarge)

                IconButton(onClick = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate(AppScreens.MainScreen.route)
                }) {
                    Icon(Icons.Filled.Logout, contentDescription = "Logout")
                }
            }
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ){
            content(innerPadding)
        }
    }
}

/**
 * Barra de navegación inferior que contiene los íconos de las diferentes funcionalidades disponibles.
 *
 * @param navController El controlador de navegación utilizado para la navegación entre pantallas.
 */
@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        EmpleadoScreen.Citas,
        EmpleadoScreen.Clientes,
        EmpleadoScreen.Empleados,
        EmpleadoScreen.Servicios,
        EmpleadoScreen.Informes
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
 * Enumeración sellada que representa las diferentes pantallas disponibles en el panel de control del empleado.
 *
 * @param route La ruta de navegación asociada a la pantalla.
 * @param icon El ícono asociado a la pantalla.
 * @param title El título de la pantalla.
 */
sealed class EmpleadoScreen(val route: String, val icon: ImageVector, val title: String) {
    object Citas : EmpleadoScreen("/manage_appointments_screen", Icons.AutoMirrored.Filled.Assignment, "Citas")
    object Clientes : EmpleadoScreen("/manage_clients_screen", Icons.Default.Group, "Clientes")
    object Empleados : EmpleadoScreen("/manage_employees_screen", Icons.Default.Person, "Empleados")
    object Servicios : EmpleadoScreen("/manage_services_screen", Icons.Outlined.AirlineSeatReclineExtra, "Servicios")
    object Informes : EmpleadoScreen("/reports_screen", Icons.Default.Timeline, "Informes")
}
