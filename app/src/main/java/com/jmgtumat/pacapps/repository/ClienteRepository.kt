package com.jmgtumat.pacapps.repository

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jmgtumat.pacapps.data.Cita
import com.jmgtumat.pacapps.data.Cliente
import kotlinx.coroutines.tasks.await

class ClienteRepository {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("clientes")
    private val citasDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference.child("citas")


    suspend fun getClientes(): List<Cliente> {
        val snapshot = database.get().await()
        return snapshot.children.map { it.getValue(Cliente::class.java)!! }
    }

    suspend fun addCliente(cliente: Cliente) {
        val newClienteRef = database.push()
        val clienteConId = cliente.copy(id = newClienteRef.key!!)
        newClienteRef.setValue(clienteConId).await()
    }

    suspend fun updateCliente(cliente: Cliente) {
        database.child(cliente.id).setValue(cliente).await()
    }

    suspend fun deleteCliente(clienteId: String) {
        database.child(clienteId).removeValue().await()
    }

    suspend fun updateHistorialCitas(clienteId: String, citas: List<Cita>) {
        val citaIds = citas.map { it.id } // Map the list of Cita objects to their IDs
        Log.d("ClienteRepository", "Actualizando historial de citas para $clienteId con los IDs: $citaIds")
        database.child(clienteId).child("historialCitas").setValue(citaIds).await()
    }

    suspend fun getHistorialCitas(clienteId: String): List<Cita> {
        val clienteCitasSnapshot = database.child(clienteId).child("historialCitas").get().await()
        val citaIds = clienteCitasSnapshot.children.mapNotNull { it.getValue(String::class.java) }
        val citas = mutableListOf<Cita>()

        Log.d("ClienteRepository", "Obteniendo historial de citas para $clienteId con los IDs: $citaIds")

        citaIds.forEach { citaId ->
            val citaSnapshot = citasDatabase.child(citaId).get().await()
            val cita = citaSnapshot.getValue(Cita::class.java)
            cita?.let { citas.add(it) }
        }

        return citas
    }

}
