package com.jmgtumat.pacapps.employeemod

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jmgtumat.pacapps.data.Cita
import com.jmgtumat.pacapps.data.HorariosPorDia
import com.jmgtumat.pacapps.data.Intervalo
import com.jmgtumat.pacapps.uiclases.appointments.CitaItem
import java.util.Calendar

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

@Composable
fun HorarioModulo(title: String, intervalo: Intervalo, onIntervaloChange: (Intervalo) -> Unit) {
    var checked by remember { mutableStateOf(intervalo.disponible) }
    var horaInicio by remember { mutableStateOf(intervalo.horaInicio) }
    var horaFin by remember { mutableStateOf(intervalo.horaFin) }

    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
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
            Spacer(modifier = Modifier.width(8.dp))
            TimePickerButton(time = horaInicio) { newHoraInicio ->
                horaInicio = newHoraInicio
                onIntervaloChange(intervalo.copy(horaInicio = horaInicio, horaFin = horaFin, disponible = checked))
            }
            Spacer(modifier = Modifier.width(8.dp))
            TimePickerButton(time = horaFin) { newHoraFin ->
                horaFin = newHoraFin
                onIntervaloChange(intervalo.copy(horaInicio = horaInicio, horaFin = horaFin, disponible = checked))
            }
        }
    }
}

@Composable
fun TimePickerButton(time: String, onTimeChange: (String) -> Unit) {
    val context = LocalContext.current
    val hour = time.split(":")[0].toInt()
    val minute = time.split(":")[1].toInt()

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
        Text(text = time)
    }
}

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
fun HorarioCompletoConCitas(citas: List<Cita>) {
    val scrollState = rememberScrollState()
    Column(modifier = Modifier.verticalScroll(scrollState)) {
        for (hour in 0..23) {
            HourBlock(hour, citas)
        }
    }
}

@Composable
fun HourBlock(hour: Int, citas: List<Cita>) {
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

    Box(modifier = Modifier.height(120.dp)) {
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
                CitaItem(cita)
            }
        }

        Text(
            text = String.format("%02d:00", hour),
            modifier = Modifier.padding(start = 8.dp, top = 8.dp)
        )
    }
}

fun defaultHorariosPorDia() = HorariosPorDia(
    Intervalo("09:30", "14:00", true),
    Intervalo("16:30", "20:30", true)
)



