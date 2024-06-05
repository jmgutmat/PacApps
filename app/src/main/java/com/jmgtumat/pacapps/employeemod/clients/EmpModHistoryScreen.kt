package com.jmgtumat.pacapps.employeemod.clients

import android.util.Log
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
import com.jmgtumat.pacapps.employeemod.EmpleadoDashboard
import com.jmgtumat.pacapps.repository.CitaRepository
import com.jmgtumat.pacapps.repository.ClienteRepository
import com.jmgtumat.pacapps.viewmodels.CitaViewModel
import com.jmgtumat.pacapps.viewmodels.CitaViewModelFactory
import com.jmgtumat.pacapps.viewmodels.ClienteViewModel
import com.jmgtumat.pacapps.viewmodels.ClienteViewModelFactory

@Composable
fun EmpModHistoryScreen(clienteId: String, navController: NavController) {
    Log.d("EmpModHistoryScreen", "Cliente ID: $clienteId") // Agregamos un log aquí para verificar el ID del cliente
    val clienteViewModel: ClienteViewModel = viewModel(
        factory = ClienteViewModelFactory(
            ClienteRepository(),
        )
    )
    val citaViewModel: CitaViewModel = viewModel(
        factory = CitaViewModelFactory(
            CitaRepository(),
        )
    )

    val historialCitas by clienteViewModel.historialCitas.observeAsState(emptyList())
    val serviciosList by clienteViewModel.servicios.observeAsState(emptyList())
    val empleadosList by clienteViewModel.empleados.observeAsState(emptyList())

    LaunchedEffect(clienteId) {
        Log.d("EmpModHistoryScreen", "Cliente ID: $clienteId")
        clienteViewModel.fetchHistorialCitas(clienteId)
    }


    EmpleadoDashboard(navController = navController) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(historialCitas) { cita ->
                Log.d("EmpModHistoryScreen", "Cita: $cita")
                val servicio = serviciosList.find { it.id == cita.servicioId }
                val empleado = empleadosList.find { it.id == cita.empleadoId }
                ClientAppointmentItem(cita,
                    servicio?.nombre ?: "Desconocido",
                    empleado?.nombre ?: "Desconocido")
            }
        }
    }
}
