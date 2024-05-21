package com.jmgtumat.pacapps.uiclases.appointments

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jmgtumat.pacapps.data.Cita
import com.jmgtumat.pacapps.viewmodels.CitaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageAppointmentsScreen(
    empleadoId: String,
    citaViewModel: CitaViewModel = viewModel(),
    onEditAppointment: (Cita) -> Unit
) {
    val citas by citaViewModel.getCitasEmpleado(empleadoId).observeAsState(emptyList())
    val loading by citaViewModel.loading.observeAsState(false)
    val error by citaViewModel.error.observeAsState(null)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestionar Citas") }
            )
        },
        content = { padding ->
            Column(modifier = Modifier.padding(padding).padding(16.dp)) {
                if (loading) {
                    Text(text = "Cargando citas...")
                } else if (error != null) {
                    Text(text = "Error: $error")
                } else {
                    if (citas.isEmpty()) {
                        Text(text = "No hay citas disponibles.")
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            items(citas) { cita ->
                                AppointmentItem(cita = cita, onEditAppointment = onEditAppointment)
                            }
                        }
                    }
                }
            }
        }
    )
}
