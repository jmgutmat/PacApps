package com.jmgtumat.pacapps.employeemod.clients

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
import com.jmgtumat.pacapps.data.Cliente
import com.jmgtumat.pacapps.viewmodels.ClienteViewModel

@Composable
fun ClientItem(
    cliente: Cliente,
    clienteViewModel: ClienteViewModel,
    navController: NavController
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
                    text = "${cliente.nombre} ${cliente.apellidos}",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "Teléfono: ${cliente.telefono}",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            if (expanded) {
                Text(
                    text = "Correo Electrónico: ${cliente.correoElectronico}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ViewHistoryButton(cliente, navController)
                    ModifyClientButton(cliente, clienteViewModel)
                    DeleteClientButton(cliente, clienteViewModel)
                }
            }
        }
    }
}




@Composable
fun AddClientButton(navController: NavController, clienteViewModel: ClienteViewModel) {
    var showDialog by remember { mutableStateOf(false) }

    FloatingActionButton(
        onClick = { showDialog = true },
        modifier = Modifier.padding(16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Nuevo Cliente",
            tint = MaterialTheme.colorScheme.onSurface
        )
    }

    if (showDialog) {
        AddClientDialog(clienteViewModel = clienteViewModel) {
            showDialog = false
        }
    }
}

@Composable
fun AddClientDialog(
    clienteViewModel: ClienteViewModel,
    onDismiss: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var correoElectronico by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Añadir Nuevo Cliente") },
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
                    val newClient = Cliente(
                        nombre = nombre,
                        apellidos = apellidos,
                        telefono = telefono,
                        correoElectronico = correoElectronico
                    )
                    clienteViewModel.insertCliente(newClient)
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
fun filterClientes(clientes: List<Cliente>, query: String): List<Cliente> {
    return if (query.isEmpty()) {
        clientes
    } else {
        clientes.filter { cliente ->
            cliente.nombre.contains(query, ignoreCase = true) ||
                    cliente.apellidos.contains(query, ignoreCase = true) ||
                    cliente.telefono.contains(query, ignoreCase = true) ||
                    cliente.correoElectronico.contains(query, ignoreCase = true)
        }
    }
}

@Composable
fun ModifyClientButton(
    cliente: Cliente,
    clienteViewModel: ClienteViewModel
) {
    var showDialog by remember { mutableStateOf(false) }

    Button(onClick = { showDialog = true }) {
        Text("Modificar")
    }

    if (showDialog) {
        ModifyClientDialog(cliente = cliente, clienteViewModel = clienteViewModel) {
            showDialog = false
        }
    }
}

@Composable
fun ModifyClientDialog(
    cliente: Cliente,
    clienteViewModel: ClienteViewModel,
    onDismiss: () -> Unit
) {
    var nombre by remember { mutableStateOf(cliente.nombre) }
    var apellidos by remember { mutableStateOf(cliente.apellidos) }
    var telefono by remember { mutableStateOf(cliente.telefono) }
    var correoElectronico by remember { mutableStateOf(cliente.correoElectronico) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Modificar Cliente") },
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
                    val updatedClient = cliente.copy(
                        nombre = nombre,
                        apellidos = apellidos,
                        telefono = telefono,
                        correoElectronico = correoElectronico
                    )
                    clienteViewModel.updateCliente(updatedClient)
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
fun DeleteClientButton(
    cliente: Cliente,
    clienteViewModel: ClienteViewModel
) {
    var showDialog by remember { mutableStateOf(false) }

    Button(onClick = { showDialog = true }) {
        Text("Eliminar")
    }

    if (showDialog) {
        DeleteClientDialog(cliente = cliente, clienteViewModel = clienteViewModel) {
            showDialog = false
        }
    }
}

@Composable
fun DeleteClientDialog(
    cliente: Cliente,
    clienteViewModel: ClienteViewModel,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Eliminar Cliente") },
        text = {
            Column {
                Text("Nombre: ${cliente.nombre}")
                Text("Apellidos: ${cliente.apellidos}")
                Text("Teléfono: ${cliente.telefono}")
                Text("Correo Electrónico: ${cliente.correoElectronico}")
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    clienteViewModel.deleteCliente(cliente.id)
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
fun ViewHistoryButton(
    cliente: Cliente,
    navController: NavController
) {
    Button(onClick = { navController.navigate("history/${cliente.id}") }) {
        Text("Ver Historial")
    }
}
