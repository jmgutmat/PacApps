package com.jmgtumat.pacapps.util

import com.jmgtumat.pacapps.data.Cita
import com.jmgtumat.pacapps.data.Empleado
import java.util.Calendar
import java.util.Locale

private fun getCitaHour(time: String): Long {
    val parts = time.split(":")
    return if (parts.size == 2) {
        val hour = parts[0].toLongOrNull() ?: 0L
        val minute = parts[1].toLongOrNull() ?: 0L
        hour * 60 + minute
    } else {
        0L
    }
}

fun getCitasEnHorarioTrabajo(citas: List<Cita>, empleado: Empleado, selectedDate: Calendar): List<Cita> {
    val dayOfWeek = selectedDate.get(Calendar.DAY_OF_WEEK)
    val dayString = getDayString(dayOfWeek)
    val horariosTrabajo = empleado.horariosTrabajo[dayString] ?: return emptyList()

    val intervaloManana = horariosTrabajo.manana
    val intervaloTarde = horariosTrabajo.tarde

    return citas.filter { cita ->
        val citaCalendar = Calendar.getInstance().apply { timeInMillis = cita.horaInicio }
        val citaHour = getCitaHour(formatTimeNew(cita.horaInicio)).toLong()

        val mananaStartHour = getCitaHour(intervaloManana.horaInicio)
        val mananaEndHour = getCitaHour(intervaloManana.horaFin)
        val tardeStartHour = getCitaHour(intervaloTarde.horaInicio)
        val tardeEndHour = getCitaHour(intervaloTarde.horaFin)

        val isMorning = citaHour in mananaStartHour..mananaEndHour
        val isAfternoon = citaHour in tardeStartHour..tardeEndHour

        citaCalendar.get(Calendar.DAY_OF_WEEK) == dayOfWeek && (isMorning || isAfternoon)
    }
}




fun getDayString(dayOfWeek: Int): String {
    return when (dayOfWeek) {
        Calendar.MONDAY -> "Lunes"
        Calendar.TUESDAY -> "Martes"
        Calendar.WEDNESDAY -> "Miércoles"
        Calendar.THURSDAY -> "Jueves"
        Calendar.FRIDAY -> "Viernes"
        Calendar.SATURDAY -> "Sábado"
        Calendar.SUNDAY -> "Domingo"
        else -> ""
    }
}

fun formatDateNew(millis: Long): String {
    val calendar = Calendar.getInstance().apply { timeInMillis = millis }
    return "${calendar.get(Calendar.DAY_OF_MONTH)} de ${calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())}"
}

fun formatTimeNew(millis: Long): String {
    val calendar = Calendar.getInstance().apply { timeInMillis = millis }
    return "${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}"
}

private fun getCitaHour(timeInMillis: Long): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timeInMillis
    return calendar.get(Calendar.HOUR_OF_DAY).toString()
}
