package com.jmgtumat.pacapps.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavHostController
import com.jmgtumat.pacapps.navigation.redirectToRoleBasedScreen
import com.jmgtumat.pacapps.util.validateEmail
import com.jmgtumat.pacapps.util.validateInputFields
import com.jmgtumat.pacapps.util.validatePassword
import com.jmgtumat.pacapps.viewmodels.GoogleSignUpViewModel

@Composable
fun GoogleSignUpScreen(viewModel: GoogleSignUpViewModel, navController: NavHostController) {
    val emailState = remember { mutableStateOf(TextFieldValue("")) }
    val passwordState = remember { mutableStateOf(TextFieldValue("")) }
    val emailErrorState = remember { mutableStateOf("") }
    val passwordErrorState = remember { mutableStateOf("") }

    Column {
        OutlinedTextField(
            value = emailState.value,
            onValueChange = {
                emailState.value = it
                emailErrorState.value = validateEmail(it.text).toString()
            },
            label = { Text("Correo electrónico") }
        )
        if (emailErrorState.value.isNotBlank()) {
            Text(text = emailErrorState.value, color = Color.Red)
        }

        OutlinedTextField(
            value = passwordState.value,
            onValueChange = {
                passwordState.value = it
                passwordErrorState.value = validatePassword(it.text).toString()
            },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation()
        )
        if (passwordErrorState.value.isNotBlank()) {
            Text(text = passwordErrorState.value, color = Color.Red)
        }

        Button(onClick = {
            val validationResult = validateInputFields(emailState.value.text, passwordState.value.text)
            if (validationResult.isValid) {
                viewModel.signUpWithGoogle(emailState.value.text, passwordState.value.text) { user ->
                    user?.let {
                        redirectToRoleBasedScreen(navController, it.id) { userId, callback ->
                            viewModel.fetchUserRole(userId, callback)
                        }
                    }
                }
            }
        }) {
            Text("Registrarse con Google")
        }
    }
}
