package com.jmgtumat.pacapps.employeemod

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.outlined.AirlineSeatReclineExtra
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController

@Composable
fun EmpleadoDashboard(
    navController: NavController,
    content: @Composable (PaddingValues) -> Unit // Cambiamos la firma para aceptar un @Composable como parámetro
) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ){
        content(innerPadding) // Llamamos al @Composable content y pasamos el innerPadding
        }
    }
}

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


sealed class EmpleadoScreen(val route: String, val icon: ImageVector, val title: String) {
    object Citas : EmpleadoScreen("/manage_appointments_screen", Icons.AutoMirrored.Filled.Assignment, "Citas")
    object Clientes : EmpleadoScreen("/manage_clients_screen", Icons.Default.Group, "Clientes")
    object Empleados : EmpleadoScreen("/manage_employees_screen", Icons.Default.Person, "Empleados")
    object Servicios : EmpleadoScreen("/manage_services_screen", Icons.Outlined.AirlineSeatReclineExtra, "Servicios")
    object Informes : EmpleadoScreen("/reports_screen", Icons.Default.Timeline, "Informes")
}
