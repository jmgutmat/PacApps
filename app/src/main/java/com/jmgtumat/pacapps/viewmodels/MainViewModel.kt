package com.jmgtumat.pacapps.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jmgtumat.pacapps.data.Cliente
import com.jmgtumat.pacapps.data.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await

/**
 * ViewModel principal para la gestión de la autenticación y roles de usuario.
 */
class MainViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: DatabaseReference = FirebaseDatabase.getInstance().reference

    private val _userRole = MutableStateFlow<UserRole?>(null)
    val userRole: StateFlow<UserRole?> = _userRole

    /**
     * Inicia sesión con correo electrónico y contraseña.
     * @param email Correo electrónico del usuario.
     * @param password Contraseña del usuario.
     * @return Resultado de la operación de inicio de sesión.
     */
    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<String> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val user = result.user
            Result.success(user?.uid ?: "")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Inicia sesión con Google.
     * @param idToken Token de identificación de Google.
     * @return Resultado de la operación de inicio de sesión.
     */
    suspend fun signInWithGoogle(idToken: String): Result<String> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(credential).await()
            val user = result.user
            val cliente = Cliente(
                id = user?.uid ?: "",
                nombre = user?.displayName ?: "",
                apellidos = "",
                telefono = "",
                correoElectronico = user?.email ?: "",
                historialCitas = emptyList()
            )
            db.child("clientes").child(user?.uid ?: "").setValue(cliente).await()
            Result.success(user?.uid ?: "")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtiene el tipo de usuario basado en su ID.
     * @param userId ID del usuario.
     * @param callback Callback para el resultado.
     */
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