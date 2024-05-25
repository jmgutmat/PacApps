package com.jmgtumat.pacapps.util

import com.jmgtumat.pacapps.data.Cita
import com.jmgtumat.pacapps.data.Empleado
import java.util.Calendar
import java.util.Locale

fun getCitasEnHorarioTrabajo(citas: List<Cita>, empleado: Empleado, selectedDate: Calendar): List<Cita> {
    val dayOfWeek = selectedDate.get(Calendar.DAY_OF_WEEK)
    val dayString = getDayString(dayOfWeek)
    val horariosTrabajo = empleado.horariosTrabajo[dayString] ?: return emptyList()

    return citas.filter { cita ->
        val citaDate = Calendar.getInstance().apply { timeInMillis = cita.fecha }
        val citaDayOfWeek = citaDate.get(Calendar.DAY_OF_WEEK)
        citaDayOfWeek == dayOfWeek && horariosTrabajo.any { intervalo ->
            val citaHour = getCitaHour(cita.horaInicio).toInt()
            val intervaloStartHour = intervalo.horaInicio.split(":")[0].toInt()
            val intervaloEndHour = intervalo.horaFin.split(":")[0].toInt()
            citaHour in intervaloStartHour..intervaloEndHour
        }
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
