package com.jmgtumat.pacapps.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jmgtumat.pacapps.data.Servicio
import java.util.UUID

class ServicioRepository {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("servicios")

    fun getServicios(): List<Servicio> {
        // Implementa la lógica para recuperar servicios de la base de datos
        // Por ejemplo, usando un ValueEventListener
        return listOf() // Reemplaza esto con la recuperación real de datos
    }

    fun addServicio(servicio: Servicio) {
        // Genera un ID único manualmente usando UUID
        val servicioId = UUID.randomUUID().toString()
        servicio.id = servicioId
        database.child(servicioId).setValue(servicio)
    }

    fun updateServicio(servicio: Servicio) {
        // Implementa la lógica para actualizar un servicio existente en la base de datos
        // Por ejemplo, usando una operación setValue()
        database.child(servicio.id).setValue(servicio)
    }

    fun deleteServicio(servicioId: String) {
        // Implementa la lógica para eliminar un servicio de la base de datos
        // Por ejemplo, usando una operación removeValue()
        database.child(servicioId).removeValue()
    }
}
