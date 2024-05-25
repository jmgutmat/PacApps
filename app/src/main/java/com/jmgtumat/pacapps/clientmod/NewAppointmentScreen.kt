package com.jmgtumat.pacapps.clientmod

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.fragment.app.FragmentActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.firebase.auth.FirebaseAuth
import com.jmgtumat.pacapps.data.Cita
import com.jmgtumat.pacapps.data.CitaEstado
import com.jmgtumat.pacapps.data.Empleado
import com.jmgtumat.pacapps.data.Servicio
import com.jmgtumat.pacapps.util.formatDateNew
import com.jmgtumat.pacapps.util.formatTimeNew
import com.jmgtumat.pacapps.viewmodels.ClienteViewModel
import java.util.Calendar

@Composable
fun NewAppointmentScreen(viewModel: ClienteViewModel) {
    val servicios by viewModel.servicios.observeAsState(emptyList())
    val empleados by viewModel.empleados.observeAsState(emptyList())

    var selectedServicio by remember { mutableStateOf<Servicio?>(null) }
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var selectedTime by remember { mutableStateOf<Long?>(null) }
    var availableSlots by remember { mutableStateOf<List<Long>>(emptyList()) }

    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        DropdownMenu(
            expanded = selectedServicio == null,
            onDismissRequest = { /* Do nothing */ }
        ) {
            servicios.forEach { servicio ->
                DropdownMenuItem(
                    onClick = { selectedServicio = servicio },
                    text = { Text(servicio.nombre) }
                )
            }
        }

        if (selectedServicio != null) {
            Button(onClick = { showDialog = true }) {
                Text("Seleccionar Fecha y Hora")
            }

            if (selectedDate != null && selectedTime != null) {
                val formattedDate = formatDateNew(selectedDate!!)
                val formattedTime = formatTimeNew(selectedTime!!)
                val summary = "Servicio: ${selectedServicio!!.nombre}\nFecha: $formattedDate\nHora: $formattedTime"
                Text(summary)

                Button(onClick = {
                    val clienteId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                    val empleadoId = empleados.firstOrNull()?.id ?: ""
                    val cita = Cita(
                        clienteId = clienteId,
                        empleadoId = empleadoId,
                        servicioId = selectedServicio!!.id,
                        fecha = selectedDate!!,
                        horaInicio = selectedTime!!,
                        duracion = selectedServicio!!.duracion,
                        estado = CitaEstado.PENDIENTE
                    )
                    viewModel.insertCita(cita)
                }) {
                    Text("Confirmar Cita")
                }
            }
        }

        if (showDialog) {
            val currentContext = LocalContext.current // Obtener el contexto actual
            DateTimePickerDialog(
                context = currentContext, // Pasar el contexto a la función
                onDateSelected = { date ->
                    selectedDate = date
                    selectedServicio?.let { servicio ->
                        calculateAvailableSlots(viewModel, servicio, date, empleados) { slots ->
                            availableSlots = slots
                        }
                    }
                },
                onTimeSelected = { time ->
                    selectedTime = time
                    showDialog = false
                },
                onDismiss = { showDialog = false }
            )
        }
    }
}

fun DateTimePickerDialog(
    context: Context, // Agregar el parámetro context
    onDateSelected: (Long) -> Unit,
    onTimeSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val calendar = Calendar.getInstance()

    val datePicker = MaterialDatePicker.Builder.datePicker()
        .setTitleText("Select Date")
        .setSelection(calendar.timeInMillis)
        .build()

    datePicker.addOnPositiveButtonClickListener { selection ->
        onDateSelected(selection)
        showTimePicker(context, onTimeSelected, onDismiss)
    }

    datePicker.addOnNegativeButtonClickListener {
        onDismiss()
    }

    datePicker.show((context as FragmentActivity).supportFragmentManager, "DatePicker") // Corregir aquí
}

private fun showTimePicker(
    context: Context, // Agregar el parámetro context
    onTimeSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val calendar = Calendar.getInstance()

    val timePicker = MaterialTimePicker.Builder()
        .setHour(calendar.get(Calendar.HOUR_OF_DAY))
        .setMinute(calendar.get(Calendar.MINUTE))
        .setTitleText("Select Time")
        .build()

    timePicker.addOnPositiveButtonClickListener {
        val selectedTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, timePicker.hour)
            set(Calendar.MINUTE, timePicker.minute)
        }.timeInMillis

        onTimeSelected(selectedTime)
    }

    timePicker.addOnNegativeButtonClickListener {
        onDismiss()
    }

    timePicker.show((context as FragmentActivity).supportFragmentManager, "TimePicker") // Corregir aquí
}


fun calculateAvailableSlots(
    viewModel: ClienteViewModel,
    servicio: Servicio,
    date: Long,
    empleados: List<Empleado>,
    onSlotsCalculated: (List<Long>) -> Unit
) {
    val startOfDay = Calendar.getInstance().apply {
        timeInMillis = date
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    val endOfDay = Calendar.getInstance().apply {
        timeInMillis = date
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
        set(Calendar.MILLISECOND, 999)
    }.timeInMillis

    viewModel.getCitasByDateRange(startOfDay, endOfDay) { citas ->
        val servicioDurationMillis = servicio.duracion * 60 * 1000
        val workStartHour = 9 // Asumimos que el trabajo comienza a las 9 AM
        val workEndHour = 17 // Asumimos que el trabajo termina a las 5 PM

        val availableSlots = mutableListOf<Long>()
        val calendar = Calendar.getInstance().apply { timeInMillis = date }

        for (hour in workStartHour until workEndHour) {
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, 0)
            val slotStart = calendar.timeInMillis
            // Verifica si hay citas en el rango de esta hora
            val hasConflict = citas.any { cita ->
                val citaStart = cita.horaInicio
                val citaEnd = citaStart + (cita.duracion * 60 * 1000)
                citaStart <= slotStart + servicioDurationMillis && slotStart <= citaEnd
            }

            // Si no hay conflictos, agrega este slot a los disponibles
            if (!hasConflict) {
                availableSlots.add(slotStart)
            }
        }

        // Llama a la función de retorno de llamada con los slots disponibles
        onSlotsCalculated(availableSlots)
    }
}

