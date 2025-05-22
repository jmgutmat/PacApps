package com.jmgtumat.pacapps.clientmod

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jmgtumat.pacapps.data.Cita
import com.jmgtumat.pacapps.data.CitaEstado
import com.jmgtumat.pacapps.viewmodels.AppointmentSummaryViewModel
import com.jmgtumat.pacapps.viewmodels.CitaViewModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun AppointmentSummaryScreen(
    navController: NavController,
    viewModel: AppointmentSummaryViewModel = viewModel(),
    citaViewModel: CitaViewModel = viewModel(),
    clienteId: String
) {
    val selectedServices = viewModel.selectedServices.collectAsState().value
    val selectedDate = viewModel.selectedDate.collectAsState().value
    val selectedTime = viewModel.selectedTime.collectAsState().value

    val totalPrice = selectedServices.sumOf { it.precio }
    val totalDuration = selectedServices.sumOf { it.duracion }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Resumen de la Cita", style = MaterialTheme.typography.headlineMedium)

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(selectedServices) { servicio ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(servicio.nombre, style = MaterialTheme.typography.titleMedium)
                        Text("Duración: ${servicio.duracion} min", style = MaterialTheme.typography.bodySmall)
                        Text("Precio: ${"%.2f".format(servicio.precio)} €", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }

        Divider()

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
            if (selectedDate != null) {
                Text("Fecha: ${selectedDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))}")
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.AccessTime, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
            Text("Hora: $selectedTime")
        }

        Divider()

        Text("Duración total: $totalDuration min", style = MaterialTheme.typography.bodyMedium)
        Text("Precio total: ${"%.2f".format(totalPrice)} €", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                // Calcular horaInicio como LocalDateTime combinando fecha y hora
                val partesHora = selectedTime?.split(":")
                val fechaHora = partesHora?.get(0)?.let {
                    partesHora[1]
                        .let { it1 -> selectedDate?.atTime(it.toInt(), it1.toInt()) }
                }
                val horaInicioMillis = fechaHora?.atZone(ZoneId.systemDefault())?.toInstant()
                    ?.toEpochMilli()
                val fechaMillis = selectedDate?.atStartOfDay()?.atZone(ZoneId.systemDefault())
                    ?.toInstant()
                    ?.toEpochMilli()

                // Crear cita para cada servicio seleccionado
                selectedServices.forEach { servicio ->
                    val cita = fechaMillis?.let {
                        if (horaInicioMillis != null) {
                            Cita(
                                clienteId = clienteId,
                                empleadoId = "empleado_unico_francisco",
                                servicioId = servicio.id,
                                fecha = it,
                                horaInicio = horaInicioMillis,
                                duracion = servicio.duracion,
                                estado = CitaEstado.PENDIENTE
                            )
                        }
                    }
                    citaViewModel.insertCita(cita, clienteId)
                }

                // Volver al home limpiando el backstack
                navController.navigate("/client_home_screen") {
                    popUpTo(0) { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Confirmar Cita")
        }
    }
}
