package com.jmgtumat.pacapps.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController

@Composable
fun MainScreen(navController: NavHostController) {
    var showDialog by remember { mutableStateOf(false) }
    var isLogin by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Bienvenido a la App",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Button(
            onClick = {
                isLogin = true
                showDialog = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Iniciar Sesión")
        }

        Button(
            onClick = {
                isLogin = false
                showDialog = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Registrarse")
        }

        if (showDialog) {
            SelectionDialog(
                isLogin = isLogin,
                onDismiss = { showDialog = false },
                onSelectOption = { option ->
                    when (option) {
                        "Correo" -> {
                            if (isLogin) {
                                navController.navigate("loginWithEmail")
                            } else {
                                navController.navigate("registerWithEmail")
                            }
                        }
                        "Google" -> {
                            if (isLogin) {
                                navController.navigate("loginWithGoogle")
                            } else {
                                navController.navigate("registerWithGoogle")
                            }
                        }
                    }
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun SelectionDialog(
    isLogin: Boolean,
    onDismiss: () -> Unit,
    onSelectOption: (String) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = if (isLogin) "Iniciar Sesión" else "Registrarse",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Button(
                    onClick = { onSelectOption("Correo") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(if (isLogin) "Con Correo" else "Registrar con Correo")
                }

                Button(
                    onClick = { onSelectOption("Google") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(if (isLogin) "Con Google" else "Registrar con Google")
                }

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Cancelar")
                }
            }
        }
    }
}
