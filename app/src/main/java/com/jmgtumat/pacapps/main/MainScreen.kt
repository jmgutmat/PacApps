package com.jmgtumat.pacapps.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.jmgtumat.pacapps.R.drawable.ic_google
import com.jmgtumat.pacapps.R.drawable.inicio
import com.jmgtumat.pacapps.navigation.AppScreens
import com.jmgtumat.pacapps.util.AuthManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController, launchGoogleSignIn: () -> Unit) {
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val authManager = remember { AuthManager(navController) }

    Scaffold {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Imagen de cabecera
            Image(
                painter = painterResource(id = inicio),
                contentDescription = "Header Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            // Mensaje de bienvenida
            Text(
                text = "Bienvenido, regístrate o inicia sesión para poder obtener una cita",
                fontSize = 20.sp
            )

            // Campo de texto para el correo electrónico
            OutlinedTextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                label = { Text("Correo Electrónico") },
                modifier = Modifier.fillMaxWidth()
            )

            // Campo de texto para la contraseña
            OutlinedTextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth()
            )

            // Botón de iniciar sesión
            Button(
                onClick = {
                    authManager.signInWithEmailAndPassword(
                        email = emailState.value,
                        password = passwordState.value
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Iniciar Sesión")
            }

            // Botón de registrarse
            OutlinedButton(
                onClick = {
                    navController.navigate(AppScreens.RegisterScreen.route)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrarse")
            }

            // Botón de inicio de sesión con Google
            Button(
                onClick = {
                    launchGoogleSignIn()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = ic_google),
                    contentDescription = "Google Icon",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Iniciar sesión con Google")
            }
        }
    }
}
