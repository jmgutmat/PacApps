package com.jmgtumat.pacapps.clientmod

import android.util.Log
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
        factory = ClienteViewModelFactory(ClienteRepository())
    )
    val citaViewModel: CitaViewModel = viewModel(
        factory = CitaViewModelFactory(CitaRepository())
    )

    val clienteId by clienteViewModel.clienteId.observeAsState()
    val pendingCita by clienteViewModel.citaPendiente.observeAsState()
    val serviciosList by clienteViewModel.servicios.observeAsState(emptyList())


    LaunchedEffect(clienteId) {
        clienteId?.let {
            Log.d("ClientHomeScreen", "Fetching data for clienteId: $it")
            clienteViewModel.fetchHistorialCitas(it)
            clienteViewModel.fetchCitaPendienteClienteAutenticado()
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
            Log.d("ClientHomeScreen", "Rendering UI with pendingCita: $pendingCita")
            pendingCita?.let { cita ->
                val servicio = serviciosList.find { it.id == cita.servicioId }

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
                        if (servicio != null) {
                            Text(text = "Servicio: ${servicio.nombre ?: "Cargando..."}")
                        }
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
                Spacer(modifier = Modifier.height(16.dp))
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
