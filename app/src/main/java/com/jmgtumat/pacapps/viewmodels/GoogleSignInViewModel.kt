package com.jmgtumat.pacapps.viewmodels

import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jmgtumat.pacapps.data.Cliente
import com.jmgtumat.pacapps.data.User
import com.jmgtumat.pacapps.data.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GoogleSignInViewModel : BaseViewModel() {

    private val _user = MutableStateFlow<Cliente?>(null)
    val user: StateFlow<Cliente?> = _user
    private val db: DatabaseReference = FirebaseDatabase.getInstance().reference

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

    fun fetchUserRole(userId: String, onRoleFetched: (UserRole) -> Unit) {
        viewModelScope.launch {
            setLoading()
            db.child("users").child(userId).get().addOnSuccessListener { dataSnapshot ->
                val user = dataSnapshot.getValue(User::class.java)
                val role = user?.rol ?: UserRole.CLIENTE
                onRoleFetched(role)
                setSuccess()
            }.addOnFailureListener {
                setError("Error fetching user role")
            }
        }
    }
}
