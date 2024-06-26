//package com.jmgtumat.pacapps.auth
//
//import androidx.compose.foundation.layout.Column
//import androidx.compose.material3.Button
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.navigation.NavHostController
//import com.jmgtumat.pacapps.navigation.redirectToRoleBasedScreen
//import com.jmgtumat.pacapps.util.validateInputFields
//import com.jmgtumat.pacapps.viewmodels.GoogleSignInViewModel
//
//@Composable
//fun GoogleSignInScreen(viewModel: GoogleSignInViewModel, navController: NavHostController) {
//    val emailState = remember { mutableStateOf("") }
//    val passwordState = remember { mutableStateOf("") }
//    val showError = remember { mutableStateOf(false) }
//    val errorMessage = remember { mutableStateOf("") }
//
//    Column {
//        OutlinedTextField(
//            value = emailState.value,
//            onValueChange = { emailState.value = it },
//            label = { Text("Correo electrónico") }
//        )
//
//        OutlinedTextField(
//            value = passwordState.value,
//            onValueChange = { passwordState.value = it },
//            label = { Text("Contraseña") },
//            visualTransformation = PasswordVisualTransformation()
//        )
//
//        Button(onClick = {
//            val validationResult = validateInputFields(emailState.value, passwordState.value)
//            if (validationResult.isValid) {
//                viewModel.signInWithGoogle("YOUR_ID_TOKEN") { user ->
//                    user?.let {
//                        redirectToRoleBasedScreen(navController, it.uid, viewModel::getUserType)
//                    }
//                }
//            } else {
//                showError.value = true
//                errorMessage.value = validationResult.errorMessage
//            }
//        }) {
//            Text("Iniciar sesión con Google")
//        }
//
//        if (showError.value) {
//            Text(errorMessage.value)
//        }
//    }
//}
