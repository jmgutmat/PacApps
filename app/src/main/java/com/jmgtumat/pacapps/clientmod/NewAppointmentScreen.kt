package com.jmgtumat.pacapps.clientmod

import android.app.DatePickerDialog
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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

/**
 * Pantalla para programar una nueva cita.
 *
 * @param navController El controlador de navegación.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NewAppointmentScreen(navController: NavController) {
    // ViewModel para obtener información del cliente, citas, empleados y servicios
    val clienteViewModel: ClienteViewModel = viewModel(factory = ClienteViewModelFactory(ClienteRepository()))
    val citaViewModel: CitaViewModel = viewModel(factory = CitaViewModelFactory(CitaRepository()))
    val empleadoViewModel: EmpleadoViewModel = viewModel(factory = EmpleadoViewModelFactory(EmpleadoRepository(), ClienteRepository()))
    val servicioViewModel: ServicioViewModel = viewModel(factory = ServicioViewModelFactory(ServicioRepository()))

    // Estado del cliente y las listas de servicios, citas y empleados
    val clienteId by clienteViewModel.clienteId.observeAsState()
    val servicios by servicioViewModel.servicios.observeAsState(emptyList())
    val citas by citaViewModel.citas.observeAsState(emptyList())
    val empleados by empleadoViewModel.empleados.observeAsState(emptyList())

    // Estado para gestionar la selección de servicio, fecha, citas disponibles, ranuras disponibles, etc.
    var selectedServicio by remember { mutableStateOf<Servicio?>(null) }
    var selectedDate by remember { mutableStateOf<Calendar?>(null) }
    var availableDates by remember { mutableStateOf<List<Calendar>>(emptyList()) }
    var availableSlots by remember { mutableStateOf<List<Calendar>>(emptyList()) }

    // Estados para controlar la visibilidad del selector de fecha y el diálogo de confirmación
    var showDatePicker by remember { mutableStateOf(false) }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var selectedSlot by remember { mutableStateOf<Calendar?>(null) }
    var expandedServiceId by remember { mutableStateOf<String?>(null) }

    // Registro de depuración para verificar el estado de las variables
    Log.d("NewAppointmentScreen", "Servicio seleccionado: $selectedServicio")
    Log.d("NewAppointmentScreen", "Fecha seleccionada: $selectedDate")
    Log.d("NewAppointmentScreen", "Ranuras disponibles: ${availableSlots.size}")
    Log.d("NewAppointmentScreen", "Mostrar selector de fecha: $showDatePicker")
    Log.d("NewAppointmentScreen", "Mostrar diálogo de confirmación: $showConfirmationDialog")

    // Mostrar el contenido en el dashboard del cliente
    ClienteDashboard(navController = navController) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Si no se ha seleccionado un servicio, mostrar la lista de servicios
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
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(text = servicio.nombre, style = MaterialTheme.typography.titleLarge)
                                if (expandedServiceId == servicio.id) {
                                    Text(text = "Duración: ${servicio.duracion} minutos", style = MaterialTheme.typography.bodyMedium)
                                    Text(text = "Precio: ${servicio.precio}€", style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                        // Agrega aquí el nuevo botón
                        if (expandedServiceId == servicio.id) {
                            Button(
                                onClick = {
                                    Log.d("NewAppointmentScreen", "Servicio seleccionado: ${servicio.nombre}")
                                    selectedServicio = servicio
                                    availableDates = calculateAvailableDates(servicio, citas, empleados)
                                    Log.d("NewAppointmentScreen", "Available dates: ${availableDates.size}")
                                    showDatePicker = true
                                },
                                shape = MaterialTheme.shapes.medium,
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                modifier = Modifier.align(alignment = Alignment.CenterHorizontally).padding(vertical = 8.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Select Service",
                                        tint = Color.Green,
                                        modifier = Modifier.width(24.dp).height(24.dp)
                                    )
                                    Text(
                                        text = "Seleccionar Servicio",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                // Si se ha seleccionado un servicio, mostrar el selector de fecha
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

                // Mostrar las citas disponibles para la fecha seleccionada
                selectedDate?.let { date ->
                    Text(text = "Citas disponibles para  ${date.time}")
                    AppointmentGrid(
                        citas = citas.filter { cita -> isSameDay(cita.fecha, date) },
                        availableSlots = availableSlots,
                        onSlotSelected = { slot ->
                            selectedSlot = slot
                            showConfirmationDialog = true
                        }
                    )
                }

                // Mostrar el diálogo de confirmación para confirmar la cita
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
                            onDismiss = { showConfirmationDialog = false }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Composable para mostrar un selector de fecha.
 *
 * @param availableDates Lista de fechas disponibles para seleccionar.
 * @param onDateSelected Callback para manejar la selección de fecha.
 */
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

/**
 * Composable para mostrar un diálogo de confirmación para confirmar una cita.
 *
 * @param slot La ranura de tiempo seleccionada para la cita.
 * @param servicio El servicio seleccionado para la cita.
 * @param onConfirm Callback para confirmar la cita.
 * @param onDismiss Callback para descartar el diálogo de confirmación.
 */
@Composable
fun ConfirmationDialog(slot: Calendar, servicio: Servicio, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Confirma tu Cita") },
        text = {
            Text(text = "¿Quieres reservar cita para ${servicio.nombre} a las ${slot.get(Calendar.HOUR_OF_DAY)}:${slot.get(Calendar.MINUTE)}?")
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
