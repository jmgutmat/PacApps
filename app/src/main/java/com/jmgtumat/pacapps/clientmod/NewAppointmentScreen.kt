package com.jmgtumat.pacapps.clientmod

import android.app.DatePickerDialog
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
    val clienteViewModel: ClienteViewModel = viewModel(factory = ClienteViewModelFactory(ClienteRepository()))
    val citaViewModel: CitaViewModel = viewModel(factory = CitaViewModelFactory(CitaRepository()))
    val empleadoViewModel: EmpleadoViewModel = viewModel(factory = EmpleadoViewModelFactory(EmpleadoRepository()))
    val servicioViewModel: ServicioViewModel = viewModel(factory = ServicioViewModelFactory(ServicioRepository()))

    val clienteId by clienteViewModel.clienteId.observeAsState()
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
    var expandedServiceId by remember { mutableStateOf<String?>(null) }

    // Agregar registros de depuración para verificar el estado de las variables
    Log.d("NewAppointmentScreen", "Servicio seleccionado: $selectedServicio")
    Log.d("NewAppointmentScreen", "Fecha seleccionada: $selectedDate")
    Log.d("NewAppointmentScreen", "Ranuras disponibles: ${availableSlots.size}")
    Log.d("NewAppointmentScreen", "Mostrar selector de fecha: $showDatePicker")
    Log.d("NewAppointmentScreen", "Mostrar diálogo de confirmación: $showConfirmationDialog")

    ClienteDashboard(navController = navController) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                //.verticalScroll(rememberScrollState())
        ) {
            if (selectedServicio == null) {
                servicios.forEach { servicio ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                expandedServiceId = if (expandedServiceId == servicio.id) null else servicio.id
                            },
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row( // Use Row instead of Column for horizontal arrangement
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween // Distribute content horizontally
                        ) {
                            Column { // Information section
                                Text(text = servicio.nombre, style = MaterialTheme.typography.titleLarge)
                                if (expandedServiceId == servicio.id) {
                                    Text(text = "Duración: ${servicio.duracion} minutos", style = MaterialTheme.typography.bodyMedium)
                                    Text(text = "Precio: ${servicio.precio}€", style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                            if (expandedServiceId == servicio.id) { // Show button only when service is expanded
                                IconButton(
                                    onClick = {
                                        Log.d("NewAppointmentScreen", "Service selected: ${servicio.nombre}")
                                        selectedServicio = servicio
                                        availableDates = calculateAvailableDates(servicio, citas, empleados)
                                        Log.d("NewAppointmentScreen", "Available dates: ${availableDates.size}")
                                        showDatePicker = true
                                    },
                                    modifier = Modifier.width(80.dp).height(80.dp) // Set button size (adjust as needed)
                                ) {
                                    Icon(Icons.Default.Check,
                                        contentDescription = "Select Service",
                                        tint = Color.Green,
                                        modifier = Modifier.width(80.dp).height(80.dp).background(color = Color.LightGray, shape = CircleShape))
                                }
                            }
                        }
                    }
                }
            } else {
                if (showDatePicker) {
                    DatePickerComposable(
                        availableDates = availableDates,
                        onDateSelected = { date ->
                            Log.d("NewAppointmentScreen", "Date selected: ${date.time}")
                            selectedDate = date
                            selectedServicio?.let { servicio ->
                                availableSlots = calculateAvailableSlots(date, servicio, citas, empleados)
                                Log.d("NewAppointmentScreen", "Available slots: ${availableSlots.size}")
                            }
                            showDatePicker = false
                        }
                    )
                }

                selectedDate?.let { date ->
                    Text(text = "Available slots for ${date.time}")
                    AppointmentGrid(
                        citas = citas.filter { cita -> isSameDay(cita.fecha, date) },
                        availableSlots = availableSlots,
                        onSlotSelected = { slot ->
                            selectedSlot = slot
                            showConfirmationDialog = true
                        }
                    )
                }

                if (showConfirmationDialog) {
                    selectedSlot?.let { slot ->
                        ConfirmationDialog(
                            slot = slot,
                            servicio = selectedServicio!!,
                            onConfirm = {
                                clienteId?.let { id ->
                                    citaViewModel.insertCita(
                                        Cita(
                                            clienteId = id,
                                            empleadoId = empleados.first().id,
                                            servicioId = selectedServicio!!.id,
                                            fecha = selectedDate!!.timeInMillis,
                                            horaInicio = slot.timeInMillis,
                                            duracion = selectedServicio!!.duracion,
                                            estado = CitaEstado.PENDIENTE
                                        ),
                                        clienteId = id
                                    )
                                }
                                showConfirmationDialog = false
                                navController.navigate(AppScreens.ClientHomeScreen.route)
                            },
                            onDismiss = { showConfirmationDialog = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DatePickerComposable(
    availableDates: List<Calendar>,
    onDateSelected: (Calendar) -> Unit
) {
    if (availableDates.isNotEmpty()) {
        val context = LocalContext.current
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val date = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }
                onDateSelected(date)
            },
            availableDates[0].get(Calendar.YEAR),
            availableDates[0].get(Calendar.MONTH),
            availableDates[0].get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}

@Composable
fun ConfirmationDialog(slot: Calendar, servicio: Servicio, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Confirma tu Cita") },
        text = {
            Text(text = "¿Quieres reesrvar cita para ${servicio.nombre} a las ${slot.get(Calendar.HOUR_OF_DAY)}:${slot.get(Calendar.MINUTE)}?")
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}