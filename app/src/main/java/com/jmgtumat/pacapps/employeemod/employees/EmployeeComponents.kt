package com.jmgtumat.pacapps.employeemod.employees

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jmgtumat.pacapps.data.Empleado
import com.jmgtumat.pacapps.data.HorariosPorDia
import com.jmgtumat.pacapps.employeemod.appointments.HorarioModulo
import com.jmgtumat.pacapps.viewmodels.EmpleadoViewModel

@Composable
fun EmployeeItem(
    empleado: Empleado,
    empleadoViewModel: EmpleadoViewModel,
    navController: NavController // Agregamos el parámetro NavController
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { expanded = !expanded }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${empleado.nombre} ${empleado.apellidos}",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "Teléfono: ${empleado.telefono}",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            if (expanded) {
                Text(
                    text = "Correo Electrónico: ${empleado.correoElectronico}",
                    style = MaterialTheme.typography.bodyMedium
                )
                // Más información del empleado puede agregarse aquí
                // Botones para modificar, eliminar y ver horario de trabajo del empleado
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ModifyHorarioButton(empleado, empleadoViewModel) // Nuevo botón para modificar horario
                    ModifyEmployeeButton(empleado, empleadoViewModel)
                    //EmployeeHistoryButton(navController, empleado.id)
                    DeleteEmployeeButton(empleado, empleadoViewModel)



                }
            }
        }
    }
}


@Composable
fun AddEmployeeButton(navController: NavController, empleadoViewModel: EmpleadoViewModel) {
    var showDialog by remember { mutableStateOf(false) }

    FloatingActionButton(
        onClick = { showDialog = true },
        modifier = Modifier.padding(16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Nuevo Empleado",
            tint = MaterialTheme.colorScheme.onSurface
        )
    }

    if (showDialog) {
        AddEmployeeDialog(empleadoViewModel = empleadoViewModel) {
            showDialog = false
        }
    }
}

@Composable
fun AddEmployeeDialog(
    empleadoViewModel: EmpleadoViewModel,
    onDismiss: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var correoElectronico by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Añadir Nuevo Empleado") },
        text = {
            Column {
                TextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") }
                )
                TextField(
                    value = apellidos,
                    onValueChange = { apellidos = it },
                    label = { Text("Apellidos") }
                )
                TextField(
                    value = telefono,
                    onValueChange = { telefono = it },
                    label = { Text("Teléfono") }
                )
                TextField(
                    value = correoElectronico,
                    onValueChange = { correoElectronico = it },
                    label = { Text("Correo Electrónico") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val newEmployee = Empleado(
                        nombre = nombre,
                        apellidos = apellidos,
                        telefono = telefono,
                        correoElectronico = correoElectronico,
                        horariosTrabajo = emptyMap(),
                        citasAsignadas = emptyList()
                    )
                    empleadoViewModel.insertEmpleado(newEmployee)
                    onDismiss()
                }
            ) {
                Text("Añadir")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancelar")
            }
        }
    )
}


@Composable
fun ModifyEmployeeButton(
    empleado: Empleado,
    empleadoViewModel: EmpleadoViewModel
) {
    var showDialog by remember { mutableStateOf(false) }

    IconButton(onClick = { showDialog = true }) {
        Icon(Icons.Filled.Edit, contentDescription = "Modificar empleado")
    }

    if (showDialog) {
        ModifyEmployeeDialog(empleado = empleado, empleadoViewModel = empleadoViewModel) {
            showDialog = false
        }
    }
}

@Composable
fun ModifyEmployeeDialog(
    empleado: Empleado,
    empleadoViewModel: EmpleadoViewModel,
    onDismiss: () -> Unit
) {
    var nombre by remember { mutableStateOf(empleado.nombre) }
    var apellidos by remember { mutableStateOf(empleado.apellidos) }
    var telefono by remember { mutableStateOf(empleado.telefono) }
    var correoElectronico by remember { mutableStateOf(empleado.correoElectronico) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Modificar Empleado") },
        text = {
            Column {
                TextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") }
                )
                TextField(
                    value = apellidos,
                    onValueChange = { apellidos = it },
                    label = { Text("Apellidos") }
                )
                TextField(
                    value = telefono,
                    onValueChange = { telefono = it },
                    label = { Text("Teléfono") }
                )
                TextField(
                    value = correoElectronico,
                    onValueChange = { correoElectronico = it },
                    label = { Text("Correo Electrónico") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updatedEmployee = empleado.copy(
                        nombre = nombre,
                        apellidos = apellidos,
                        telefono = telefono,
                        correoElectronico = correoElectronico
                    )
                    empleadoViewModel.updateEmpleado(updatedEmployee)
                    onDismiss()
                }
            ) {
                Text("Actualizar")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun DeleteEmployeeButton(
    empleado: Empleado,
    empleadoViewModel: EmpleadoViewModel
) {
    var showDialog by remember { mutableStateOf(false) }

    IconButton(onClick = { showDialog = true }) {
        Icon(Icons.Filled.Delete, contentDescription = "Eliminar empleado")
    }

    if (showDialog) {
        DeleteEmployeeDialog(empleado = empleado, empleadoViewModel = empleadoViewModel) {
            showDialog = false
        }
    }
}

@Composable
fun DeleteEmployeeDialog(
    empleado: Empleado,
    empleadoViewModel: EmpleadoViewModel,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Eliminar Empleado") },
        text = {
            Column {
                Text("Nombre: ${empleado.nombre}")
                Text("Apellidos: ${empleado.apellidos}")
                Text("Teléfono: ${empleado.telefono}")
                Text("Correo Electrónico: ${empleado.correoElectronico}")
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    empleadoViewModel.deleteEmpleado(empleado.id)
                    onDismiss()
                }
            ) {
                Text("Borrar")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancelar")
            }
        }
    )
}


@Composable
fun EmployeeHistoryButton(navController: NavController, empleadoId: String) {
    IconButton(onClick = { navController.navigate("/employee_history_screen") }) {
        Icon(Icons.Filled.History, contentDescription = "Ver historial de citas")
    }
}

@Composable
fun ModifyHorarioButton(
    empleado: Empleado,
    empleadoViewModel: EmpleadoViewModel
) {
    var showDialog by remember { mutableStateOf(false) }

    IconButton(onClick = { showDialog = true }) {
        Icon(Icons.Filled.Schedule, contentDescription = "Modificar horario de trabajo")
    }

    if (showDialog) {
        ModifyHorarioDialog(empleado = empleado, empleadoViewModel = empleadoViewModel) {
            showDialog = false
        }
    }
}

@Composable
fun ModifyHorarioDialog(
    empleado: Empleado,
    empleadoViewModel: EmpleadoViewModel,
    onDismiss: () -> Unit
) {
    val horariosTrabajo by empleadoViewModel.horariosTrabajo.observeAsState(initial = null)
    var horarioTrabajo by remember { mutableStateOf<Map<String, HorariosPorDia>?>(null) }
    var selectedDay by remember { mutableStateOf("") }

    LaunchedEffect(empleado.id) {
        empleadoViewModel.fetchEmpleados() // Fetch all employees (if needed)
        empleadoViewModel.fetchHorariosTrabajo(empleado.id) // Fetch horarios for this employee
    }

    if (horariosTrabajo == null) {
        CircularProgressIndicator()
    } else {
        horarioTrabajo = horariosTrabajo

        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text("Modificar Horario de Trabajo") },
            text = {
                Column {
                    val diasSemana = listOf("L", "M", "X", "J", "V", "S")
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    ) {
                        diasSemana.forEachIndexed { index, dia ->
                            val dayName = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado")[index]
                            val isSelected = selectedDay == dayName
                            Box(
                                modifier = Modifier
                                    .width(40.dp)
                                    .height(40.dp)
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
                                    .clickable { selectedDay = dayName },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = dia,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.titleSmall
                                )
                            }
                        }
                    }
                    if (selectedDay.isNotEmpty()) {
                        // Handle potential null value for horarioTrabajo[selectedDay]
                        val horarioDia = horarioTrabajo?.get(selectedDay) ?: HorariosPorDia()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Mañana", style = MaterialTheme.typography.headlineSmall)
                        HorarioModulo("Mañana", horarioDia.manana) { updatedManana ->
                            horarioTrabajo?.let { // Update only if horarioTrabajo is not null
                                val mutableHorarioTrabajo = it.toMutableMap()
                                mutableHorarioTrabajo[selectedDay] = horarioDia.copy(manana = updatedManana)
                                horarioTrabajo = mutableHorarioTrabajo // Update state with the modified map
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Tarde", style = MaterialTheme.typography.headlineSmall)
                        HorarioModulo("Tarde", horarioDia.tarde) { updatedTarde ->
                            horarioTrabajo?.let { // Update only if horarioTrabajo is not null
                                val mutableHorarioTrabajo = it.toMutableMap()
                                mutableHorarioTrabajo[selectedDay] = horarioDia.copy(tarde = updatedTarde)
                                horarioTrabajo = mutableHorarioTrabajo // Update state with the modified map
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (horarioTrabajo != null) {
                        empleadoViewModel.updateHorariosTrabajo(empleado.id, horarioTrabajo!!)
                        onDismiss()
                    }
                }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text("Cancelar")
                }
            }
        )
    }
}
