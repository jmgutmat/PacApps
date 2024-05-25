package com.jmgtumat.pacapps.viewmodels

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.jmgtumat.pacapps.data.Cliente
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GoogleSignInViewModel : BaseViewModel() {

    private val _user = MutableStateFlow<Cliente?>(null)
    val user: StateFlow<Cliente?> = _user

    fun signInWithGoogle(idToken: String, onComplete: (Cliente?) -> Unit) {
        val auth = FirebaseAuth.getInstance()
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = auth.currentUser
                    val cliente = firebaseUser?.let {
                        Cliente(
                            id = it.uid,
                            nombre = it.displayName ?: "",
                            apellidos = "",
                            telefono = "",
                            correoElectronico = it.email ?: "",
                            historialCitas = emptyList()
                        )
                    }
                    _user.value = cliente
                    onComplete(cliente)
                } else {
                    setError("Error signing in with Google")
                    onComplete(null)
                }
            }
    }
}
