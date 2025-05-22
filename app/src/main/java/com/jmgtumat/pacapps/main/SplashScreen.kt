package com.jmgtumat.pacapps.main

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.jmgtumat.pacapps.R
import com.jmgtumat.pacapps.navigation.AppScreens
import com.jmgtumat.pacapps.ui.theme.PacAppsTheme

/**
 * Pantalla de presentación que se muestra al inicio de la aplicación.
 * @param navController Controlador de navegación para gestionar las transiciones entre pantallas.
 */
@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            navController.navigate(AppScreens.MainScreen.route) {
                popUpTo(AppScreens.SplashScreen.route) { inclusive = true }
            }
        } else {
            val uid = user.uid
            val db = FirebaseDatabase.getInstance().reference

            val clienteRef = db.child("clientes").child(uid)
            val empleadoRef = db.child("empleados").child(uid)
            val adminRef = db.child("administradores").child(uid)

            clienteRef.get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    navController.navigate(AppScreens.ClientHomeScreen.route) {
                        popUpTo(AppScreens.SplashScreen.route) { inclusive = true }
                    }
                } else {
                    empleadoRef.get().addOnSuccessListener { snapshotEmp ->
                        if (snapshotEmp.exists()) {
                            navController.navigate(AppScreens.ManageAppointmentsScreen.route) {
                                popUpTo(AppScreens.SplashScreen.route) { inclusive = true }
                            }
                        } else {
                            adminRef.get().addOnSuccessListener { snapshotAdmin ->
                                if (snapshotAdmin.exists()) {
                                    navController.navigate(AppScreens.ReportsScreen.route) {
                                        popUpTo(AppScreens.SplashScreen.route) { inclusive = true }
                                    }
                                } else {
                                    navController.navigate(AppScreens.MainScreen.route) {
                                        popUpTo(AppScreens.SplashScreen.route) { inclusive = true }
                                    }
                                }
                            }
                        }
                    }
                }
            }.addOnFailureListener {
                navController.navigate(AppScreens.MainScreen.route) {
                    popUpTo(AppScreens.SplashScreen.route) { inclusive = true }
                }
            }
        }
    }


    Splash()
}

/**
 * Componente de composición para la pantalla de presentación.
 */
@Composable
fun Splash() {
    Image(
        painter = painterResource(id = R.drawable.inicio),
        contentDescription = "Imagen inicio",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}

/**
 * Vista previa de la pantalla de presentación.
 */
@Preview(
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
    device = "id:Nexus One"
)
@Composable
fun SplashScreenPreview() {
    PacAppsTheme() {
        Splash()
    }
}
