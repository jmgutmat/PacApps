package com.jmgutmat.pacapps.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jmgtumat.pacapps.data.Cliente
import kotlinx.coroutines.tasks.await

class ClienteRepository {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("clientes")

    fun getClientes(): List<Cliente> {
        // Implement logic to fetch clientes from Firebase Realtime Database
        // For example, using a ValueEventListener
        return listOf() // Replace with actual data retrieval
    }

    suspend fun getNextId(): Int {
        val snapshot = database.orderByKey().limitToLast(1).get().await()
        val lastKey = snapshot.children.firstOrNull()?.key
        val lastId = lastKey?.toIntOrNull() ?: 0
        return lastId + 1
    }

    suspend fun addCliente(cliente: Cliente) {
        val nextId = getNextId()
        val clienteConId = cliente.copy(id = nextId.toString())
        database.child(nextId.toString()).setValue(clienteConId)
    }

    fun updateCliente(cliente: Cliente) {
        // Implement logic to update an existing cliente in Firebase Realtime Database
        // For example, using a setValue() operation
        database.child(cliente.id).setValue(cliente)
    }

    fun deleteCliente(clienteId: String) {
        // Implement logic to delete a cliente from Firebase Realtime Database
        // For example, using a removeValue() operation
        database.child(clienteId).removeValue()
    }
}
