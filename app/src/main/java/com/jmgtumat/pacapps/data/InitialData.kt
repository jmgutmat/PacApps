package com.jmgtumat.pacapps.data

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InitialData {

    // Función para crear la estructura de datos inicial en la base de datos
    suspend fun createInitialDataStructure() {
        val database = FirebaseDatabase.getInstance().reference

        // Crea nodos raíz para cada tipo de datos
        createCitasStructure(database)
        createClientesStructure(database)
        createEmpleadosStructure(database)
        createServiciosStructure(database)
    }

    // Función para crear la estructura de citas
    private suspend fun createCitasStructure(database: DatabaseReference) {
        database.child("citas").setValue(emptyMap<String, Any>())
    }

    // Función para crear la estructura de clientes
    private suspend fun createClientesStructure(database: DatabaseReference) {
        database.child("clientes").setValue(emptyMap<String, Any>())
    }

    // Función para crear la estructura de empleados
    private suspend fun createEmpleadosStructure(database: DatabaseReference) {
        database.child("empleados").setValue(emptyMap<String, Any>())
    }

    // Función para crear la estructura de servicios
    private suspend fun createServiciosStructure(database: DatabaseReference) {
        database.child("servicios").setValue(emptyMap<String, Any>())
    }
}
