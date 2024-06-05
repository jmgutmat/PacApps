package com.jmgtumat.pacapps.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jmgtumat.pacapps.data.Servicio
import kotlinx.coroutines.tasks.await

/**
 * Repositorio para acceder y manipular datos de servicios en la base de datos Firebase.
 */
class ServicioRepository {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("servicios")

    /**
     * Obtiene todos los servicios de la base de datos.
     * @return Lista de servicios.
     */
    suspend fun getServicios(): List<Servicio> {
        val snapshot = database.get().await()
        return snapshot.children.map { it.getValue(Servicio::class.java)!! }
    }

    /**
     * Agrega un nuevo servicio a la base de datos.
     * @param servicio El servicio a agregar.
     */
    suspend fun addServicio(servicio: Servicio) {
        val newServicioRef = database.push()
        val servicioConId = servicio.copy(id = newServicioRef.key!!)
        newServicioRef.setValue(servicioConId).await()
    }

    /**
     * Actualiza un servicio existente en la base de datos.
     * @param servicio El servicio actualizado.
     */
    suspend fun updateServicio(servicio: Servicio) {
        database.child(servicio.id).setValue(servicio).await()
    }

    /**
     * Elimina un servicio de la base de datos.
     * @param servicioId El ID del servicio a eliminar.
     */
    suspend fun deleteServicio(servicioId: String) {
        database.child(servicioId).removeValue().await()
    }

    /**
     * Obtiene un servicio específico por su ID de la base de datos.
     * @param servicioId El ID del servicio.
     * @return El servicio correspondiente al ID proporcionado.
     * @throws IllegalArgumentException Si el servicio no se encuentra en la base de datos.
     */
    suspend fun getServicioById(servicioId: String): Servicio {
        val snapshot = database.child(servicioId).get().await()
        return snapshot.getValue(Servicio::class.java) ?: throw IllegalArgumentException("Servicio no encontrado")
    }
}