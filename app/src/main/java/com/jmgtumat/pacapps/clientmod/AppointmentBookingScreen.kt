package com.jmgtumat.pacapps.clientmod

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jmgtumat.pacapps.navigation.AppScreens
import com.jmgtumat.pacapps.viewmodels.AppointmentSummaryViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun AppointmentBookingScreen(navController: NavController) {
    val viewModel: AppointmentSummaryViewModel = viewModel()
    val horasDisponibles by viewModel.horasDisponibles.collectAsState()
    val hoy = LocalDate.now()
    val diasDisponibles = (0..60).map { hoy.plusDays(it.toLong()) }

    var diaSeleccionado by remember { mutableStateOf(diasDisponibles.first()) }
    var horaSeleccionada by remember { mutableStateOf("") }

    LaunchedEffect(diaSeleccionado) {
        viewModel.selectedDate = diaSeleccionado
        viewModel.calcularHorasDisponibles()
    }

    ClienteDashboard(navController = navController) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text("Selecciona el día", style = MaterialTheme.typography.titleLarge)
            LazyRow {
                items(diasDisponibles) { dia ->
                    val formato = dia.format(DateTimeFormatter.ofPattern("dd MMM"))
                    Button(
                        onClick = {
                            diaSeleccionado = dia
                            Log.d("AppointmentScreen", "Día seleccionado: $diaSeleccionado")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (dia == diaSeleccionado)
                                MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(formato)
                    }
                }
            }

            Text("Selecciona la hora", style = MaterialTheme.typography.titleLarge)
            LazyRow {
                items(horasDisponibles) { hora ->
                    Button(
                        onClick = {
                            horaSeleccionada = hora
                            Log.d("AppointmentScreen", "Hora seleccionada: $horaSeleccionada")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (hora == horaSeleccionada)
                                MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(hora)
                    }
                }
            }

            Button(
                onClick = {
                    viewModel.selectedTime = horaSeleccionada
                    navController.navigate(AppScreens.AppointmentSummaryScreen.route) {
                        popUpTo(AppScreens.ServiceSelectionScreen.route) { inclusive = true }
                    }
                },
                enabled = horaSeleccionada.isNotEmpty(),
                modifier = Modifier.padding(top = 24.dp)
            ) {
                Text("Continuar")
            }
        }
    }
}



/*
package com.jmgtumat.pacapps.clientmod

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jmgtumat.pacapps.data.Cita
import com.jmgtumat.pacapps.data.Servicio
import com.jmgtumat.pacapps.repository.CitaRepository
import com.jmgtumat.pacapps.repository.ServicioRepository
import com.jmgtumat.pacapps.viewmodels.CitaViewModel
import com.jmgtumat.pacapps.viewmodels.CitaViewModelFactory
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun AppointmentBookingScreen(
    navController: NavController,
    clienteId: String?
) {
    val viewModel: CitaViewModel = viewModel(
        factory = CitaViewModelFactory(CitaRepository())
    )
    val scope = rememberCoroutineScope()
    val servicioRepository = remember { ServicioRepository() }

    // Estado para servicios
    var servicios by remember { mutableStateOf<List<Servicio>>(emptyList()) }
    var servicioSeleccionado by remember { mutableStateOf<Servicio?>(null) }

    // Cargar servicios desde Firebase
    LaunchedEffect(Unit) {
        servicios = servicioRepository.getServicios()
        if (servicios.isNotEmpty()) {
            servicioSeleccionado = servicios.first()
        }
    }

    // Fechas y horas
    val hoy = LocalDate.now()
    val diasDisponibles = (0..60).map { hoy.plusDays(it.toLong()) } // 2 meses
    var diaSeleccionado by remember { mutableStateOf(diasDisponibles.first()) }
    val horasDisponibles = listOf("10:00", "10:30", "11:00", "11:30", "12:00")
    var horaSeleccionada by remember { mutableStateOf(horasDisponibles.first()) }

    val empleadoId = "empleado_unico_francisco"

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Selecciona un servicio", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        servicios.forEach { servicio ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                RadioButton(
                    selected = servicioSeleccionado == servicio,
                    onClick = { servicioSeleccionado = servicio }
                )
                Text(servicio.nombre, style = MaterialTheme.typography.bodyLarge)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Selecciona el día", style = MaterialTheme.typography.titleLarge)
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(diasDisponibles) { dia ->
                val formato = dia.format(DateTimeFormatter.ofPattern("dd MMM"))
                Button(
                    onClick = { diaSeleccionado = dia },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (dia == diaSeleccionado)
                            MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text(formato)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Selecciona la hora", style = MaterialTheme.typography.titleLarge)
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(horasDisponibles) { hora ->
                Button(
                    onClick = { horaSeleccionada = hora },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (hora == horaSeleccionada)
                            MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text(hora)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                scope.launch {
                    try {
                        servicioSeleccionado?.let { servicio ->
                            // Convertir fecha y hora a timestamps
                            val fechaMillis = diaSeleccionado.atStartOfDay()
                                .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

                            val partes = horaSeleccionada.split(":")
                            val horaInicio = diaSeleccionado.atTime(partes[0].toInt(), partes[1].toInt())
                                .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

                            val cita = Cita(
                                clienteId = clienteId ?: "cliente_autenticado",
                                empleadoId = empleadoId,
                                servicioId = servicio.id,
                                fecha = fechaMillis,
                                horaInicio = horaInicio,
                                duracion = servicio.duracion
                            )

                            viewModel.insertCita(cita, clienteId ?: "cliente_autenticado")
                            navController.popBackStack()
                        }
                    } catch (e: Exception) {
                        Log.e("AppointmentBookingScreen", "Error al crear cita", e)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = servicioSeleccionado != null
        ) {
            Text("Confirmar Cita")
        }
    }
}
*/
