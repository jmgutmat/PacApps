package com.jmgtumat.pacapps.util

import java.util.Calendar
import java.util.Locale

/**
 * Función utilitaria que convierte una hora en formato de cadena a minutos desde la medianoche.
 * @param time La hora en formato de cadena (HH:mm).
 * @return La hora en minutos desde la medianoche.
 */
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

/**
 * Función utilitaria que formatea una fecha en milisegundos en un formato de cadena personalizado.
 * @param millis La fecha en milisegundos.
 * @return La fecha formateada en el formato "Día de Mes".
 */
fun formatDateNew(millis: Long): String {
    val calendar = Calendar.getInstance().apply { timeInMillis = millis }
    return "${calendar.get(Calendar.DAY_OF_MONTH)} de ${calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())}"
}

/**
 * Función utilitaria que formatea una hora en milisegundos en un formato de cadena personalizado.
 * @param millis La hora en milisegundos.
 * @return La hora formateada en el formato "HH:mm".
 */
fun formatTimeNew(millis: Long): String {
    val calendar = Calendar.getInstance().apply { timeInMillis = millis }
    return "${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}"
}
/*
private fun getCitaHour(timeInMillis: Long): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timeInMillis
    return calendar.get(Calendar.HOUR_OF_DAY).toString()
}
*/


/*
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
*/

/*
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
*/
