package com.jmgtumat.pacapps.clientmod

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.jmgtumat.pacapps.data.Cita
import com.jmgtumat.pacapps.data.CitaEstado
import com.jmgtumat.pacapps.util.formatDateNew
import com.jmgtumat.pacapps.util.formatTimeNew
import java.util.Calendar

@Composable
fun ClientAppointmentItem(
    cita: Cita,
    servicioNombre: String,
    empleadoNombre: String
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = { expanded = !expanded })
            },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Servicio: $servicioNombre", style = MaterialTheme.typography.titleLarge)
            Text(text = "Hora: ${formatTimeNew(cita.horaInicio)}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Fecha: ${formatDateNew(cita.fecha)}", style = MaterialTheme.typography.bodyMedium)

            if (expanded) {
                Text(text = "Empleado: $empleadoNombre", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Estado: ${cita.estado}", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun NewAppointmentItem(
    cita: Cita,
    onItemClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable(onClick = onItemClicked),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Surface(color = Color.Transparent) {
            Column(modifier = Modifier.padding(16.dp)) {
                BasicText(text = "Servicio: ${cita.servicioId}")
                BasicText(text = "Hora: ${formatTimeNew(cita.horaInicio)}")
            }
        }
    }
}


@Composable
fun AppointmentGrid(
    availableSlots: List<Calendar>,
    citas: List<Cita>,
    onSlotSelected: (Calendar) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(modifier = Modifier.verticalScroll(scrollState)) {
        availableSlots.forEach { slot ->
            val citasInThisSlot = citas.filter { cita ->
                val slotStart = Calendar.getInstance().apply { timeInMillis = slot.timeInMillis }
                val slotEnd = (slotStart.clone() as Calendar).apply { add(Calendar.MINUTE, cita.duracion) }
                cita.horaInicio >= slotStart.timeInMillis && cita.horaInicio < slotEnd.timeInMillis
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(4.dp)
                    .clickable { onSlotSelected(slot) }
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val canvasWidth = size.width
                    val canvasHeight = size.height

                    // Dibujar el fondo de la cita
                    drawRoundRect(
                        color = Color.Green.copy(alpha = 0.3f),
                        topLeft = Offset(0f, 0f),
                        size = size,
                        cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
                    )

                    // Dibujar una sombra suave
                    drawRoundRect(
                        color = Color.Black.copy(alpha = 0.1f),
                        topLeft = Offset(4.dp.toPx(), 4.dp.toPx()),
                        size = size,
                        cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    citasInThisSlot.forEach { cita ->
                        BasicText(
                            text = "Hora: ${formatTimeNew(cita.horaInicio)}",
                            modifier = Modifier
                                .padding(bottom = 4.dp)
                                .align(Alignment.CenterHorizontally), // Centrar la hora
                            style = MaterialTheme.typography.titleLarge // Cambiar el estilo de la fuente
                        )
                    }
                }

                Text(
                    text = String.format("%02d:%02d", slot.get(Calendar.HOUR_OF_DAY), slot.get(Calendar.MINUTE)),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(start = 8.dp, top = 8.dp)
                )
            }
        }
    }
}


@Composable
fun AppointmentHourBlock(
    hour: Int,
    citas: List<Cita>,
    onCitaSelected: (Cita) -> Unit
) {
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
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .background(if (cita.estado == CitaEstado.DISPONIBLE) Color.Green else Color.Red)
                        .clickable { onCitaSelected(cita) }
                        .padding(8.dp)
                ) {
                    Text(text = "${cita.horaInicio}")
                }
            }
        }

        Text(
            text = String.format("%02d:00", hour),
            modifier = Modifier.padding(start = 8.dp, top = 8.dp)
        )
    }
}


