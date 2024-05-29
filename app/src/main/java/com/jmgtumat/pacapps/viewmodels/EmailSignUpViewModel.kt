package com.jmgtumat.pacapps.viewmodels

import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jmgtumat.pacapps.data.Cliente
import com.jmgtumat.pacapps.data.User
import com.jmgtumat.pacapps.data.UserRole
import kotlinx.coroutines.launch

class EmailSignUpViewModel : BaseViewModel() {

    private val db: DatabaseReference = FirebaseDatabase.getInstance().reference

    fun signUpWithEmailAndPassword(nombre: String, apellidos: String, telefono: String, correoElectronico: String, password: String, onComplete: (FirebaseUser?) -> Unit) {
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(correoElectronico, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = auth.currentUser
                    val userId = firebaseUser?.uid ?: ""
                    val cliente = Cliente(
                        id = userId,
                        nombre = nombre,
                        apellidos = apellidos,
                        telefono = telefono,
                        correoElectronico = correoElectronico,
                        historialCitas = emptyList()
                    )

                    viewModelScope.launch {
                        setLoading()
                        db.child("clientes").child(userId).setValue(cliente)
                            .addOnSuccessListener {
                                setSuccess()
                                onComplete(firebaseUser)
                            }
                            .addOnFailureListener { exception ->
                                setError("Error saving user data: ${exception.message}")
                                onComplete(null)
                            }
                    }
                } else {
                    val exception = task.exception
                    setError("Error signing up: ${exception?.message ?: "Unknown error"}")
                    onComplete(null)
                }
            }
    }

    fun fetchUserRole(userId: String, onRoleFetched: (UserRole) -> Unit) {
        viewModelScope.launch {
            setLoading()
            db.child("clientes").child(userId).get().addOnSuccessListener { dataSnapshot ->
                val user = dataSnapshot.getValue(User::class.java)
                val role = user?.rol ?: UserRole.CLIENTE
                onRoleFetched(role)
                setSuccess()
            }.addOnFailureListener { exception ->
                setError("Error fetching user role: ${exception.message}")
            }
        }
    }

    fun getUserType(userId: String, callback: (Boolean) -> Unit) {
        db.child("empleados").child(userId).get().addOnSuccessListener { empSnapshot ->
            if (empSnapshot.exists()) {
                callback(false)  // Es un empleado/administrador
            } else {
                db.child("clientes").child(userId).get().addOnSuccessListener { snapshot ->
                    callback(snapshot.exists())  // Es un cliente si existe
                }.addOnFailureListener {
                    callback(false)  // No es un cliente
                }
            }
        }.addOnFailureListener {
            callback(false)  // No es un empleado/administrador
        }
    }
}
