package com.jmgtumat.pacapps.employeemod

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.navigation.NavHostController
import com.jmgtumat.pacapps.data.Cita
import com.jmgtumat.pacapps.data.Cliente
import com.jmgtumat.pacapps.uiclases.appointments.CitaItem
import com.jmgtumat.pacapps.viewmodels.ClienteViewModel

@Composable
fun HistoryScreen(
    clienteId: String,
    clienteViewModel: ClienteViewModel = viewModel(),
    navController: NavHostController
) {
    val clienteList by clienteViewModel.clientes.observeAsState(emptyList())
    val currentCliente = clienteList.find { it.id == clienteId } ?: Cliente()
    val historialCitas = currentCliente.historialCitas

    var citasList by remember { mutableStateOf<List<Cita>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(historialCitas) {
        val fetchedCitas = historialCitas.mapNotNull { citaId ->
            clienteViewModel.getCitaById(citaId.toString())
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
                CitaItem(cita)
            }
        }
    }
}