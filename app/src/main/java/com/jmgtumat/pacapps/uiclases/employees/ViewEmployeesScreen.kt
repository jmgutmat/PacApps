package com.jmgtumat.pacapps.uiclases.employees

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.preference.forEach
import com.jmgtumat.pacapps.data.Empleado
import com.jmgtumat.pacapps.viewmodels.EmpleadoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewEmployeesScreen(
    viewModel: EmpleadoViewModel,
    onNavigateToAddEmployee: () -> Unit,
    onNavigateToEditEmployee: (Empleado) -> Unit
) {
    val empleados by viewModel.empleados.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text("View Employees") },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAddEmployee) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Employee"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            empleados.forEach { empleado ->
                EmployeeItem(
                    empleado = empleado,
                    onClick = { onNavigateToEditEmployee(empleado) }
                )
            }
        }
    }
}