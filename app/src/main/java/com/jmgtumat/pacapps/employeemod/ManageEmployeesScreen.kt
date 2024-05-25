package com.jmgtumat.pacapps.employeemod

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.jmgtumat.pacapps.util.SearchBar
import com.jmgtumat.pacapps.viewmodels.EmpleadoViewModel

@Composable
fun ManageEmployeesScreen(
    navController: NavHostController,
    empleadoViewModel: EmpleadoViewModel = viewModel()
) {
    val empleados by empleadoViewModel.empleados.observeAsState(emptyList())
    var searchQuery by remember { mutableStateOf("") }

    EmpleadoDashboard(
        navController = navController,
        empleadoViewModel = empleadoViewModel
    ) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {
            SearchBar(
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it }
            )

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(empleados.filter {
                    it.nombre.contains(searchQuery, ignoreCase = true) ||
                            it.apellidos.contains(searchQuery, ignoreCase = true) ||
                            it.telefono.contains(searchQuery, ignoreCase = true) ||
                            it.correoElectronico.contains(searchQuery, ignoreCase = true)
                }) { empleado ->
                    EmployeeItem(empleado, empleadoViewModel, navController)
                }
            }

            AddEmployeeButton(navController, empleadoViewModel)
        }
    }
}
