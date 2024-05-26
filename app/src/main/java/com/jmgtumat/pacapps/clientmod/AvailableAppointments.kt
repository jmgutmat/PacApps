package com.jmgtumat.pacapps.clientmod

import com.jmgtumat.pacapps.data.Cita
import com.jmgtumat.pacapps.data.Empleado
import com.jmgtumat.pacapps.data.HorariosPorDia
import com.jmgtumat.pacapps.data.Servicio
import java.util.Calendar
import java.util.Locale
import kotlin.math.min

fun calculateAvailableDates(servicio: Servicio, citas: List<Cita>, empleados: List<Empleado>): List<Calendar> {
    val availableDates = mutableListOf<Calendar>()
    val currentDate = Calendar.getInstance()
    val endDate = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 30) }

    while (currentDate.before(endDate)) {
        if (hasAvailableSlots(currentDate, servicio, citas, empleados)) {
            availableDates.add(currentDate.clone() as Calendar)
        }
        currentDate.add(Calendar.DAY_OF_YEAR, 1)
    }

    return availableDates
}

private fun hasAvailableSlots(date: Calendar, servicio: Servicio, citas: List<Cita>, empleados: List<Empleado>): Boolean {
    return empleados.any { empleado ->
        val workSchedule = empleado.horariosTrabajo[date.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
            ?.toString()]
        workSchedule != null && calculateAvailableSlotsForEmployee(date, servicio, citas, workSchedule).isNotEmpty()
    }
}

fun calculateAvailableSlots(date: Calendar, servicio: Servicio, citas: List<Cita>, empleados: List<Empleado>): List<Calendar> {
    return empleados.flatMap { empleado ->
        val workSchedule = empleado.horariosTrabajo[date.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
            ?.toString()]
        workSchedule?.let { calculateAvailableSlotsForEmployee(date, servicio, citas, it) } ?: emptyList()
    }
}

private fun calculateAvailableSlotsForEmployee(date: Calendar, servicio: Servicio, citas: List<Cita>, horariosPorDia: HorariosPorDia): List<Calendar> {
    val availableSlots = mutableListOf<Calendar>()
    val intervals = listOf(horariosPorDia.manana, horariosPorDia.tarde)

    intervals.forEach { intervalo ->
        if (intervalo.disponible) {
            val intervalStart = date.clone() as Calendar
            intervalStart.set(Calendar.HOUR_OF_DAY, intervalo.horaInicio.split(":")[0].toInt())
            intervalStart.set(Calendar.MINUTE, intervalo.horaInicio.split(":")[1].toInt())

            val intervalEnd = date.clone() as Calendar
            intervalEnd.set(Calendar.HOUR_OF_DAY, intervalo.horaFin.split(":")[0].toInt())
            intervalEnd.set(Calendar.MINUTE, intervalo.horaFin.split(":")[1].toInt())

            var currentSlot = intervalStart.clone() as Calendar
            while (currentSlot.before(intervalEnd)) {
                val slotEnd = (currentSlot.clone() as Calendar).apply { add(Calendar.MINUTE, servicio.duracion) }

                if (slotEnd.after(intervalEnd)) break

                if (isSlotAvailable(currentSlot, slotEnd, servicio, citas)) {
                    availableSlots.add(currentSlot.clone() as Calendar)
                }

                currentSlot.add(Calendar.MINUTE, 20) // Avanzar 20 minutos para la próxima iteración
            }
        }
    }

    return availableSlots
}

private fun isSlotAvailable(slotStart: Calendar, slotEnd: Calendar, servicio: Servicio, citas: List<Cita>): Boolean {
    return citas.none { cita ->
        val citaStart = Calendar.getInstance().apply { timeInMillis = cita.horaInicio }
        val citaEnd = Calendar.getInstance().apply { timeInMillis = cita.horaInicio + cita.duracion * 60 * 1000 }

        (slotStart.before(citaEnd) && slotEnd.after(citaStart)) ||
                (slotEnd.after(citaStart) && slotStart.before(citaEnd)) ||
                (min(slotStart.timeInMillis - citaEnd.timeInMillis, citaStart.timeInMillis - slotEnd.timeInMillis) < 5 * 60 * 1000)
    }
}
