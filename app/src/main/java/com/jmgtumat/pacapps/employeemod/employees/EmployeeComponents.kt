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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import com.jmgtumat.pacapps.data.Cliente
import com.jmgtumat.pacapps.data.Empleado
import com.jmgtumat.pacapps.data.HorariosPorDia
import com.jmgtumat.pacapps.employeemod.appointments.HorarioModulo
import com.jmgtumat.pacapps.viewmodels.ClienteViewModel
import com.jmgtumat.pacapps.viewmodels.EmpleadoViewModel

/**
 * Composable que muestra un elemento de empleado con su información básica y opciones de acción.
 *
 * @param empleado El objeto Empleado que contiene la información del empleado a mostrar.
 * @param empleadoViewModel ViewModel para interactuar con los datos del empleado.
 * @param navController Controlador de navegación para manejar la navegación en la aplicación.
 */
@Composable
fun EmployeeItem(
    empleado: Empleado,
    empleadoViewModel: EmpleadoViewModel,
    navController: NavController // Parámetro para controlar la navegación
) {
    var expanded by remember { mutableStateOf(false) }

    /**
     * Diseño de un elemento de empleado.
     */
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
                // Nombre completo del empleado
                Text(
                    text = "${empleado.nombre} ${empleado.apellidos}",
                    style = MaterialTheme.typography.titleMedium
                )
                // Número de teléfono del empleado
                Text(
                    text = "Teléfono: ${empleado.telefono}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            if (expanded) {
                // Correo electrónico del empleado
                Text(
                    text = "Correo Electrónico: ${empleado.correoElectronico}",
                    style = MaterialTheme.typography.bodyMedium
                )
                // Sección para mostrar más información y opciones de acción para el empleado
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Botón para modificar horario del empleado
                    ModifyHorarioButton(empleado, empleadoViewModel)
                    // Botón para modificar información del empleado
                    ModifyEmployeeButton(empleado, empleadoViewModel)
                    // Botón para eliminar empleado
                    DeleteEmployeeButton(empleado, empleadoViewModel)
                }
            }
        }
    }
}



/**
 * Composable que muestra un botón flotante para agregar un nuevo empleado y controla el diálogo de agregar empleado.
 *
 * @param navController Controlador de navegación para manejar la navegación en la aplicación.
 * @param empleadoViewModel ViewModel para interactuar con los datos del empleado.
 */
/*@Composable
fun AddEmployeeButton(navController: NavController, empleadoViewModel: EmpleadoViewModel) {
    var showDialog by remember { mutableStateOf(false) }

    *//**
     * Botón flotante para agregar un nuevo empleado.
     *//*
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
}*/

@Composable
fun AddEmployeeButton(navController: NavController, empleadoViewModel: EmpleadoViewModel, clienteViewModel: ClienteViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    var showSelectClientDialog by remember { mutableStateOf(false) }
    var showAddEmployeeDialog by remember { mutableStateOf(false) }

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
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Añadir Nuevo Empleado") },
            text = {
                Column {
                    Button(onClick = {
                        showDialog = false
                        showSelectClientDialog = true
                    }) {
                        Text("Seleccionar Cliente Existente")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = {
                        showDialog = false
                        showAddEmployeeDialog = true
                    }) {
                        Text("Crear Nuevo Empleado")
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (showSelectClientDialog) {
        SelectClientDialog(empleadoViewModel = empleadoViewModel, clienteViewModel = clienteViewModel) {
            showSelectClientDialog = false
        }
    }

    if (showAddEmployeeDialog) {
        AddEmployeeDialog(empleadoViewModel = empleadoViewModel) {
            showAddEmployeeDialog = false
        }
    }
}

@Composable
fun SelectClientDialog(
    empleadoViewModel: EmpleadoViewModel,
    clienteViewModel: ClienteViewModel,
    onDismiss: () -> Unit
) {
    val clientes by clienteViewModel.clientes.observeAsState(emptyList())
    var selectedClient by remember { mutableStateOf<Cliente?>(null) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Seleccionar Cliente Existente") },
        text = {
            Column {
                LazyColumn {
                    items(clientes) { cliente ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedClient = cliente }
                                .padding(8.dp)
                        ) {
                            Text(text = "${cliente.nombre} ${cliente.apellidos}")
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    selectedClient?.let {
                        val newEmployee = Empleado(
                            nombre = it.nombre,
                            apellidos = it.apellidos,
                            telefono = it.telefono,
                            correoElectronico = it.correoElectronico,
                            horariosTrabajo = emptyMap(),
                            citasAsignadas = emptyList()
                        )
                        empleadoViewModel.addEmpleadoAndDeleteCliente(newEmployee, it.id)
                        empleadoViewModel.changeUserRoleToEmpleado(it.id)
                    }
                    onDismiss()
                }
            ) {
                Text("Seleccionar")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancelar")
            }
        }
    )
}

/**
 * Composable que muestra un diálogo para agregar un nuevo empleado.
 *
 * @param empleadoViewModel ViewModel para interactuar con los datos del empleado.
 * @param onDismiss Callback que se llama cuando se cierra el diálogo.
 */
@Composable
fun AddEmployeeDialog(
    empleadoViewModel: EmpleadoViewModel,
    onDismiss: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var correoElectronico by remember { mutableStateOf("") }

    /**
     * Diálogo para agregar un nuevo empleado.
     */
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


/**
 * Composable que muestra un botón para modificar un empleado y controla el diálogo de modificación del empleado.
 *
 * @param empleado El empleado que se modificará.
 * @param empleadoViewModel ViewModel para interactuar con los datos del empleado.
 */
@Composable
fun ModifyEmployeeButton(
    empleado: Empleado,
    empleadoViewModel: EmpleadoViewModel
) {
    var showDialog by remember { mutableStateOf(false) }

    /**
     * Botón que activa el diálogo para modificar el empleado.
     */
    IconButton(onClick = { showDialog = true }) {
        Icon(Icons.Filled.Edit, contentDescription = "Modificar empleado")
    }

    if (showDialog) {
        ModifyEmployeeDialog(empleado = empleado, empleadoViewModel = empleadoViewModel) {
            showDialog = false
        }
    }
}

/**
 * Composable que muestra un diálogo para modificar un empleado.
 *
 * @param empleado El empleado que se modificará.
 * @param empleadoViewModel ViewModel para interactuar con los datos del empleado.
 * @param onDismiss Callback que se llama cuando se cierra el diálogo.
 */
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

    /**
     * Diálogo para modificar los detalles del empleado.
     */
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


/**
 * Composable que muestra un botón para eliminar un empleado y controla el diálogo de confirmación de eliminación.
 *
 * @param empleado El empleado que se eliminará.
 * @param empleadoViewModel ViewModel para interactuar con los datos del empleado.
 */
@Composable
fun DeleteEmployeeButton(
    empleado: Empleado,
    empleadoViewModel: EmpleadoViewModel
) {
    var showDialog by remember { mutableStateOf(false) }

    /**
     * Botón que activa el diálogo de confirmación para eliminar el empleado.
     */
    IconButton(onClick = { showDialog = true }) {
        Icon(Icons.Filled.Delete, contentDescription = "Eliminar empleado")
    }

    if (showDialog) {
        DeleteEmployeeDialog(empleado = empleado, empleadoViewModel = empleadoViewModel) {
            showDialog = false
        }
    }
}

/**
 * Composable que muestra un diálogo de confirmación para eliminar un empleado.
 *
 * @param empleado El empleado que se eliminará.
 * @param empleadoViewModel ViewModel para interactuar con los datos del empleado.
 * @param onDismiss Callback que se llama cuando se cierra el diálogo.
 */
@Composable
fun DeleteEmployeeDialog(
    empleado: Empleado,
    empleadoViewModel: EmpleadoViewModel,
    onDismiss: () -> Unit
) {
    /**
     * Diálogo de confirmación para eliminar el empleado.
     */
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



/*
@Composable
fun EmployeeHistoryButton(navController: NavController, empleadoId: String) {
    IconButton(onClick = { navController.navigate("/employee_history_screen") }) {
        Icon(Icons.Filled.History, contentDescription = "Ver historial de citas")
    }
}
*/

/**
 * Composable que muestra un botón para modificar el horario de trabajo de un empleado y controla el diálogo
 * correspondiente.
 *
 * @param empleado El empleado cuyo horario de trabajo se modificará.
 * @param empleadoViewModel ViewModel para interactuar con los datos del empleado.
 */
@Composable
fun ModifyHorarioButton(
    empleado: Empleado,
    empleadoViewModel: EmpleadoViewModel
) {
    var showDialog by remember { mutableStateOf(false) }

    /**
     * Botón que activa el diálogo de modificación del horario de trabajo del empleado.
     */
    IconButton(onClick = { showDialog = true }) {
        Icon(Icons.Filled.Schedule, contentDescription = "Modificar horario de trabajo")
    }

    if (showDialog) {
        ModifyHorarioDialog(empleado = empleado, empleadoViewModel = empleadoViewModel) {
            showDialog = false
        }
    }
}

/**
 * Composable que muestra un diálogo para modificar el horario de trabajo de un empleado.
 *
 * @param empleado El empleado cuyo horario de trabajo se modificará.
 * @param empleadoViewModel ViewModel para interactuar con los datos del empleado.
 * @param onDismiss Callback que se llama cuando se cierra el diálogo.
 */
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
        empleadoViewModel.fetchEmpleados() // Obtener todos los empleados (si es necesario)
        empleadoViewModel.fetchHorariosTrabajo(empleado.id) // Obtener los horarios para este empleado
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
                        // Manejar el valor potencialmente nulo para horarioTrabajo[selectedDay]
                        val horarioDia = horarioTrabajo?.get(selectedDay) ?: HorariosPorDia()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Mañana", style = MaterialTheme.typography.headlineSmall)
                        HorarioModulo("Mañana", horarioDia.manana) { updatedManana ->
                            horarioTrabajo?.let { // Actualizar solo si horarioTrabajo no es nulo
                                val mutableHorarioTrabajo = it.toMutableMap()
                                mutableHorarioTrabajo[selectedDay] = horarioDia.copy(manana = updatedManana)
                                horarioTrabajo = mutableHorarioTrabajo // Actualizar estado con el mapa modificado
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Tarde", style = MaterialTheme.typography.headlineSmall)
                        HorarioModulo("Tarde", horarioDia.tarde) { updatedTarde ->
                            horarioTrabajo?.let { // Actualizar solo si horarioTrabajo no es nulo
                                val mutableHorarioTrabajo = it.toMutableMap()
                                mutableHorarioTrabajo[selectedDay] = horarioDia.copy(tarde = updatedTarde)
                                horarioTrabajo = mutableHorarioTrabajo // Actualizar estado con el mapa modificado
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
