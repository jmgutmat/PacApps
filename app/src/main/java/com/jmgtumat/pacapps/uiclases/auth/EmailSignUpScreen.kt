package com.jmgtumat.pacapps.uiclases.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.jmgtumat.pacapps.data.Cliente
import com.jmgtumat.pacapps.util.validateInputFields
import com.jmgtumat.pacapps.viewmodels.EmailSignUpViewModel

@Composable
fun EmailSignUpScreen(viewModel: EmailSignUpViewModel) {
    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var correoElectronico by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
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
            val validationResult = validateInputFields(correoElectronico, password)
            if (validationResult.isValid) {
                val cliente = Cliente(
                    nombre = nombre,
                    apellidos = apellidos,
                    telefono = telefono,
                    correoElectronico = correoElectronico,
                    historialCitas = emptyList()  // Inicia el historial de citas vacío
                )

                viewModel.signUpWithEmailAndPassword(cliente, password)
            } else {
                showError = true
                errorMessage = validationResult.errorMessage
            }
        }) {
            Text("Registrarse con correo electrónico")
        }

        if (showError) {
            Text(errorMessage, color = Color.Red)
        }
    }
}

