package com.jmgtumat.pacapps.clientmod

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jmgtumat.pacapps.repository.CitaRepository
import com.jmgtumat.pacapps.repository.ClienteRepository
import com.jmgtumat.pacapps.util.formatDateNew
import com.jmgtumat.pacapps.util.formatTimeNew
import com.jmgtumat.pacapps.viewmodels.CitaViewModel
import com.jmgtumat.pacapps.viewmodels.CitaViewModelFactory
import com.jmgtumat.pacapps.viewmodels.ClienteViewModel
import com.jmgtumat.pacapps.viewmodels.ClienteViewModelFactory

@Composable
fun ClientHomeScreen(navController: NavController) {
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
    val clienteList by clienteViewModel.clientes.observeAsState(emptyList())
    val pendingCita by clienteViewModel.citaPendiente.observeAsState()

    LaunchedEffect(pendingCita) {
        if (pendingCita == null) {
            clienteViewModel.fetchHistorialCitasClienteAutenticado()
        }
    }

    ClienteDashboard(navController = navController) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            pendingCita?.let { cita ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(text = "Cita Pendiente", style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Servicio: ${cita.servicioId}")
                        Text(text = "Fecha: ${formatDateNew(cita.fecha)}")
                        Text(text = "Hora: ${formatTimeNew(cita.horaInicio)}")
                        Text(text = "Duración: ${cita.duracion} minutos")
                        Text(text = "Estado: ${cita.estado}")
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { citaViewModel.cancelarCita(cita.id) },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text("Cancelar Cita")
                        }
                    }
                }
            } ?: run {
                Text(text = "No tiene ninguna cita pendiente")
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (pendingCita == null) {
                Button(
                    onClick = {
                        navController.navigate("/new_appointments_screen")
                    }
                ) {
                    Text("Crear nueva cita")
                }
            }
        }
    }
}
