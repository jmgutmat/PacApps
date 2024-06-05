package com.jmgtumat.pacapps.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jmgtumat.pacapps.data.Servicio
import kotlinx.coroutines.tasks.await

class ServicioRepository {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("servicios")

    suspend fun getServicios(): List<Servicio> {
        val snapshot = database.get().await()
        return snapshot.children.map { it.getValue(Servicio::class.java)!! }
    }

    suspend fun addServicio(servicio: Servicio) {
        val newServicioRef = database.push()
        val servicioConId = servicio.copy(id = newServicioRef.key!!)
        newServicioRef.setValue(servicioConId).await()
    }

    suspend fun updateServicio(servicio: Servicio) {
        database.child(servicio.id).setValue(servicio).await()
    }

    suspend fun deleteServicio(servicioId: String) {
        database.child(servicioId).removeValue().await()
    }

    suspend fun getServicioById(servicioId: String): Servicio {
        val snapshot = database.child(servicioId).get().await()
        return snapshot.getValue(Servicio::class.java) ?: throw IllegalArgumentException("Servicio no encontrado")
    }
}
