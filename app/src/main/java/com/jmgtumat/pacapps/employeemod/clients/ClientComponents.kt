package com.jmgtumat.pacapps.employeemod.clients

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.jmgtumat.pacapps.navigation.AppScreens
import com.jmgtumat.pacapps.viewmodels.ClienteViewModel

/**
 * Composable que muestra un elemento de cliente en una lista.
 *
 * @param cliente El cliente a mostrar.
 * @param clienteViewModel El ViewModel del cliente.
 * @param navController El controlador de navegación.
 */
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
                    horizontalArrangement = Arrangement.SpaceBetween,
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

/**
 * Composable que muestra un botón para añadir un nuevo cliente.
 *
 * @param navController El controlador de navegación.
 * @param clienteViewModel El ViewModel del cliente.
 */
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

/**
 * Composable que muestra un diálogo para añadir un nuevo cliente.
 *
 * @param clienteViewModel El ViewModel del cliente.
 * @param onDismiss La acción a realizar al cerrar el diálogo.
 */
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

/**
 * Filtra la lista de clientes según la consulta proporcionada.
 *
 * @param clientes La lista de clientes a filtrar.
 * @param query La consulta de búsqueda.
 * @return La lista filtrada de clientes.
 */

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

/**
 * Composable que muestra un botón para modificar un cliente.
 *
 * @param cliente El cliente a modificar.
 * @param clienteViewModel El ViewModel del cliente.
 */
@Composable
fun ModifyClientButton(
    cliente: Cliente,
    clienteViewModel: ClienteViewModel
) {
    var showDialog by remember { mutableStateOf(false) }

    IconButton(onClick = { showDialog = true }) {
        Icon(Icons.Filled.Edit, contentDescription = "Modificar cliente")
    }

    if (showDialog) {
        ModifyClientDialog(cliente = cliente, clienteViewModel = clienteViewModel) {
            showDialog = false
        }
    }
}

/**
 * Composable que muestra un diálogo para modificar un cliente existente.
 *
 * @param cliente El cliente a modificar.
 * @param clienteViewModel El ViewModel del cliente.
 * @param onDismiss La acción a realizar al cerrar el diálogo.
 */
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

/**
 * Composable que muestra un botón para eliminar un cliente.
 *
 * @param cliente El cliente a eliminar.
 * @param clienteViewModel El ViewModel del cliente.
 */
@Composable
fun DeleteClientButton(
    cliente: Cliente,
    clienteViewModel: ClienteViewModel
) {
    var showDialog by remember { mutableStateOf(false) }

    IconButton(onClick = { showDialog = true }) {
        Icon(Icons.Filled.Delete, contentDescription = "Eliminar cliente")
    }

    if (showDialog) {
        DeleteClientDialog(cliente = cliente, clienteViewModel = clienteViewModel) {
            showDialog = false
        }
    }
}

/**
 * Composable que muestra un diálogo para confirmar la eliminación de un cliente.
 *
 * @param cliente El cliente a eliminar.
 * @param clienteViewModel El ViewModel del cliente.
 * @param onDismiss La acción a realizar al cerrar el diálogo.
 */
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

/**
 * Composable que muestra un botón para ver el historial de un cliente.
 *
 * @param cliente El cliente del cual ver el historial.
 * @param navController El controlador de navegación.
 */
@Composable
fun ViewHistoryButton(
    cliente: Cliente,
    navController: NavController
) {
    IconButton(
        onClick = {
            Log.d("ClientId", "ClienteId: ${cliente.id}")

            navController.currentBackStackEntry?.arguments?.putString("clienteId", cliente.id)
            navController.navigate("${AppScreens.EmpModHistoryScreen.route}/${cliente.id}") {
                launchSingleTop = true
                restoreState = true
            }
        }
    ) {
        Icon(Icons.Filled.History, contentDescription = "Ver Historial")
    }
}