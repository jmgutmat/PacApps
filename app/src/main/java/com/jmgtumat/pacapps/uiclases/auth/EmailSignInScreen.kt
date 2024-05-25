package com.jmgtumat.pacapps.uiclases.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavHostController
import com.jmgtumat.pacapps.navigation.redirectToRoleBasedScreen
import com.jmgtumat.pacapps.util.validateInputFields
import com.jmgtumat.pacapps.viewmodels.EmailSignInViewModel

@Composable
fun EmailSignInScreen(viewModel: EmailSignInViewModel, navController: NavHostController) {
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
                viewModel.signInWithEmailAndPassword(correoElectronico, password)
            } else {
                showError = true
                errorMessage = validationResult.errorMessage
            }
        }) {
            Text("Iniciar sesión con correo electrónico")
        }

        if (showError) {
            Text(errorMessage, color = Color.Red)
        }
    }

    LaunchedEffect(viewModel.user) {
        viewModel.user.collect { firebaseUser ->
            firebaseUser?.let {
                redirectToRoleBasedScreen(navController, it.uid, viewModel)
            }
        }
    }
}

