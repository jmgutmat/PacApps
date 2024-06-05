package com.jmgtumat.pacapps.clientmod

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
import com.jmgtumat.pacapps.repository.CitaRepository
import com.jmgtumat.pacapps.repository.ClienteRepository
import com.jmgtumat.pacapps.viewmodels.CitaViewModel
import com.jmgtumat.pacapps.viewmodels.CitaViewModelFactory
import com.jmgtumat.pacapps.viewmodels.ClienteViewModel
import com.jmgtumat.pacapps.viewmodels.ClienteViewModelFactory

/**
 * Composable para mostrar el historial de citas del cliente.
 *
 * @param navController El controlador de navegación.
 */
@Composable
fun ClientmodHistoryScreen(navController: NavController) {
    // Se obtienen las instancias de los ViewModel
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

    // Observa el ID del cliente y el historial de citas
    val clienteId by clienteViewModel.clienteId.observeAsState()
    val historialCitas by clienteViewModel.historialCitas.observeAsState(emptyList())
    val serviciosList by clienteViewModel.servicios.observeAsState(emptyList())
    val empleadosList by clienteViewModel.empleados.observeAsState(emptyList())

    // Al detectar un cambio en el ID del cliente, se recupera el historial de citas
    LaunchedEffect(clienteId) {
        clienteId?.let {
            clienteViewModel.fetchHistorialCitas(it)
        }
    }

    // Se muestra el historial de citas en el dashboard del cliente
    ClienteDashboard(navController = navController) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(historialCitas) { cita ->
                // Busca el servicio y el empleado asociados a cada cita
                val servicio = serviciosList.find { it.id == cita.servicioId }
                val empleado = empleadosList.find { it.id == cita.empleadoId }
                // Muestra cada elemento del historial de citas
                ClientAppointmentItem(cita,
                    servicio?.nombre ?: "Desconocido",
                    empleado?.nombre ?: "Desconocido")
            }
        }
    }
}
