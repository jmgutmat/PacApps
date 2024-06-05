package com.jmgtumat.pacapps.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jmgtumat.pacapps.navigation.redirectToRoleBasedScreen
import com.jmgtumat.pacapps.util.validateInputFields
import com.jmgtumat.pacapps.viewmodels.EmailSignUpViewModel

/**
 * [EmailSignUpScreen] es una pantalla de registro por correo electrónico que permite al usuario
 * ingresar su información personal y registrarse en la aplicación.
 *
 * @param navController el controlador de navegación para la navegación dentro de la aplicación.
 */
@Composable
fun EmailSignUpScreen(navController: NavController) {
    // Obtiene el ViewModel de EmailSignUpViewModel
    val viewModel: EmailSignUpViewModel = viewModel()

    // Estados para los campos de texto y mensajes de error
    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var correoElectronico by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Callback para obtener el tipo de usuario
    val getUserType: (String, (Boolean) -> Unit) -> Unit = { userId, callback ->
        viewModel.getUserType(userId, callback)
    }

    // Estados para la visibilidad de las contraseñas
    var passwordVisibility by remember { mutableStateOf(false) }
    var confirmPasswordVisibility by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()), // Permite desplazamiento vertical
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Campos de texto para la información personal del usuario
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

        // Campos de texto para las contraseñas con botones para mostrar/ocultar las contraseñas
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(
                    onClick = { passwordVisibility = !passwordVisibility },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = "Toggle password visibility"
                    )
                }
            }
        )
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirmar Contraseña") },
            visualTransformation = if (confirmPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(
                    onClick = { confirmPasswordVisibility = !confirmPasswordVisibility },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = if (confirmPasswordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = "Toggle password visibility"
                    )
                }
            }
        )

        // Botón para registrar al usuario con su correo electrónico y contraseña
        Button(
            onClick = {
                val validationResult = validateInputFields(correoElectronico, password)
                if (validationResult.isValid && password == confirmPassword) {
                    viewModel.signUpWithEmailAndPassword(nombre, apellidos, telefono, correoElectronico, password) { firebaseUser ->
                        firebaseUser?.let {
                            redirectToRoleBasedScreen(navController, it.uid, getUserType)
                        } ?: run {
                            showError = true
                            errorMessage = "Error al registrar"
                        }
                    }
                } else {
                    showError = true
                    errorMessage = if (!validationResult.isValid) validationResult.errorMessage
                    else "Las contraseñas no coinciden"
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Registrarse con correo electrónico")
        }

        // Muestra un mensaje de error si es necesario
        if (showError) {
            Text(errorMessage, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
        }
    }
}
