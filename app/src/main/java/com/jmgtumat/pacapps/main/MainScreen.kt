package com.jmgtumat.pacapps.main

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.jmgtumat.pacapps.R
import com.jmgtumat.pacapps.navigation.AppScreens
import com.jmgtumat.pacapps.navigation.redirectToRoleBasedScreen
import com.jmgtumat.pacapps.viewmodels.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun MainScreen(navController: NavController) {

    val viewModel = MainViewModel()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }


    val googleSignInClient = remember { getGoogleSignInClient(context) }
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account.idToken
            if (idToken != null) {
                coroutineScope.launch {
                    val authResult = viewModel.signInWithGoogle(idToken)
                    if (authResult.isSuccess) {
                        val userId = authResult.getOrNull()
                        userId?.let {
                            redirectToRoleBasedScreen(navController, it, viewModel::getUserType)
                        }
                    } else {
                        errorMessage = authResult.exceptionOrNull()?.message ?: "Error al iniciar sesión con Google"
                    }
                }
            }
        } catch (e: ApiException) {
            errorMessage = e.localizedMessage ?: "Error al obtener el ID Token de Google"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp), // Ajusta la altura deseada
            contentAlignment = Alignment.TopCenter // Alinea el contenido en la parte superior

        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_blanco),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter),
                contentScale = ContentScale.Crop // Ajusta la escala para que la imagen se recorte según el tamaño del contenedor
            )
        }

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(
                        imageVector = if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = "Toggle password visibility"
                    )
                }
            }
        )

        Button(
            onClick = {
                coroutineScope.launch {
                    val result = viewModel.signInWithEmailAndPassword(email, password)
                    if (result.isSuccess) {
                        val userId = result.getOrNull()
                        userId?.let {
                            redirectToRoleBasedScreen(navController, it, viewModel::getUserType)
                        }
                    } else {
                        errorMessage = result.exceptionOrNull()?.message ?: "Error al iniciar sesión"
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Iniciar Sesión")
        }

        Button(
            onClick = {
                navController.navigate(AppScreens.EmailSignUpScreen.route)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Text("Registrarse")
        }

        IconButton(
            onClick = {
                val signInIntent = googleSignInClient.signInIntent
                googleSignInLauncher.launch(signInIntent)
            },
            modifier = Modifier
                .padding(vertical = 8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_google), // Agregar el icono de Google
                contentDescription = "Google"
            )
        }
        if (errorMessage.isNotBlank()) {
            Text(text = errorMessage, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
        }
    }
}

fun getGoogleSignInClient(context: Context): GoogleSignInClient {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()
    return GoogleSignIn.getClient(context, gso)
}
