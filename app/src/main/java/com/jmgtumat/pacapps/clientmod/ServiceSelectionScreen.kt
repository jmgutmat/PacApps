package com.jmgtumat.pacapps.clientmod

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jmgtumat.pacapps.navigation.AppScreens
import com.jmgtumat.pacapps.repository.ServicioRepository
import com.jmgtumat.pacapps.viewmodels.AppointmentSummaryViewModel
import com.jmgtumat.pacapps.viewmodels.ServicioViewModel
import com.jmgtumat.pacapps.viewmodels.ServicioViewModelFactory

@Composable
fun ServiceSelectionScreen(navController: NavController) {
    val servicioViewModel: ServicioViewModel = viewModel(factory = ServicioViewModelFactory(ServicioRepository()))
    val appointmentSummaryViewModel: AppointmentSummaryViewModel = viewModel()

    val servicios by servicioViewModel.servicios.observeAsState(emptyList())
    var selectedIds by remember { mutableStateOf(setOf<String>()) }

    ClienteDashboard(navController = navController) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(
                "Selecciona los servicios",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .weight(1f) // Ocupa el espacio restante
                    .padding(horizontal = 16.dp)
            ) {
                items(servicios) { servicio ->
                    val isSelected = selectedIds.contains(servicio.id)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedIds = if (isSelected) {
                                    selectedIds - servicio.id
                                } else {
                                    selectedIds + servicio.id
                                }
                            }
                            .border(
                                width = if (isSelected) 2.dp else 0.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
                            ),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFEDE7F6))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(servicio.nombre, style = MaterialTheme.typography.titleMedium)
                            Text("Duración: ${servicio.duracion} min", style = MaterialTheme.typography.bodyMedium)
                            Text("Precio: ${servicio.precio} €", style = MaterialTheme.typography.bodyMedium)
                            Text(servicio.descripcion, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

            // --- Zona inferior con resumen y botón ---
            if (selectedIds.isNotEmpty()) {
                val selectedServices = servicios.filter { selectedIds.contains(it.id) }
                val totalPrecio = selectedServices.sumOf { it.precio } // Sin toDoubleOrNull()
                val totalTiempo = selectedServices.sumOf { it.duracion } // Sin toIntOrNull()

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        "${"%.2f".format(totalPrecio)}€ | ${selectedServices.size} servicio(s) - ${totalTiempo} min",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Button(
                        onClick = {
                            appointmentSummaryViewModel.setSelectedServices(selectedServices)
                            navController.navigate(AppScreens.AppointmentBookingScreen.route)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Continuar")
                    }
                }
            }
        }
    }
}
