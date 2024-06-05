/*
package com.jmgtumat.pacapps.employeemod.employees

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jmgtumat.pacapps.clientmod.ClientAppointmentItem
import com.jmgtumat.pacapps.repository.EmpleadoRepository
import com.jmgtumat.pacapps.viewmodels.EmpleadoViewModel
import com.jmgtumat.pacapps.viewmodels.EmpleadoViewModelFactory

@Composable
fun EmployeeHistoryScreen(navController: NavController) {
    val empleadoViewModel: EmpleadoViewModel = viewModel(
        factory = EmpleadoViewModelFactory(EmpleadoRepository())
    )
    val empleadoId by empleadoViewModel.empleadoId.observeAsState()
    val citasAsignadas by empleadoViewModel.citasAsignadas.observeAsState(emptyList())

    LaunchedEffect(empleadoId) {
        empleadoId?.let { empleadoViewModel.fetchCitasAsignadas(it) }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(citasAsignadas) { cita ->
            ClientAppointmentItem(
                cita = cita,
                servicioNombre = "Servicio Desconocido", // Puedes obtener el nombre del servicio si lo necesitas
                empleadoNombre = "Nombre del Empleado" // Puedes obtener el nombre del empleado si lo necesitas
            )
        }
    }
}


*/
