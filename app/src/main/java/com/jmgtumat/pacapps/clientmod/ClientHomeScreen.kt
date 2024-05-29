package com.jmgtumat.pacapps.clientmod

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jmgtumat.pacapps.data.Cita
import com.jmgtumat.pacapps.data.CitaEstado
import com.jmgtumat.pacapps.data.Cliente
import com.jmgtumat.pacapps.repository.ClienteRepository
import com.jmgtumat.pacapps.util.formatDateNew
import com.jmgtumat.pacapps.util.formatTimeNew
import com.jmgtumat.pacapps.viewmodels.ClienteViewModel
import com.jmgtumat.pacapps.viewmodels.ClienteViewModelFactory

@Composable
fun ClientHomeScreen(navController: NavController) {

    val clienteViewModel: ClienteViewModel = viewModel(
        factory = ClienteViewModelFactory(
            ClienteRepository(/* parámetros de configuración si los hay */),
        )
    )
    val clienteList by clienteViewModel.clientes.observeAsState(emptyList())
    val currentCliente = clienteList.firstOrNull() ?: Cliente()
    var pendingCita by remember { mutableStateOf<Cita?>(null) }

    LaunchedEffect(currentCliente) {
        pendingCita = currentCliente.historialCitas.firstOrNull { it.estado == CitaEstado.PENDIENTE }
    }

    ClienteDashboard(navController = navController) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            pendingCita?.let { cita ->
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(text = "Cita Pendiente")
                        Text(text = "Servicio: ${cita.servicioId}")
                        Text(text = "Fecha: ${formatDateNew(cita.fecha)}")
                        Text(text = "Hora: ${formatTimeNew(cita.horaInicio)}")
                        Text(text = "Duración: ${cita.duracion} minutos")
                        Text(text = "Estado: ${cita.estado}")
                    }
                }
            } ?: run {
                Text(text = "No tiene ninguna cita pendiente")
                Button(
                    onClick = {
                        navController.navigate("/new_appointments_screen")
                    },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Crear nueva cita")
                }
            }
        }
    }
}
