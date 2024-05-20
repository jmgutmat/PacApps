package com.jmgtumat.pacapps.uiclases.appointments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jmgtumat.pacapps.data.Cita
import com.jmgtumat.pacapps.data.CitaEstado
import com.jmgtumat.pacapps.data.Cliente
import com.jmgtumat.pacapps.data.Empleado
import com.jmgtumat.pacapps.data.Servicio
import com.jmgtumat.pacapps.viewmodels.CitaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAppointmentScreen(
    viewModel: CitaViewModel,
    onNavigateBack: () -> Unit
) {
    var clienteNombre by remember { mutableStateOf("") }
    var empleadoNombre by remember { mutableStateOf("") }
    var servicioNombre by remember { mutableStateOf("") }
    var fechaHora by remember { mutableStateOf("") }
    var duracion by remember { mutableStateOf("") }
    var notas by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text("Add Appointment") },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = clienteNombre,
                onValueChange = { clienteNombre = it },
                label = { Text("Client Name") }
            )
            OutlinedTextField(
                value = empleadoNombre,
                onValueChange = { empleadoNombre = it },
                label = { Text("Employee Name") }
            )
            OutlinedTextField(
                value = servicioNombre,
                onValueChange = { servicioNombre = it },
                label = { Text("Service Name") }
            )
            OutlinedTextField(
                value = fechaHora,
                onValueChange = { fechaHora = it },
                label = { Text("Date and Time") }
            )
            OutlinedTextField(
                value = duracion,
                onValueChange = { duracion = it },
                label = { Text("Duration") }
            )
            OutlinedTextField(
                value = notas,
                onValueChange = { notas = it },
                label = { Text("Notes") }
            )
            Button(
                onClick = {
                    viewModel.insertCita(
                        Cita(
                            cliente = Cliente(nombre = clienteNombre),
                            empleado = Empleado(nombre = empleadoNombre),
                            servicio = Servicio(nombre = servicioNombre),
                            fechaHora = fechaHora.toLong(), // Convertir a Long
                            duracion = duracion.toInt(), // Convertir a Int
                            estado = CitaEstado.PENDIENTE,
                            notas = notas
                        )
                    )
                    onNavigateBack()
                }
            ) {
                Text("Save")
            }

        }
    }
}
