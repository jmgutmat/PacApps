package com.jmgtumat.pacapps.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jmgtumat.pacapps.data.Cliente
import kotlinx.coroutines.tasks.await

class ClienteRepository {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("clientes")

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
}
