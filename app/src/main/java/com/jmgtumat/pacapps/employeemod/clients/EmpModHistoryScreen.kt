package com.jmgtumat.pacapps.employeemod.clients

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jmgtumat.pacapps.clientmod.ClientAppointmentItem
import com.jmgtumat.pacapps.data.Cita
import com.jmgtumat.pacapps.data.Cliente
import com.jmgtumat.pacapps.employeemod.EmpleadoDashboard
import com.jmgtumat.pacapps.repository.CitaRepository
import com.jmgtumat.pacapps.repository.ClienteRepository
import com.jmgtumat.pacapps.viewmodels.CitaViewModel
import com.jmgtumat.pacapps.viewmodels.CitaViewModelFactory
import com.jmgtumat.pacapps.viewmodels.ClienteViewModel
import com.jmgtumat.pacapps.viewmodels.ClienteViewModelFactory

@Composable
fun EmpModHistoryScreen(clienteId: String, navController: NavController) {
    val clienteViewModel: ClienteViewModel = viewModel(
        factory = ClienteViewModelFactory(
            ClienteRepository(/* parámetros de configuración si los hay */),
        )
    )
    val citaViewModel: CitaViewModel = viewModel(
        factory = CitaViewModelFactory(
            CitaRepository(/* parámetros de configuración si los hay */),
        )
    )
    val clienteList by clienteViewModel.clientes.observeAsState(emptyList())
    val currentCliente = clienteList.find { it.id == clienteId } ?: Cliente()
    val historialCitas = currentCliente.historialCitas

    var citasList by remember { mutableStateOf<List<Cita>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(historialCitas) {
        val fetchedCitas = historialCitas.map { citaId ->
            citaViewModel.getCitaById(citaId.toString())
        }
        citasList = fetchedCitas
    }

    EmpleadoDashboard(navController = navController) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(citasList) { cita ->
                ClientAppointmentItem(cita)
            }
        }
    }
}