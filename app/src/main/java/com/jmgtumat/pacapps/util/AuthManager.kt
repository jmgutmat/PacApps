package com.jmgtumat.pacapps.util

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.jmgtumat.pacapps.data.Cliente
import com.jmgtumat.pacapps.repository.ClienteRepository
import com.google.firebase.auth.Task


class AuthManager(private val context: Context) {
    private val auth = FirebaseAuth.getInstance()
    private var googleSignInLauncher: ActivityResultLauncher<Intent>? = null

    private val _signInWithGoogleResult = MutableLiveData<Result<Boolean>>()
    val signInWithGoogleResult: LiveData<Result<Boolean>> get() = _signInWithGoogleResult

    fun initGoogleSignInLauncher(launcher: ActivityResultLauncher<Intent>) {
        googleSignInLauncher = launcher
    }

    fun signInWithGoogle(signInWithGoogleLauncher: ActivityResultLauncher<Intent>) {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("YOUR_WEB_CLIENT_ID") // Reemplaza con tu ID de cliente web
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions)
        val signInIntent = googleSignInClient.signInIntent

        signInWithGoogleLauncher.launch(signInIntent)
    }

    fun handleSignInWithGoogleResult(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            if (account != null) {
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener { signInTask ->
                        if (signInTask.isSuccessful) {
                            _signInWithGoogleResult.value = Result.success(true)
                        } else {
                            _signInWithGoogleResult.value = Result.failure(Exception("Authentication failed"))
                        }
                    }
            } else {
                _signInWithGoogleResult.value = Result.failure(Exception("Google sign in failed"))
            }
        } catch (e: ApiException) {
            _signInWithGoogleResult.value = Result.failure(e)
        }
    }

    fun signInWithEmail(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("AuthManager", "signInWithEmail:success")
                    onSuccess.invoke()
                } else {
                    Log.w("AuthManager", "signInWithEmail:failure", task.exception)
                    onError.invoke(task.exception?.message ?: "Unknown error occurred")
                }
            }
    }

    fun register(
        name: String,
        lastName: String,
        phone: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Si el registro es exitoso, actualizamos el perfil del usuario con su nombre completo
                    val user = auth.currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName("$name $lastName")
                        .build()

                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { profileTask ->
                            if (profileTask.isSuccessful) {
                                // Creamos un nuevo cliente con los datos proporcionados
                                val cliente = Cliente(
                                    nombre = name,
                                    apellidos = lastName,
                                    telefono = phone,
                                    correoElectronico = email,
                                    historialCitas = emptyList() // Inicializamos el historial de citas como una lista vacía
                                )

                                // Guardamos el cliente en la base de datos utilizando ClienteRepository
                                ClienteRepository().addCliente(cliente)
                                    .addOnCompleteListener { dbTask ->
                                        if (dbTask.isSuccessful) {
                                            onSuccess.invoke()
                                        } else {
                                            onError.invoke("Error al guardar el cliente en la base de datos")
                                        }
                                    }
                            } else {
                                onError.invoke("Error al actualizar el perfil del usuario")
                            }
                        }
                } else {
                    onError.invoke(task.exception?.message ?: "Error desconocido durante el registro")
                }
            }
    }


}
