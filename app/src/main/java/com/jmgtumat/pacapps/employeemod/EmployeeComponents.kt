package com.jmgtumat.pacapps.employeemod

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jmgtumat.pacapps.data.Empleado
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
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ModifyEmployeeButton(empleado, empleadoViewModel)
                    DeleteEmployeeButton(empleado, empleadoViewModel)
                    EmployeeHistoryButton(navController, empleado.id)
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

    Button(onClick = { showDialog = true }) {
        Text("Modificar")
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

    Button(onClick = { showDialog = true }) {
        Text("Eliminar")
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
    Button(
        onClick = {
            navController.navigate("employee_history/$empleadoId")
        },
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text("Ver Historial de Citas")
    }
}
