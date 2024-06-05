package com.jmgtumat.pacapps.employeemod.appointments

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.jmgtumat.pacapps.data.Intervalo
import java.util.Calendar

/**
 * Componente composable que muestra un botón para seleccionar una fecha mediante un diálogo de selección de fecha.
 *
 * @param selectedDate La fecha seleccionada actualmente.
 * @param onDateChange La función de callback que se llama cuando se selecciona una nueva fecha.
 */
@Composable
fun DatePicker(selectedDate: Calendar, onDateChange: (Calendar) -> Unit) {
    val context = LocalContext.current
    val year = selectedDate.get(Calendar.YEAR)
    val month = selectedDate.get(Calendar.MONTH)
    val day = selectedDate.get(Calendar.DAY_OF_MONTH)

    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        DatePickerDialog(
            context,
            { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                val newDate = Calendar.getInstance().apply {
                    set(selectedYear, selectedMonth, selectedDay)
                }
                onDateChange(newDate)
                showDialog = false
            }, year, month, day
        ).show()
    }

    Button(onClick = { showDialog = true }) {
        Text(text = "Seleccionar fecha")
    }
}

/**
 * Componente composable que muestra un intervalo de tiempo con casillas de verificación para indicar la disponibilidad.
 *
 * @param title El título del intervalo.
 * @param intervalo El intervalo de tiempo.
 * @param onIntervaloChange La función de callback que se llama cuando se cambia el estado del intervalo.
 */
@Composable
fun HorarioModulo(title: String, intervalo: Intervalo, onIntervaloChange: (Intervalo) -> Unit) {
    var checked by remember { mutableStateOf(intervalo.disponible) }
    var horaInicio by remember { mutableStateOf(intervalo.horaInicio) }
    var horaFin by remember { mutableStateOf(intervalo.horaFin) }

    Column(modifier = Modifier.padding(8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(4.dp)
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = {
                    checked = it
                    onIntervaloChange(intervalo.copy(disponible = checked, horaInicio = horaInicio, horaFin = horaFin))
                }
            )
            Text(
                text = "$horaInicio - $horaFin",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        ) {
            TimePickerButton(time = horaInicio) { newHoraInicio ->
                horaInicio = newHoraInicio
                onIntervaloChange(intervalo.copy(horaInicio = horaInicio, horaFin = horaFin, disponible = checked))
            }
            TimePickerButton(time = horaFin) { newHoraFin ->
                horaFin = newHoraFin
                onIntervaloChange(intervalo.copy(horaInicio = horaInicio, horaFin = horaFin, disponible = checked))
            }
        }
    }
}

/**
 * Componente composable que muestra un botón para seleccionar una hora mediante un diálogo de selección de hora.
 *
 * @param time La hora seleccionada actualmente en formato HH:mm.
 * @param onTimeChange La función de callback que se llama cuando se selecciona una nueva hora.
 */
@Composable
fun TimePickerButton(time: String, onTimeChange: (String) -> Unit) {
    val context = LocalContext.current
    val parts = time.split(":")
    val hour = parts.getOrNull(0)?.toIntOrNull() ?: 0
    val minute = parts.getOrNull(1)?.toIntOrNull() ?: 0

    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        val timePickerDialog = android.app.TimePickerDialog(
            context,
            { _, selectedHour, selectedMinute ->
                onTimeChange(String.format("%02d:%02d", selectedHour, selectedMinute))
                showDialog = false
            },
            hour, minute, true
        )
        timePickerDialog.show()
    }

    Button(onClick = { showDialog = true }) {
        Text(text = time.ifEmpty { "00:00" })
    }
}

/*@Composable
fun HourBlock(intervalo: Intervalo, citas: List<Cita>, onCancelCita: (String) -> Unit, onConfirmCita: (String) -> Unit) {
    val startHour = intervalo.horaInicio.split(":").first().toInt()
    val endHour = intervalo.horaFin.split(":").first().toInt()

    Column(
        modifier = Modifier
            .height(120.dp)
            .fillMaxWidth()
    ) {
        for (hour in startHour until endHour) {
            val hourStart = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            }.timeInMillis

            val hourEnd = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, 59)
                set(Calendar.SECOND, 59)
            }.timeInMillis

            val citasInThisHour = citas.filter { cita ->
                cita.horaInicio in hourStart..hourEnd
            }

            Box(
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val canvasWidth = size.width
                    drawLine(
                        color = Color.Gray,
                        start = Offset(0f, 0f),
                        end = Offset(canvasWidth, 0f),
                        strokeWidth = 1.dp.toPx()
                    )
                    drawLine(
                        color = Color.Gray,
                        start = Offset(0f, size.height / 2),
                        end = Offset(canvasWidth, size.height / 2),
                        strokeWidth = 1.dp.toPx()
                    )
                }

                Column(modifier = Modifier.fillMaxSize()) {
                    citasInThisHour.forEach { cita ->
                        EmployeeAppointmentItem(
                            cita = cita,
                            onCancel = {
                                onCancelCita(cita.id)
                            },
                            onConfirm = {
                                onConfirmCita(cita.id)
                            }
                        )
                    }
                }

                Text(
                    text = String.format("%02d:00", hour),
                    modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                )
            }
        }
    }
}*/

/*
@Composable
fun AddAppointmentButton(navController: NavController) {
    FloatingActionButton(
        onClick = { navController.navigate("new_appointment") },
        modifier = Modifier.padding(16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Nueva Cita",
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun HorarioCompletoConCitas(
    citas: List<Cita>,
    horariosTrabajo: HorariosPorDia,
    onConfirmCita: (String) -> Unit,
    onCancelCita: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    Column(modifier = Modifier.verticalScroll(scrollState)) {
        // Iterar sobre el horario de la mañana
        for (intervalo in listOf(horariosTrabajo.manana)) {
            HourBlock(intervalo, citas, onConfirmCita, onCancelCita)
        }

        // Iterar sobre el horario de la tarde
        for (intervalo in listOf(horariosTrabajo.tarde)) {
            HourBlock(intervalo, citas, onConfirmCita, onCancelCita)
        }
    }
}


@Composable
fun HorariosTrabajo(horariosPorDia: HorariosPorDia, onHorarioChange: (HorariosPorDia) -> Unit) {
    Column {
        HorarioModulo("Mañana", horariosPorDia.manana) { updatedManana ->
            onHorarioChange(horariosPorDia.copy(manana = updatedManana))
        }
        HorarioModulo("Tarde", horariosPorDia.tarde) { updatedTarde ->
            onHorarioChange(horariosPorDia.copy(tarde = updatedTarde))
        }
    }
}

fun defaultHorariosPorDia() = HorariosPorDia(
    Intervalo("09:30", "14:00", true),
    Intervalo("16:30", "20:30", true)
)



*/
