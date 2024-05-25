package com.jmgtumat.pacapps.viewmodels

import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jmgtumat.pacapps.data.User
import com.jmgtumat.pacapps.data.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EmailSignInViewModel : BaseViewModel() {

    private val _user = MutableStateFlow<FirebaseUser?>(null)
    val user: StateFlow<FirebaseUser?> = _user
    private val db: DatabaseReference = FirebaseDatabase.getInstance().reference

    fun signInWithEmailAndPassword(correoElectronico: String, password: String) {
        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(correoElectronico, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _user.value = auth.currentUser
                } else {
                    setError("Error signing in")
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
