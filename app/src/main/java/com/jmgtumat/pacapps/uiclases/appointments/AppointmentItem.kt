package com.jmgtumat.pacapps.uiclases.appointments

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jmgtumat.pacapps.data.Cita
import com.jmgtumat.pacapps.data.CitaEstado

@Composable
fun AppointmentItem(cita: Cita, onEditAppointment: (Cita) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onEditAppointment(cita) }
    ) {
        Text(text = "Cliente: ${cita.clienteId}")
        Text(text = "Servicio: ${cita.servicioId}")
        Text(text = "Fecha: ${cita.fecha}")
        Text(text = "Hora: ${cita.horaInicio}")
        Text(text = "Estado: ${cita.estado}")
        if (cita.estado == CitaEstado.PENDIENTE) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { /* Confirmar cita */ }) {
                    Text("Confirmar")
                }
                Button(onClick = { /* Cancelar cita */ }) {
                    Text("Cancelar")
                }
            }
        }
    }
}
