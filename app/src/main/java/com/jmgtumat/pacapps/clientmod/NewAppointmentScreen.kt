package com.jmgtumat.pacapps.clientmod

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jmgtumat.pacapps.data.Cita
import com.jmgtumat.pacapps.data.CitaEstado
import com.jmgtumat.pacapps.data.Servicio
import com.jmgtumat.pacapps.navigation.AppScreens
import com.jmgtumat.pacapps.repository.CitaRepository
import com.jmgtumat.pacapps.repository.ClienteRepository
import com.jmgtumat.pacapps.repository.EmpleadoRepository
import com.jmgtumat.pacapps.repository.ServicioRepository
import com.jmgtumat.pacapps.viewmodels.CitaViewModel
import com.jmgtumat.pacapps.viewmodels.CitaViewModelFactory
import com.jmgtumat.pacapps.viewmodels.ClienteViewModel
import com.jmgtumat.pacapps.viewmodels.ClienteViewModelFactory
import com.jmgtumat.pacapps.viewmodels.EmpleadoViewModel
import com.jmgtumat.pacapps.viewmodels.EmpleadoViewModelFactory
import com.jmgtumat.pacapps.viewmodels.ServicioViewModel
import com.jmgtumat.pacapps.viewmodels.ServicioViewModelFactory
import java.util.Calendar

@Composable
fun NewAppointmentScreen(navController: NavController) {

    val clienteViewModel: ClienteViewModel = viewModel(
        factory = ClienteViewModelFactory(
            ClienteRepository(/* parámetros de configuración si los hay */),
        )
    )
    val citaViewModel: CitaViewModel = viewModel(
        factory = CitaViewModelFactory(
            CitaRepository(/* parámetros de configuración si los hay */),
        )
    )
    val empleadoViewModel: EmpleadoViewModel = viewModel(
        factory = EmpleadoViewModelFactory(
            EmpleadoRepository(/* parámetros de configuración si los hay */),
        )
    )
    val servicioViewModel: ServicioViewModel = viewModel(
        factory = ServicioViewModelFactory(
            ServicioRepository(/* parámetros de configuración si los hay */),
        )
    )


    val servicios by servicioViewModel.servicios.observeAsState(emptyList())
    val citas by citaViewModel.citas.observeAsState(emptyList())
    val empleados by empleadoViewModel.empleados.observeAsState(emptyList())

    var selectedServicio by remember { mutableStateOf<Servicio?>(null) }
    var selectedDate by remember { mutableStateOf<Calendar?>(null) }
    var availableDates by remember { mutableStateOf<List<Calendar>>(emptyList()) }
    var availableSlots by remember { mutableStateOf<List<Calendar>>(emptyList()) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var selectedSlot by remember { mutableStateOf<Calendar?>(null) }

    ClienteDashboard(navController = navController) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Listado de servicios
            servicios.forEach { servicio ->
                Button(onClick = {
                    selectedServicio = servicio
                    availableDates = calculateAvailableDates(servicio, citas, empleados)
                    showDatePicker = true
                }) {
                    Text(text = servicio.nombre)
                }
            }

            // DatePicker para seleccionar la fecha
            if (showDatePicker && availableDates.isNotEmpty()) {
                val context = LocalContext.current
                DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        val date = Calendar.getInstance().apply {
                            set(year, month, dayOfMonth)
                        }
                        selectedDate = date
                        availableSlots = calculateAvailableSlots(date, selectedServicio!!, citas, empleados)
                        showDatePicker = false
                    },
                    availableDates[0].get(Calendar.YEAR),
                    availableDates[0].get(Calendar.MONTH),
                    availableDates[0].get(Calendar.DAY_OF_MONTH)
                ).show()
            }

            // Cuadrícula de horarios disponibles
            selectedDate?.let { date ->
                Text(text = "Available slots for ${date.time}")
                availableSlots.forEach { slot ->
                    Button(onClick = {
                        selectedSlot = slot
                        showConfirmationDialog = true
                    }) {
                        Text(text = "${slot.get(Calendar.HOUR_OF_DAY)}:${slot.get(Calendar.MINUTE)}")
                    }
                }
            }

        }
        // Diálogo de Confirmación
        if (showConfirmationDialog) {
            selectedSlot?.let { slot ->
                ConfirmationDialog(
                    slot = slot,
                    servicio = selectedServicio!!,
                    onConfirm = {
                        citaViewModel.insertCita(
                            Cita(
                                clienteId = "cliente_id", // Debería ser el ID del cliente actual
                                empleadoId = empleados.first().id, // Suponiendo que seleccionamos el primer empleado disponible
                                servicioId = selectedServicio!!.id,
                                fecha = selectedDate!!.timeInMillis,
                                horaInicio = slot.timeInMillis,
                                duracion = selectedServicio!!.duracion,
                                estado = CitaEstado.PENDIENTE
                            )
                        )
                        showConfirmationDialog = false
                        navController.navigate(AppScreens.ClientHomeScreen.route) // Navegar de vuelta a la pantalla de inicio del cliente
                    },
                    onDismiss = { showConfirmationDialog = false }
                )
            }
        }
    }
}

@Composable
fun ConfirmationDialog(slot: Calendar, servicio: Servicio, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Confirm Appointment") },
        text = {
            Text(text = "Do you want to book an appointment for ${servicio.nombre} at ${slot.get(Calendar.HOUR_OF_DAY)}:${slot.get(Calendar.MINUTE)}?")
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
