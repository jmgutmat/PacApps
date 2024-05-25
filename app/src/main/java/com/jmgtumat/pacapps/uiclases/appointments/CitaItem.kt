package com.jmgtumat.pacapps.uiclases.appointments

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.jmgtumat.pacapps.data.Cita
import com.jmgtumat.pacapps.util.formatDateNew
import com.jmgtumat.pacapps.util.formatTimeNew

@Composable
fun CitaItem(cita: Cita, onModify: (() -> Unit)? = null, onCancel: (() -> Unit)? = null) {
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
            Text(text = "Servicio: ${cita.servicioId}", style = MaterialTheme.typography.titleLarge)
            Text(text = "Empleado: ${cita.empleadoId}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Fecha: ${formatDateNew(cita.fecha)}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Hora: ${formatTimeNew(cita.horaInicio)}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Estado: ${cita.estado}", style = MaterialTheme.typography.bodyMedium)

            if (expanded) {
                Text(text = "Notas: ${cita.notas ?: "N/A"}", style = MaterialTheme.typography.bodySmall)
                Row {
                    onModify?.let {
                        Button(onClick = it) {
                            Text("Modificar")
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    onCancel?.let {
                        Button(onClick = it) {
                            Text("Cancelar")
                        }
                    }
                }
            }
        }
    }
}
