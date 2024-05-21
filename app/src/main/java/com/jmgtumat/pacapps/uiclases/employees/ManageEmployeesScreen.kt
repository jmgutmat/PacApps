// ManageEmployeesScreen.kt
package com.jmgtumat.pacapps.uiclases.employees

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jmgtumat.pacapps.data.Empleado
import com.jmgtumat.pacapps.viewmodels.EmpleadoViewModel

@Composable
fun ManageEmployeesScreen(
    navController: NavController,
    empleadoViewModel: EmpleadoViewModel = viewModel(),
    onEditEmployee: (Empleado) -> Unit
) {
    val empleados by empleadoViewModel.empleados.observeAsState(emptyList())
    val loading by empleadoViewModel.loading.observeAsState(false)
    val error by empleadoViewModel.error.observeAsState(null)

    EmployeeDashboard(
        currentSection = "Manage Employees",
        navController = navController
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (loading) {
                Text(text = "Loading employees...")
            } else if (error != null) {
                Text(text = "Error: $error")
            } else {
                if (empleados.isEmpty()) {
                    Text(text = "No employees available.")
                } else {
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(empleados) { empleado ->
                            EmployeeItem(empleado = empleado, onClick = { onEditEmployee(empleado) })
                        }
                    }
                }
            }
        }
    }
}
