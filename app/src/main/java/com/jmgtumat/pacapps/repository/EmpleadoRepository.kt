package com.jmgtumat.pacapps.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.jmgtumat.pacapps.data.Empleado
import com.jmgtumat.pacapps.data.HorarioDisponible
import kotlinx.coroutines.tasks.await

class EmpleadoRepository {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("empleados")

    suspend fun getEmpleados(): List<Empleado> {
        val snapshot = database.get().await()
        return snapshot.children.mapNotNull { it.getValue(Empleado::class.java) }
    }

    suspend fun addEmpleado(empleado: Empleado) {
        val newEmpleadoRef = database.push()
        val empleadoConId = empleado.copy(id = newEmpleadoRef.key!!)
        newEmpleadoRef.setValue(empleadoConId).await()
    }

    suspend fun updateEmpleado(empleado: Empleado) {
        database.child(empleado.id).setValue(empleado).await()
    }

    suspend fun deleteEmpleado(empleadoId: String) {
        database.child(empleadoId).removeValue().await()
    }

    suspend fun getHorarioTrabajo(empleadoId: String): Map<String, HorarioDisponible>? {
        val snapshot = database.child(empleadoId).child("horarioTrabajo").get().await()
        return snapshot.getValue(object : GenericTypeIndicator<Map<String, HorarioDisponible>>() {})
    }

    suspend fun updateHorarioTrabajo(empleadoId: String, horarioTrabajo: Map<String, HorarioDisponible>) {
        database.child(empleadoId).child("horarioTrabajo").setValue(horarioTrabajo).await()
    }
}