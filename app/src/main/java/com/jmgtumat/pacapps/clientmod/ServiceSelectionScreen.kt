package com.jmgtumat.pacapps.clientmod

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import com.jmgtumat.pacapps.viewmodels.ServicioViewModel
import com.jmgtumat.pacapps.viewmodels.ServicioViewModelFactory

@Composable
fun ServiceSelectionScreen(navController: NavController) {
    val servicioViewModel: ServicioViewModel = viewModel(
        factory = ServicioViewModelFactory(ServicioRepository())
    )

    val servicios by servicioViewModel.servicios.observeAsState(emptyList())
    var selectedIds by remember { mutableStateOf(setOf<String>()) }

    ClienteDashboard(navController = navController) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Selecciona los servicios", style = MaterialTheme.typography.headlineSmall)

                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
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
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(servicio.nombre, style = MaterialTheme.typography.titleMedium)
                                Text(
                                    "Duración: ${servicio.duracion} min",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    "Precio: ${servicio.precio} €",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    servicio.descripcion,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }

                Button(
                    onClick = {
                        val ids = selectedIds.joinToString(",")
                        navController.navigate(AppScreens.AppointmentBookingScreen.route + "/$ids")
                    },
                    enabled = selectedIds.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    Text("Continuar")
                }
            }
        }
    }
}
