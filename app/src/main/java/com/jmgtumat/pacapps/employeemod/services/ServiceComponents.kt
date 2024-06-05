package com.jmgtumat.pacapps.employeemod.services

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.jmgtumat.pacapps.data.Servicio
import com.jmgtumat.pacapps.viewmodels.ServicioViewModel


/**
 * Composable que muestra una lista de servicios.
 *
 * @param servicios La lista de servicios a mostrar.
 * @param servicioViewModel El ViewModel utilizado para realizar operaciones en los servicios.
 */
@Composable
fun ServicesList(servicios: List<Servicio>, servicioViewModel: ServicioViewModel) {
    LazyColumn {
        items(servicios) { servicio ->
            ServiceItem(servicio, servicioViewModel)
        }
    }
}

/**
 * Composable que muestra un elemento individual de servicio en la lista.
 *
 * @param servicio El servicio a mostrar.
 * @param servicioViewModel El ViewModel utilizado para realizar operaciones en los servicios.
 */
@Composable
fun ServiceItem(
    servicio: Servicio,
    servicioViewModel: ServicioViewModel
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
                    text = servicio.nombre,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "${servicio.precio}€",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            if (expanded) {
                Text(
                    text = servicio.descripcion,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Duración: ${servicio.duracion} minutos",
                    style = MaterialTheme.typography.bodyMedium
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ModifyServiceButton(servicio, servicioViewModel)
                    DeleteServiceButton(servicio, servicioViewModel)
                }
            }
        }
    }
}

/**
 * Composable que muestra un botón para agregar un nuevo servicio.
 *
 * @param servicioViewModel El ViewModel utilizado para realizar operaciones en los servicios.
 */
@Composable
fun AddServiceButton(
    servicioViewModel: ServicioViewModel
) {
    var showDialog by remember { mutableStateOf(false) }

    Button(
        onClick = { showDialog = true },
        modifier = Modifier.padding(8.dp)
    ) {
        Text("+ Añadir servicio")
    }

    if (showDialog) {
        AddServiceDialog(
            servicioViewModel = servicioViewModel,
            onDismiss = { showDialog = false }
        )
    }
}

/**
 * Composable que muestra un botón para modificar un servicio existente.
 *
 * @param servicio El servicio que se modificará.
 * @param servicioViewModel El ViewModel utilizado para realizar operaciones en los servicios.
 */
@Composable
fun ModifyServiceButton(
    servicio: Servicio,
    servicioViewModel: ServicioViewModel
) {
    var showDialog by remember { mutableStateOf(false) }

    Button(
        onClick = { showDialog = true },
        modifier = Modifier.padding(8.dp)
    ) {
        Text("Modificar servicio")
    }

    if (showDialog) {
        ModifyServiceDialog(
            servicio = servicio,
            servicioViewModel = servicioViewModel,
            onDismiss = { showDialog = false }
        )
    }
}

/**
 * Composable que muestra un botón para eliminar un servicio existente.
 *
 * @param servicio El servicio que se eliminará.
 * @param servicioViewModel El ViewModel utilizado para realizar operaciones en los servicios.
 */
@Composable
fun DeleteServiceButton(
    servicio: Servicio,
    servicioViewModel: ServicioViewModel
) {
    var showDialog by remember { mutableStateOf(false) }

    Button(
        onClick = { showDialog = true },
        modifier = Modifier.padding(8.dp)
    ) {
        Text("Eliminar servicio")
    }

    if (showDialog) {
        DeleteServiceDialog(
            servicio = servicio,
            servicioViewModel = servicioViewModel,
            onDismiss = { showDialog = false }
        )
    }
}

/**
 * Composable que muestra un diálogo para agregar un nuevo servicio.
 *
 * @param servicioViewModel El ViewModel utilizado para realizar operaciones en los servicios.
 * @param onDismiss La acción a realizar cuando se descarta el diálogo.
 */
@Composable
fun AddServiceDialog(
    servicioViewModel: ServicioViewModel,
    onDismiss: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var duracion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            TextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.padding(bottom = 8.dp)
            )
            TextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.padding(bottom = 8.dp)
            )
            TextField(
                value = duracion,
                onValueChange = { duracion = it },
                label = { Text("Duración (minutos)") },
                modifier = Modifier.padding(bottom = 8.dp)
            )
            TextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio") },
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        val servicio = Servicio(
                            nombre = nombre,
                            descripcion = descripcion,
                            duracion = duracion.toInt(),
                            precio = precio.toDouble()
                        )
                        servicioViewModel.insertServicio(servicio)
                        onDismiss()
                    },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("Añadir")
                }
                Button(onClick = onDismiss) {
                    Text("Cancelar")
                }
            }
        }
    }
}

/**
 * Composable que muestra un diálogo para modificar un servicio existente.
 *
 * @param servicio El servicio que se modificará.
 * @param servicioViewModel El ViewModel utilizado para realizar operaciones en los servicios.
 * @param onDismiss La acción a realizar cuando se descarta el diálogo.
 */
@Composable
fun ModifyServiceDialog(
    servicio: Servicio,
    servicioViewModel: ServicioViewModel,
    onDismiss: () -> Unit
) {
    var nombre by remember { mutableStateOf(servicio.nombre) }
    var descripcion by remember { mutableStateOf(servicio.descripcion) }
    var duracion by remember { mutableStateOf(servicio.duracion.toString()) }
    var precio by remember { mutableStateOf(servicio.precio.toString()) }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            TextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.padding(bottom = 8.dp)
            )
            TextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.padding(bottom = 8.dp)
            )
            TextField(
                value = duracion,
                onValueChange = { duracion = it },
                label = { Text("Duración (minutos)") },
                modifier = Modifier.padding(bottom = 8.dp)
            )
            TextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio") },
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        val updatedServicio = servicio.copy(
                            nombre = nombre,
                            descripcion = descripcion,
                            duracion = duracion.toInt(),
                            precio = precio.toDouble()
                        )
                        servicioViewModel.updateServicio(updatedServicio)
                        onDismiss()
                    },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("Actualizar")
                }
                Button(onClick = onDismiss) {
                    Text("Cancelar")
                }
            }
        }
    }
}

/**
 * Composable que muestra un diálogo para eliminar un servicio existente.
 *
 * @param servicio El servicio que se eliminará.
 * @param servicioViewModel El ViewModel utilizado para realizar operaciones en los servicios.
 * @param onDismiss La acción a realizar cuando se descarta el diálogo.
 */
@Composable
fun DeleteServiceDialog(
    servicio: Servicio,
    servicioViewModel: ServicioViewModel,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text("¿Estás seguro de que deseas eliminar el servicio ${servicio.nombre}?")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ) {
                Button(
                    onClick = {
                        servicioViewModel.deleteServicio(servicio.id)
                        onDismiss()
                    },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("Eliminar")
                }
                Button(onClick = onDismiss) {
                    Text("Cancelar")
                }
            }
        }
    }
}
