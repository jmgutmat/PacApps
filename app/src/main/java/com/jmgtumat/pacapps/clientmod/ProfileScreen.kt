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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jmgtumat.pacapps.data.Cliente
import com.jmgtumat.pacapps.util.validateProfileInput
import com.jmgtumat.pacapps.viewmodels.ClienteViewModel

@Composable
fun ProfileScreen(viewModel: ClienteViewModel = viewModel()) {
    val clienteList by viewModel.clientes.observeAsState(emptyList())
    val currentCliente = clienteList.firstOrNull() ?: Cliente()

    var nombre by remember { mutableStateOf(currentCliente.nombre) }
    var apellidos by remember { mutableStateOf(currentCliente.apellidos) }
    var telefono by remember { mutableStateOf(currentCliente.telefono) }
    var correoElectronico by remember { mutableStateOf(currentCliente.correoElectronico) }
    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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

        OutlinedTextField(
            value = correoElectronico,
            onValueChange = { correoElectronico = it },
            label = { Text("Correo electrónico") }
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation()
        )

        Button(onClick = {
            val isValid = validateProfileInput(nombre, apellidos, telefono, correoElectronico, password)
            if (isValid.isValid) {
                val updatedCliente = currentCliente.copy(
                    nombre = nombre,
                    apellidos = apellidos,
                    telefono = telefono,
                    correoElectronico = correoElectronico
                )
                viewModel.updateCliente(updatedCliente)
            } else {
                showError = true
                errorMessage = isValid.errorMessage
            }
        }) {
            Text("Actualizar Datos")
        }

        if (showError) {
            Text(errorMessage, color = Color.Red)
        }
    }
}