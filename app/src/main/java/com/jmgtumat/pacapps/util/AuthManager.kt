package com.jmgtumat.pacapps.util

import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.jmgtumat.pacapps.data.Cliente
import com.jmgtumat.pacapps.navigation.AppScreens

class AuthManager(private val navController: NavHostController) {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    fun signInWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                user?.let {
                    val userId = it.uid
                    database.getReference("roles").child(userId).get().addOnSuccessListener { dataSnapshot ->
                        if (dataSnapshot.exists()) {
                            val role = dataSnapshot.value as String
                            if (role == "client") {
                                navController.navigate(AppScreens.ClientScreen.route)
                            } else if (role == "employee") {
                                navController.navigate(AppScreens.EmployeeScreen.route)
                            }
                        } else {
                            // Usuario nuevo, se asume que es un cliente
                            val cliente = Cliente(
                                id = userId,
                                nombre = it.displayName ?: "",
                                apellidos = "",
                                telefono = "",
                                correoElectronico = it.email ?: "",
                                historialCitas = emptyList()
                            )
                            saveUserToDatabase(cliente)
                            navController.navigate(AppScreens.ClientScreen.route)
                        }
                    }
                }
            } else {
                // Manejo del error de autenticación
            }
        }
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                user?.let {
                    val userId = it.uid
                    database.getReference("roles").child(userId).get().addOnSuccessListener { dataSnapshot ->
                        if (dataSnapshot.exists()) {
                            val role = dataSnapshot.value as String
                            if (role == "client") {
                                navController.navigate(AppScreens.ClientScreen.route)
                            } else if (role == "employee") {
                                navController.navigate(AppScreens.EmployeeScreen.route)
                            }
                        }
                    }
                }
            } else {
                // Manejo del error de autenticación
            }
        }
    }

    private fun saveUserToDatabase(cliente: Cliente) {
        val user = auth.currentUser
        user?.let { it ->
            val userRef = database.getReference("clients").child(it.uid)
            userRef.setValue(cliente).addOnSuccessListener {
                database.getReference("roles").child(it.uid).setValue("client")
            }
        }
    }
}
