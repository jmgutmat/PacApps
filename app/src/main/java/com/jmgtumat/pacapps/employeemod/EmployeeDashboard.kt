package com.jmgtumat.pacapps.employeemod

import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.jmgtumat.pacapps.viewmodels.AppViewModel

@Composable
fun EmpleadoDashboard(
    navController: NavHostController = rememberNavController(),
    appViewModel: AppViewModel = viewModel(),
    content: @Composable (PaddingValues) -> Unit // Cambiamos la firma para aceptar un @Composable como parámetro
) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController, appViewModel)
        }
    ) { innerPadding ->
        content(innerPadding) // Llamamos al @Composable content y pasamos el innerPadding
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController, appViewModel: AppViewModel) {
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
                selected = false,
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


enum class EmpleadoScreen(val route: String, val icon: ImageVector, val title: String) {
    Citas("citas", Icons.AutoMirrored.Filled.Assignment, "Citas"),
    Clientes("clientes", Icons.Default.Group, "Clientes"),
    Empleados("empleados", Icons.Default.Person, "Empleados"),
    Servicios("servicios", Icons.Outlined.AirlineSeatReclineExtra, "Servicios"),
    Informes("informes", Icons.Default.Timeline, "Informes")
}
