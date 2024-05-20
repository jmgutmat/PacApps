package com.jmgtumat.pacapps.main

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.jmgtumat.pacapps.R.drawable.ic_facebook
import com.jmgtumat.pacapps.R.drawable.ic_google
import com.jmgtumat.pacapps.R.drawable.inicio
import com.jmgtumat.pacapps.navigation.AppScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val context = LocalContext.current
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }

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
                    // Lógica de inicio de sesión
                    Toast.makeText(context, "Iniciar sesión con ${emailState.value} y ${passwordState.value}", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Iniciar Sesión")
            }

            // Botón de registrarse
            OutlinedButton(
                onClick = {
                    // Lógica de registro
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrarse")
            }

            // Botones de inicio de sesión con Google y Facebook usando recursos de íconos
            Button(
                onClick = {
                    // Lógica de inicio de sesión con Google
                    Toast.makeText(context, "Iniciar sesión con Google", Toast.LENGTH_SHORT).show()
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

            Button(
                onClick = {
                    // Lógica de inicio de sesión con Facebook
                    Toast.makeText(context, "Iniciar sesión con Facebook", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = ic_facebook),
                    contentDescription = "Facebook Icon",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Iniciar sesión con Facebook")
            }

            // Botón para ver servicios
            OutlinedButton(
                onClick = {
                    navController.navigate(AppScreens.ViewServicesScreen.route)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver Servicios")
            }

            // Botón para ver la dirección
            OutlinedButton(
                onClick = {
                    // Navegar a la pantalla de dirección
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Dirección")
            }

            // Botón para contacto
            OutlinedButton(
                onClick = {
                    // Navegar a la pantalla de contacto
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Contacto")
            }
        }
    }
}
