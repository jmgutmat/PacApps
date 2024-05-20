package com.jmgtumat.pacapps.uiclases.appointments

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jmgtumat.pacapps.viewmodels.CitaViewModel
import com.jmgtumat.pacapps.data.Cliente
import com.jmgtumat.pacapps.data.Empleado
import com.jmgtumat.pacapps.data.Servicio

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmAppointmentScreen(
    navController: NavController,
    citaViewModel: CitaViewModel = viewModel(),
    cliente: Cliente,
    empleado: Empleado,
    onAppointmentConfirmed: () -> Unit
) {
    val selectedFecha by citaViewModel.selectedFecha.observeAsState()
    val selectedHora by citaViewModel.selectedHora.observeAsState()
    val selectedServicio by citaViewModel.selectedServicio.observeAsState()
    val loading by citaViewModel.loading.observeAsState(initial = false)
    val error by citaViewModel.error.observeAsState()

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text("Confirm Appointment") },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (loading) {
                CircularProgressIndicator()
            } else if (error != null) {
                Text("Error: $error")
            } else {
                Text("Cliente: ${cliente.nombre}")
                Text("Empleado: ${empleado.nombre}")
                Text("Servicio: ${selectedServicio?.nombre}")
                Text("Fecha: ${selectedFecha?.let { it.toString() }}")
                Text("Hora: $selectedHora")

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        citaViewModel.confirmarCita(
                            clienteId = cliente.id,
                            empleadoId = empleado.id
                        )
                        onAppointmentConfirmed()
                    }
                ) {
                    Text("Confirmar Cita")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        navController.popBackStack()
                    }
                ) {
                    Text("Cancelar")
                }
            }
        }
    }
}
