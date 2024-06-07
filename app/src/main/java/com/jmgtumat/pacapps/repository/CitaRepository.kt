package com.jmgtumat.pacapps.repository

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jmgtumat.pacapps.data.Cita
import kotlinx.coroutines.tasks.await
import java.util.Calendar

/**
 * Repositorio para acceder y manipular datos de citas en la base de datos Firebase.
 */
class CitaRepository {

    var database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("citas")

    /**
     * Obtiene todas las citas de la base de datos.
     */
    suspend fun getCitas(): List<Cita> {
        val snapshot = database.get().await()
        return snapshot.children.mapNotNull { child ->
            child.getValue(Cita::class.java)
        }
    }

    /**
     * Obtiene las citas filtradas por fecha.
     */
    suspend fun getCitasByDate(dateInMillis: Long): List<Cita> {
        val snapshot = database.get().await()
        return snapshot.children.mapNotNull { child ->
            child.getValue(Cita::class.java)
        }.filter {
            val calendar = Calendar.getInstance().apply { timeInMillis = it.fecha }
            val selectedCalendar = Calendar.getInstance().apply { timeInMillis = dateInMillis }
            calendar.get(Calendar.YEAR) == selectedCalendar.get(Calendar.YEAR) &&
                    calendar.get(Calendar.DAY_OF_YEAR) == selectedCalendar.get(Calendar.DAY_OF_YEAR)
        }
    }

    /**
     * Obtiene las citas filtradas por ID de empleado.
     */
    suspend fun getCitasByEmpleadoId(empleadoId: String): List<Cita> {
        val snapshot = database.get().await()
        return snapshot.children.mapNotNull { child ->
            child.getValue(Cita::class.java)
        }.filter {
            it.empleadoId == empleadoId
        }
    }

    /**
     * Agrega una nueva cita a la base de datos.
     */
    suspend fun addCita(cita: Cita): String {
        val newCitaRef = database.push() // Obtiene una nueva referencia con un ID único
        cita.id = newCitaRef.key ?: "" // Asigna el ID a la cita
        newCitaRef.setValue(cita).await() // Guarda la cita en la base de datos
        return newCitaRef.key ?: "" // Devuelve el ID de la nueva cita
    }

    /**
     * Actualiza una cita existente en la base de datos.
     */
    suspend fun updateCita(cita: Cita) {
        database.child(cita.id).setValue(cita).await()
    }

    /**
     * Elimina una cita de la base de datos.
     */
    suspend fun deleteCita(citaId: String) {
        database.child(citaId).removeValue().await()
    }

    /**
     * Obtiene una cita por su ID.
     */
    suspend fun getCitaById(citaId: String): Cita {
        val snapshot = database.child(citaId).get().await()
        return snapshot.getValue(Cita::class.java)!!
    }

    /**
     * Obtiene las citas filtradas por ID de empleado y fecha.
     */
    suspend fun getCitasByEmpleadoIdAndDate(empleadoId: String, dateInMillis: Long): List<Cita> {
        val snapshot = database.get().await()
        val citasByEmpleado = snapshot.children.mapNotNull { child ->
            child.getValue(Cita::class.java)
        }.filter { it.empleadoId == empleadoId } // Filtra por ID de empleado

        // Filtra por fecha (año, mes, día)
        val selectedDate = Calendar.getInstance().apply { timeInMillis = dateInMillis }
        val selectedYear = selectedDate.get(Calendar.YEAR)
        val selectedMonth = selectedDate.get(Calendar.MONTH)
        val selectedDay = selectedDate.get(Calendar.DAY_OF_MONTH)

        Log.d("CitaRepository", "Fecha seleccionada: $selectedYear-$selectedMonth-$selectedDay")

        return citasByEmpleado.filter {
            val citaCalendar = Calendar.getInstance().apply { timeInMillis = it.fecha }
            Log.d("Fecha cita", "Año: ${citaCalendar.get(Calendar.YEAR)}, Mes: ${citaCalendar.get(Calendar.MONTH)}, Día: ${citaCalendar.get(Calendar.DAY_OF_MONTH)}")
            citaCalendar.get(Calendar.YEAR) == selectedYear &&
                    citaCalendar.get(Calendar.MONTH) == selectedMonth &&
                    citaCalendar.get(Calendar.DAY_OF_MONTH) == selectedDay
        }
    }
}

//    suspend fun getHistorialCitasCliente(clienteId: String): List<Cita> {
//        val snapshot = database.orderByChild("clienteId").equalTo(clienteId).get().await()
//        return snapshot.children.map { it.getValue(Cita::class.java)!! }
//    }
//
//    suspend fun getCitaPendienteCliente(clienteId: String): Cita? {
//        val snapshot = database.orderByChild("clienteId").equalTo(clienteId).get().await()
//        val citas = snapshot.children.mapNotNull { it.getValue(Cita::class.java) }
//        return citas.find { it.estado == CitaEstado.PENDIENTE }
//    }
//
//    suspend fun getCitasByDateRange(startDate: Long, endDate: Long): List<Cita> {
//        val snapshot = database.orderByChild("fecha").startAt(startDate.toDouble()).endAt(endDate.toDouble()).get().await()
//        return snapshot.children.map { it.getValue(Cita::class.java)!! }
//    }

