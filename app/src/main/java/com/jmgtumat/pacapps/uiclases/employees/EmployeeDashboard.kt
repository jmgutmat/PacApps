// EmployeeDashboard.kt
package com.jmgtumat.pacapps.uiclases.employees

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jmgtumat.pacapps.util.MenuItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeDashboard(
    currentSection: String,
    navController: NavController,
    content: @Composable () -> Unit
) {
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
                        navController.navigate("ManageEmployeesScreen")
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
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(currentSection) },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        }
                    )
                },
                content = { padding ->
                    Column(modifier = Modifier.padding(padding)) {
                        content()
                    }
                }
            )
        }
    )
}
