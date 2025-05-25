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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jmgtumat.pacapps.data.Cita
import com.jmgtumat.pacapps.viewmodels.AppointmentSummaryViewModel
import com.jmgtumat.pacapps.viewmodels.CitaViewModel
import java.time.format.DateTimeFormatter

@Composable
fun AppointmentSummaryScreen(
    navController: NavController,
    clienteId: String // 👈 Añadido el parámetro que faltaba
) {
    val viewModel: AppointmentSummaryViewModel = viewModel()
    val citaViewModel: CitaViewModel = viewModel()

    val selectedServices = viewModel.selectedServices
    val selectedDate = viewModel.selectedDate
    val selectedTime = viewModel.selectedTime

    val totalPrecio = selectedServices.sumOf { it.precio }
    val totalDuracion = selectedServices.sumOf { it.duracion }

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
            selectedServices.forEach { servicio ->
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
                        if (selectedDate != null && selectedTime != null) {
                            val cita = Cita(
                                clienteId = clienteId,
                                empleadoId = "-OL_6myd8kEiS60lWykm",
                                servicioId = selectedServices.joinToString(",") { it.id },
                                fecha = selectedDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli(),
                                horaInicio = selectedTime.let { timeStr ->
                                    val time = java.time.LocalTime.parse(timeStr)
                                    val dateTime = selectedDate.atTime(time)
                                    dateTime.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
                                },
                                duracion = totalDuracion
                            )

                            citaViewModel.insertCita(cita, clienteId)
                            navController.navigate(com.jmgtumat.pacapps.navigation.AppScreens.ClientHomeScreen.route) {
                                popUpTo(com.jmgtumat.pacapps.navigation.AppScreens.ClientHomeScreen.route) { inclusive = true }
                            }
                        } else {
                            Log.e("AppointmentSummary", "Faltan datos para crear la cita")
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Confirmar")
                }

                Button(
                    onClick = {
                        navController.navigate(com.jmgtumat.pacapps.navigation.AppScreens.ClientHomeScreen.route) {
                            popUpTo(com.jmgtumat.pacapps.navigation.AppScreens.ClientHomeScreen.route) { inclusive = true }
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
