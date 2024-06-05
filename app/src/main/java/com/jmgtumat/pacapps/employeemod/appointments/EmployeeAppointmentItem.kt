package com.jmgtumat.pacapps.employeemod.appointments

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
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

/**
 * Componente composable que representa un elemento de cita para el empleado, mostrando información
 * relevante sobre la cita y proporcionando opciones para confirmarla o cancelarla.
 *
 * @param cita La cita que se va a mostrar.
 * @param onConfirm La función de callback que se llama cuando se confirma la cita.
 * @param onCancel La función de callback que se llama cuando se cancela la cita.
 */
@Composable
fun EmployeeAppointmentItem(
    cita: Cita,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    // Estado para controlar la visibilidad del diálogo de confirmación para cancelar la cita
    var showDialog by remember { mutableStateOf(false) }
    // Estado para controlar si el componente está expandido
    var expanded by remember { mutableStateOf(false) }

    // Componente de tarjeta que contiene la información de la cita
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
            // Muestra los detalles de la cita
            Text(text = "Servicio: ${cita.servicioId}", style = MaterialTheme.typography.titleLarge)
            Text(text = "Empleado: ${cita.empleadoId}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Fecha: ${formatDateNew(cita.fecha)}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Hora: ${formatTimeNew(cita.horaInicio)}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Estado: ${cita.estado}", style = MaterialTheme.typography.bodyMedium)

            // Botones para confirmar o cancelar la cita
            Row {
                Button(onClick = onConfirm) {
                    Text("Confirmar")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { showDialog = true }) {
                    Text("Cancelar")
                }
            }
        }
    }

    // Diálogo de confirmación para cancelar la cita
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Cancelar Cita") },
            text = {
                Column {
                    Text(text = "¿Estás seguro de que deseas cancelar esta cita?")
                    Text(text = "Cliente: ${cita.clienteId}")
                    Text(text = "Servicio: ${cita.servicioId}")
                    Text(text = "Hora: ${formatTimeNew(cita.horaInicio)}")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onCancel()
                        showDialog = false
                    }
                ) {
                    Text("Sí")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text("No")
                }
            }
        )
    }
}
