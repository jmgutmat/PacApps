package com.jmgtumat.pacapps.viewmodels

import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jmgtumat.pacapps.data.Cliente
import kotlinx.coroutines.launch

class EmailSignUpViewModel : BaseViewModel() {

    private val db: DatabaseReference = FirebaseDatabase.getInstance().reference

    fun signUpWithEmailAndPassword(cliente: Cliente, password: String) {
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(cliente.correoElectronico, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = auth.currentUser
                    val userId = firebaseUser?.uid ?: ""
                    val newCliente = cliente.copy(id = userId)

                    viewModelScope.launch {
                        setLoading()
                        db.child("users").child(userId).setValue(newCliente)
                            .addOnSuccessListener {
                                setSuccess()
                            }
                            .addOnFailureListener {
                                setError("Error saving user data")
                            }
                    }
                } else {
                    setError("Error signing up")
                }
            }
    }
}
