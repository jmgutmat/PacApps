package com.jmgtumat.pacapps.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jmgtumat.pacapps.data.Cita
import kotlinx.coroutines.tasks.await
import java.util.Calendar

class CitaRepository {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("citas")

    suspend fun getCitas(): List<Cita> {
        val snapshot = database.get().await()
        return snapshot.children.map { it.getValue(Cita::class.java)!! }
    }

    suspend fun getCitasByDate(dateInMillis: Long): List<Cita> {
        val snapshot = database.get().await()
        return snapshot.children.mapNotNull { it.getValue(Cita::class.java) }.filter {
            val calendar = Calendar.getInstance().apply { timeInMillis = it.fecha }
            val selectedCalendar = Calendar.getInstance().apply { timeInMillis = dateInMillis }
            calendar.get(Calendar.YEAR) == selectedCalendar.get(Calendar.YEAR) &&
                    calendar.get(Calendar.DAY_OF_YEAR) == selectedCalendar.get(Calendar.DAY_OF_YEAR)
        }
    }

    suspend fun addCita(cita: Cita) {
        val newCitaRef = database.push()
        val citaConId = cita.copy(id = newCitaRef.key!!)
        newCitaRef.setValue(citaConId).await()
    }

    suspend fun updateCita(cita: Cita) {
        database.child(cita.id).setValue(cita).await()
    }

    suspend fun deleteCita(citaId: String) {
        database.child(citaId).removeValue().await()
    }

    suspend fun getCitaById(citaId: String): Cita {
        val snapshot = database.child(citaId).get().await()
        return snapshot.getValue(Cita::class.java)!!
    }

    suspend fun getCitasByDateRange(startDate: Long, endDate: Long): List<Cita> {
        val snapshot = database.orderByChild("fecha").startAt(startDate.toDouble()).endAt(endDate.toDouble()).get().await()
        return snapshot.children.map { it.getValue(Cita::class.java)!! }
    }
}
