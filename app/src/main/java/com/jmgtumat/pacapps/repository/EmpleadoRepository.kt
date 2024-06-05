package com.jmgtumat.pacapps.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.jmgtumat.pacapps.data.Cita
import com.jmgtumat.pacapps.data.Empleado
import com.jmgtumat.pacapps.data.HorariosPorDia
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Locale

class EmpleadoRepository {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("empleados")
    private val citasDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference.child("citas")


    suspend fun getEmpleados(): List<Empleado> {
        Log.d("EmpleadoRepository", "Fetching empleados from Firebase")
        val snapshot = database.get().await()
        val empleados = snapshot.children.mapNotNull { it.getValue(Empleado::class.java) }
        Log.d("EmpleadoRepository", "Fetched empleados: $empleados")
        return empleados
    }

    // Método para obtener el ID del empleado autenticado
    fun getAuthenticatedEmpleadoId(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser?.uid ?: throw IllegalStateException("User not authenticated")
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

    suspend fun getHorariosTrabajo(empleadoId: String): Map<String, HorariosPorDia>? {
        val snapshot = database.child(empleadoId).child("horariosTrabajo").get().await()
        Log.d("EmpleadoRepository", "Snapshot obtenido: $snapshot")
        val horariosTrabajo = snapshot.getValue(object : GenericTypeIndicator<Map<String, HorariosPorDia>>() {})
        Log.d("EmpleadoRepository", "Horarios de trabajo decodificados: $horariosTrabajo")

        // Asegurarse de que las horas de inicio y fin estén en el formato adecuado (HH:mm)
        horariosTrabajo?.forEach { (_, horariosPorDia) ->
            horariosPorDia.manana?.let { intervalo ->
                intervalo.horaInicio = formatToHHmm(intervalo.horaInicio)
                intervalo.horaFin = formatToHHmm(intervalo.horaFin)
            }
            horariosPorDia.tarde?.let { intervalo ->
                intervalo.horaInicio = formatToHHmm(intervalo.horaInicio)
                intervalo.horaFin = formatToHHmm(intervalo.horaFin)
            }
        }

        return horariosTrabajo
    }

    private fun formatToHHmm(time: String): String {
        val parsedTime = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(time)
        return SimpleDateFormat("HH:mm", Locale.getDefault()).format(parsedTime)
    }

    suspend fun updateHorariosTrabajo(empleadoId: String, horarioTrabajo: Map<String, HorariosPorDia>) {
        database.child(empleadoId).child("horariosTrabajo").setValue(horarioTrabajo).await()
    }

    suspend fun getCitasAsignadas(empleadoId: String): List<Cita> {
        val empleadoCitasSnapshot = database.child(empleadoId).child("citasAsignadas").get().await()
        val citaIds = empleadoCitasSnapshot.children.mapNotNull { it.getValue(String::class.java) }
        val citas = mutableListOf<Cita>()

        citaIds.forEach { citaId ->
            try {
                val citaSnapshot = citasDatabase.child(citaId).get().await()
                val cita = citaSnapshot.getValue(Cita::class.java)
                cita?.let { citas.add(it) }
            } catch (e: Exception) {
                Log.e("EmpleadoRepository", "Error al obtener la cita con ID: $citaId, Error: ${e.message}")
            }
        }

        return citas
    }

    suspend fun updateCitasAsignadas(empleadoId: String, citas: List<String>) {
        Log.d("EmpleadoRepository", "Actualizando citas asignadas para $empleadoId con los IDs: $citas")
        database.child(empleadoId).child("citasAsignadas").setValue(citas).await()
    }
}