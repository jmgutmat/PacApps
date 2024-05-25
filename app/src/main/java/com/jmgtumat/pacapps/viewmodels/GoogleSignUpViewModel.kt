package com.jmgtumat.pacapps.viewmodels

import com.google.firebase.auth.FirebaseAuth
import com.jmgtumat.pacapps.data.Cliente
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GoogleSignUpViewModel : BaseViewModel() {

    private val _user = MutableStateFlow<Cliente?>(null)
    val user: StateFlow<Cliente?> = _user

    fun signUpWithGoogle(email: String, password: String, onComplete: (Cliente?) -> Unit) {
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = auth.currentUser
                    val cliente = firebaseUser?.let {
                        Cliente(
                            id = it.uid,
                            nombre = "",
                            apellidos = "",
                            telefono = "",
                            correoElectronico = it.email ?: "",
                            historialCitas = emptyList()
                        )
                    }
                    _user.value = cliente
                    onComplete(cliente)
                } else {
                    setError("Error signing up with Google")
                    onComplete(null)
                }
            }
    }
}
