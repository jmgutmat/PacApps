package com.jmgtumat.pacapps.clientmod

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jmgtumat.pacapps.data.Cita
import com.jmgtumat.pacapps.data.Servicio
import com.jmgtumat.pacapps.navigation.AppScreens
import com.jmgtumat.pacapps.repository.CitaRepository
import com.jmgtumat.pacapps.repository.ServicioRepository
import com.jmgtumat.pacapps.viewmodels.CitaViewModel
import com.jmgtumat.pacapps.viewmodels.CitaViewModelFactory
import com.jmgtumat.pacapps.viewmodels.ServicioViewModel
import com.jmgtumat.pacapps.viewmodels.ServicioViewModelFactory
import java.time.format.DateTimeFormatter

@Composable
fun AppointmentSummaryScreen(
    navController: NavController,
    clienteId: String
) {
    // 🆕 Obtenemos la instancia de CitaViewModel del back stack entry anterior (la ruta de booking)
    val citaViewModel: CitaViewModel = viewModel(
        viewModelStoreOwner = remember(navController.previousBackStackEntry) {
            navController.getBackStackEntry(AppScreens.AppointmentBookingScreen.route)
        },
        factory = CitaViewModelFactory(CitaRepository())
    )

    val servicioViewModel: ServicioViewModel = viewModel(
        factory = ServicioViewModelFactory(ServicioRepository())
    )

    val serviciosState = servicioViewModel.servicios.observeAsState(initial = emptyList())
    val citaActualState = citaViewModel.citaActual.observeAsState(initial = null)

    val servicios: List<Servicio> = serviciosState.value
    val citaActual: Cita? = citaActualState.value

    // 👇 Procesamos los servicios de la cita actual
    val serviciosDeCita = remember(servicios, citaActual) {
        citaActual?.servicioId
            ?.split(",")
            ?.mapNotNull { id -> servicios.find { it.id == id } }
            ?: emptyList()
    }

    val totalPrecio = serviciosDeCita.sumOf { it.precio }
    val totalDuracion = serviciosDeCita.sumOf { it.duracion }

    val selectedDate = citaActual?.fecha?.let { millis ->
        java.time.Instant.ofEpochMilli(millis)
            .atZone(java.time.ZoneId.systemDefault())
            .toLocalDate()
    }

    val selectedTime = citaActual?.horaInicio?.let { millis ->
        java.time.Instant.ofEpochMilli(millis)
            .atZone(java.time.ZoneId.systemDefault())
            .toLocalTime()
            .toString()
    }

    // 👇 UI final con el estilo que ya tenías
    ClienteDashboard(navController = navController) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Resumen de la cita", style = MaterialTheme.typography.headlineSmall)
            Text("Fecha: ${selectedDate?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) ?: "No seleccionada"}")
            Text("Hora: ${selectedTime ?: "No seleccionada"}")
            Text("Duración total: $totalDuracion min")
            Text("Precio total: %.2f€".format(totalPrecio))

            Divider()

            Text("Servicios seleccionados:", style = MaterialTheme.typography.titleMedium)
            serviciosDeCita.forEach { servicio ->
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(servicio.nombre, style = MaterialTheme.typography.bodyLarge)
                    Text("Duración: ${servicio.duracion} min", style = MaterialTheme.typography.bodySmall)
                    Text("Precio: %.2f€".format(servicio.precio), style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Atrás")
                }

                Button(
                    onClick = {
                        citaActual?.let { cita ->
                            citaViewModel.insertCita(cita, clienteId)
                            navController.navigate(AppScreens.ClientHomeScreen.route) {
                                popUpTo(AppScreens.ClientHomeScreen.route) { inclusive = true }
                            }
                        } ?: Log.e("AppointmentSummary", "Faltan datos para crear la cita")
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Confirmar")
                }

                Button(
                    onClick = {
                        navController.navigate(AppScreens.ClientHomeScreen.route) {
                            popUpTo(AppScreens.ClientHomeScreen.route) { inclusive = true }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Cancelar")
                }
            }
        }
    }
}