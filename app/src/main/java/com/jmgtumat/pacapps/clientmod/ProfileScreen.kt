package com.jmgtumat.pacapps.clientmod

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.jmgtumat.pacapps.data.Cliente
import com.jmgtumat.pacapps.repository.ClienteRepository
import com.jmgtumat.pacapps.viewmodels.ClienteViewModel
import com.jmgtumat.pacapps.viewmodels.ClienteViewModelFactory

/**
 * Pantalla para mostrar y editar el perfil del cliente.
 *
 * @param navController El controlador de navegación.
 */
@Composable
fun ProfileScreen(navController: NavController) {
    // ViewModel para obtener y actualizar información del cliente
    val clienteViewModel: ClienteViewModel = viewModel(
        factory = ClienteViewModelFactory(
            ClienteRepository(),
        )
    )

    // Estado del cliente y sus datos actuales
    val clienteList by clienteViewModel.clientes.observeAsState(emptyList())
    val currentCliente = clienteList.firstOrNull() ?: Cliente()

    // Estados para los campos del perfil del cliente
    var nombre by remember { mutableStateOf(currentCliente.nombre) }
    var apellidos by remember { mutableStateOf(currentCliente.apellidos) }
    var telefono by remember { mutableStateOf(currentCliente.telefono) }

    // Mostrar el contenido en el dashboard del cliente
    ClienteDashboard(navController = navController) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Campos de entrada para editar el perfil del cliente
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") }
            )

            OutlinedTextField(
                value = apellidos,
                onValueChange = { apellidos = it },
                label = { Text("Apellidos") }
            )

            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono") }
            )

            // Botón para actualizar los datos del cliente
            Button(onClick = {
                val updatedCliente = currentCliente.copy(
                    nombre = nombre,
                    apellidos = apellidos,
                    telefono = telefono
                )
                clienteViewModel.updateCliente(updatedCliente)
            }) {
                Text("Actualizar Datos")
            }

            // Botón para cerrar la sesión del cliente
            Button(
                onClick = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate("/main_screen") {
                        popUpTo(0) // Limpiar la pila de retroceso
                    }
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Cerrar Sesión")
            }
        }
    }
}
