package com.jmgtumat.pacapps.uiclases.employees

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeScreen(navController: NavController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(modifier = Modifier.padding(16.dp)) {
                    MenuItem("Manage Appointments") {
                        scope.launch { drawerState.close() }
                        navController.navigate("ManageAppointmentsScreen")
                    }
                    MenuItem("Manage Employees") {
                        scope.launch { drawerState.close() }
                        navController.navigate("EmployeeManagementScreen")
                    }
                    MenuItem("Manage Services") {
                        scope.launch { drawerState.close() }
                        navController.navigate("ManageServicesScreen")
                    }
                    MenuItem("Manage Clients") {
                        scope.launch { drawerState.close() }
                        navController.navigate("ManageClientsScreen")
                    }
                    MenuItem("View Reports") {
                        scope.launch { drawerState.close() }
                        navController.navigate("ReportsScreen")
                    }
                }
            }
        },
        content = {
            // Contenido principal de la pantalla de empleado
            // Este contenido puede variar dependiendo de lo que desees mostrar en el dashboard
            // Por ejemplo, estadísticas, resumen de citas pendientes, etc.
        }
    )
}

@Composable
fun MenuItem(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.clickable(onClick = onClick)
    )
}
