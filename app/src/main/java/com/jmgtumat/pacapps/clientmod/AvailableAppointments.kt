package com.jmgtumat.pacapps.clientmod

import android.util.Log
import com.jmgtumat.pacapps.data.Cita
import com.jmgtumat.pacapps.data.Empleado
import com.jmgtumat.pacapps.data.HorariosPorDia
import com.jmgtumat.pacapps.data.Intervalo
import com.jmgtumat.pacapps.data.Servicio
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Calcula las fechas disponibles para una cita basándose en el servicio, las citas existentes y los horarios de los empleados.
 *
 * @param servicio el servicio para el que se están calculando las fechas disponibles.
 * @param citas lista de citas existentes.
 * @param empleados lista de empleados con sus horarios de trabajo.
 * @return una lista de objetos Calendar representando las fechas disponibles.
 */
fun calculateAvailableDates(servicio: Servicio, citas: List<Cita>, empleados: List<Empleado>): List<Calendar> {
    Log.d("NewAppointmentScreen", "Calculating available dates for service: ${servicio.nombre}")

    val availableDates = mutableListOf<Calendar>()
    val today = Calendar.getInstance()

    // Mapear los nombres de los días de la semana a los utilizados en el diálogo de horario
    val dayOfWeekMap = mapOf(
        Calendar.MONDAY to "Lunes".toLowerCase(Locale.ROOT),
        Calendar.TUESDAY to "Martes".toLowerCase(Locale.ROOT),
        Calendar.WEDNESDAY to "Miércoles".toLowerCase(Locale.ROOT),
        Calendar.THURSDAY to "Jueves".toLowerCase(Locale.ROOT),
        Calendar.FRIDAY to "Viernes".toLowerCase(Locale.ROOT),
        Calendar.SATURDAY to "Sábado".toLowerCase(Locale.ROOT)
    )

    // Vamos a permitir las fechas para los próximos 30 días
    for (i in 0..30) {
        val date = (today.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, i) }
        val dayOfWeek = dayOfWeekMap[date.get(Calendar.DAY_OF_WEEK)]
        Log.d("NewAppointmentScreen", "Checking date: ${date.time}, Day of week: $dayOfWeek")

        if (dayOfWeek != null) {
            // Obtener los empleados que trabajen en ese día
            val workingEmployees = empleados.filter { empleado ->
                empleado.horariosTrabajo[dayOfWeek.capitalize(Locale.ROOT)]?.manana?.disponible == true ||
                        empleado.horariosTrabajo[dayOfWeek.capitalize(Locale.ROOT)]?.tarde?.disponible == true
            }
            Log.d("NewAppointmentScreen", "Working employees: ${workingEmployees.size}")

            if (workingEmployees.isNotEmpty()) {
                val availableSlots = mutableListOf<Intervalo>()
                workingEmployees.forEach { empleado ->
                    val horariosPorDia = empleado.horariosTrabajo[dayOfWeek.capitalize(Locale.ROOT)]

                    horariosPorDia?.manana?.let { if (it.disponible) availableSlots.add(it) }
                    horariosPorDia?.tarde?.let { if (it.disponible) availableSlots.add(it) }
                }

                // Comprobar y agregar intervalos disponibles de acuerdo con las citas existentes y los intervalos de tiempo
                val citasDelDia = citas.filter { cita ->
                    val citaCalendar = Calendar.getInstance().apply { timeInMillis = cita.fecha }
                    citaCalendar.get(Calendar.DAY_OF_YEAR) == date.get(Calendar.DAY_OF_YEAR)
                }
                Log.d("NewAppointmentScreen", "Citas del dia: ${citasDelDia.size}")

                val availableIntervals = calculateAvailableIntervals(date, availableSlots, citasDelDia, servicio.duracion)
                if (availableIntervals.isNotEmpty()) {
                    availableDates.add(date)
                }
            }
        }
    }

    Log.d("NewAppointmentScreen", "Available dates calculated: ${availableDates.size}")
    return availableDates
}

/**
 * Calcula los intervalos de tiempo disponibles para una cita basándose en los intervalos de trabajo, las citas existentes y la duración del servicio.
 *
 * @param date el día para el que se están calculando los intervalos.
 * @param slots los intervalos de trabajo disponibles.
 * @param citas las citas existentes para el día dado.
 * @param duracionServicio la duración del servicio en minutos.
 * @return una lista de objetos Intervalo representando los intervalos disponibles.
 */
fun calculateAvailableIntervals(date: Calendar, slots: List<Intervalo>, citas: List<Cita>, duracionServicio: Int): List<Intervalo> {
    val availableIntervals = mutableListOf<Intervalo>()
    val df = SimpleDateFormat("HH:mm", Locale.getDefault())

    slots.forEach { slot ->
        val horaInicioSlot = df.parse(slot.horaInicio).time
        val horaFinSlot = df.parse(slot.horaFin).time

        var currentStart = horaInicioSlot

        while (currentStart + duracionServicio * 60000 <= horaFinSlot) {
            val currentEnd = currentStart + duracionServicio * 60000
            val overlap = citas.any { cita ->
                val citaStart = cita.horaInicio
                val citaEnd = cita.horaInicio + cita.duracion * 60000
                !(currentEnd <= citaStart || currentStart >= citaEnd)
            }

            if (!overlap) {
                availableIntervals.add(Intervalo(df.format(Date(currentStart)), df.format(Date(currentEnd)), true))
            }

            // Evitar tiempos muertos
            currentStart += (duracionServicio + 5) * 60000 // Añadimos 5 minutos
        }
    }

    return availableIntervals
}

/**
 * Calcula los intervalos de tiempo disponibles para un servicio en un día específico, para todos los empleados, teniendo en cuenta las citas existentes.
 *
 * @param date el día para el que se están calculando los intervalos.
 * @param servicio el servicio para el que se están calculando los intervalos.
 * @param citas las citas existentes.
 * @param empleados la lista de empleados.
 * @return una lista de objetos Calendar representando los intervalos de tiempo disponibles.
 */
fun calculateAvailableSlots(date: Calendar, servicio: Servicio, citas: List<Cita>, empleados: List<Empleado>): List<Calendar> {
    Log.d("NewAppointmentScreen", "Calculating available slots for date: ${date.time} and service: ${servicio.nombre}")
    val availableSlots = mutableListOf<Calendar>()

    empleados.forEach { empleado ->
        val dayOfWeek = date.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
            ?.toLowerCase(Locale.ROOT)?.capitalize(Locale.ROOT)
        val workSchedule = empleado.horariosTrabajo[dayOfWeek]

        workSchedule?.let {
            availableSlots.addAll(calculateAvailableSlotsForEmployee(date, servicio, citas, it))
        }
    }
    return availableSlots
}

/**
 * Calcula los intervalos de tiempo disponibles para un empleado en un día específico, para un horario dado, teniendo en cuenta las citas existentes.
 *
 * @param date el día para el que se están calculando los intervalos.
 * @param servicio el servicio para el que se están calculando los intervalos.
 * @param citas las citas existentes.
 * @param horariosPorDia los horarios de trabajo del empleado para el día dado.
 * @return una lista de objetos Calendar representando los intervalos de tiempo disponibles.
 */
private fun calculateAvailableSlotsForEmployee(date: Calendar, servicio: Servicio, citas: List<Cita>, horariosPorDia: HorariosPorDia): List<Calendar> {
    val availableSlots = mutableListOf<Calendar>()
    val intervals = listOf(horariosPorDia.manana, horariosPorDia.tarde)
    val durationInMillis = servicio.duracion * 60 * 1000

    intervals.forEach { intervalo ->
        if (intervalo.disponible) {
            val df = SimpleDateFormat("HH:mm", Locale.getDefault())
            val start = df.parse(intervalo.horaInicio).time
            val end = df.parse(intervalo.horaFin).time
            var currentSlotStart = Calendar.getInstance().apply { timeInMillis = start }

            while (currentSlotStart.timeInMillis + durationInMillis <= end) {
                val slotEnd = currentSlotStart.timeInMillis + durationInMillis

                // Filtrar citas por dia
                val citasDelDia = citas.filter { cita ->
                    isSameDay(cita.fecha, date)
                }

                val overlap = citasDelDia.any { cita ->
                    cita.horaInicio < slotEnd && cita.horaInicio + cita.duracion * 60 * 1000 > currentSlotStart.timeInMillis
                }

                if (!overlap) {
                    availableSlots.add(currentSlotStart.clone() as Calendar)
                }

                // Incrementa el tiempo de inicio del slot en intervalos de la duración del servicio
                currentSlotStart.add(Calendar.MINUTE, servicio.duracion)
            }
        }
    }

    return availableSlots
}

/**
 * Verifica si una fecha dada es el mismo día que una fecha de calendario.
 *
 * @param date1 la fecha en milisegundos.
 * @param date2 el objeto Calendar que representa la fecha.
 * @return true si son el mismo día, de lo contrario false.
 */
fun isSameDay(date1: Long, date2: Calendar): Boolean {
    val calendar1 = Calendar.getInstance().apply { timeInMillis = date1 }
    return calendar1.get(Calendar.YEAR) == date2.get(Calendar.YEAR) &&
            calendar1.get(Calendar.DAY_OF_YEAR) == date2.get(Calendar.DAY_OF_YEAR)
}


//private fun hasAvailableSlots(date: Calendar, servicio: Servicio, citas: List<Cita>, empleados: List<Empleado>): Boolean {
//    return empleados.any { empleado ->
//        val workSchedule = empleado.horariosTrabajo[date.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
//            ?.toString()]
//        workSchedule != null && calculateAvailableSlotsForEmployee(date, servicio, citas, workSchedule).isNotEmpty()
//    }
//}

//fun calculateAvailableSlots(date: Calendar, servicio: Servicio, citas: List<Cita>, empleados: List<Empleado>): List<Calendar> {
//    Log.d("NewAppointmentScreen", "Calculating available slots for date: ${date.time} and service: ${servicio.nombre}")
//    return empleados.flatMap { empleado ->
//        val workSchedule = empleado.horariosTrabajo[date.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
//            ?.toString()]
//        workSchedule?.let { calculateAvailableSlotsForEmployee(date, servicio, citas, it) } ?: emptyList()
//    }
//}

//private fun isSlotAvailable(slotStart: Calendar, slotEnd: Calendar, servicio: Servicio, citas: List<Cita>): Boolean {
//    return citas.none { cita ->
//        val citaStart = Calendar.getInstance().apply { timeInMillis = cita.horaInicio }
//        val citaEnd = Calendar.getInstance().apply { timeInMillis = cita.horaInicio + cita.duracion * 60 * 1000 }
//
//        (slotStart.before(citaEnd) && slotEnd.after(citaStart)) ||
//                (slotEnd.after(citaStart) && slotStart.before(citaEnd)) ||
//                (min(slotStart.timeInMillis - citaEnd.timeInMillis, citaStart.timeInMillis - slotEnd.timeInMillis) < 5 * 60 * 1000)
//    }
//}

/*
package com.jmgtumat.pacapps.clientmod

import android.util.Log
import com.jmgtumat.pacapps.data.Cita
import com.jmgtumat.pacapps.data.CitaEstado
import com.jmgtumat.pacapps.data.Empleado
import com.jmgtumat.pacapps.data.HorariosPorDia
import com.jmgtumat.pacapps.data.Servicio
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// Mapear los nombres de los días de la semana a los utilizados en el diálogo de horario
val dayOfWeekMap = mapOf(
    Calendar.MONDAY to "Lunes",
    Calendar.TUESDAY to "Martes",
    Calendar.WEDNESDAY to "Miércoles",
    Calendar.THURSDAY to "Jueves",
    Calendar.FRIDAY to "Viernes",
    Calendar.SATURDAY to "Sábado"
)

fun calculateAvailableDates(servicio: Servicio, citas: List<Cita>, empleados: List<Empleado>): List<Calendar> {
    Log.d("NewAppointmentScreen", "Calculating available dates for service: ${servicio.nombre}")

    val availableDates = mutableListOf<Calendar>()
    val today = Calendar.getInstance()

    // Vamos a permitir las fechas para los próximos 30 días
    for (i in 0..30) {
        val date = (today.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, i) }
        val dayOfWeek = dayOfWeekMap[date.get(Calendar.DAY_OF_WEEK)]
        Log.d("NewAppointmentScreen", "Checking date: ${date.time}, Day of week: $dayOfWeek")

        if (dayOfWeek != null) {
            // Obtener los empleados que trabajen en ese día
            val workingEmployees = empleados.filter { empleado ->
                empleado.horariosTrabajo[dayOfWeek]?.manana?.disponible == true || empleado.horariosTrabajo[dayOfWeek]?.tarde?.disponible == true
            }
            Log.d("NewAppointmentScreen", "Working employees for $dayOfWeek: ${workingEmployees.size}")

            if (workingEmployees.isNotEmpty()) {
                availableDates.add(date)
            }
        }
    }

    Log.d("NewAppointmentScreen", "Available dates calculated: ${availableDates.size}")
    return availableDates
}

fun calculateAvailableSlots(date: Calendar, servicio: Servicio, citas: List<Cita>, empleados: List<Empleado>): List<Calendar> {
    Log.d("NewAppointmentScreen", "Calculating available slots for date: ${date.time} and service: ${servicio.nombre}")

    val availableSlots = mutableListOf<Calendar>()
    val dayOfWeek = dayOfWeekMap[date.get(Calendar.DAY_OF_WEEK)]

    if (dayOfWeek == null) return emptyList()

    empleados.forEach { empleado ->
        val workSchedule = empleado.horariosTrabajo[dayOfWeek]
        if (workSchedule != null) {
            Log.d("NewAppointmentScreen", "Working schedule for ${empleado.nombre}: $workSchedule")
            availableSlots.addAll(calculateAvailableSlotsForEmployee(date, citas, workSchedule))
        } else {
            Log.d("NewAppointmentScreen", "No working schedule for ${empleado.nombre} on $dayOfWeek")
        }
    }

    Log.d("NewAppointmentScreen", "Available slots calculated: ${availableSlots.size}")
    return availableSlots
}

private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

private fun calculateAvailableSlotsForEmployee(date: Calendar, citas: List<Cita>, horariosPorDia: HorariosPorDia): List<Calendar> {
    val availableSlots = mutableListOf<Calendar>()
    val intervals = listOf(horariosPorDia.manana, horariosPorDia.tarde)

    intervals.forEach { intervalo ->
        if (intervalo.disponible) {
            val intervalStart = Calendar.getInstance().apply {
                time = date.time
                set(Calendar.HOUR_OF_DAY, timeFormat.parse(intervalo.horaInicio).hours)
                set(Calendar.MINUTE, timeFormat.parse(intervalo.horaInicio).minutes)
            }

            val intervalEnd = Calendar.getInstance().apply {
                time = date.time
                set(Calendar.HOUR_OF_DAY, timeFormat.parse(intervalo.horaFin).hours)
                set(Calendar.MINUTE, timeFormat.parse(intervalo.horaFin).minutes)
            }

            var currentSlot = intervalStart.clone() as Calendar
            while (currentSlot.before(intervalEnd)) {
                val slotEnd = minOf((currentSlot.clone() as Calendar).apply {
                    add(Calendar.MINUTE, 20) // Duración fija de 20 minutos
                }, intervalEnd)

                if (isSlotAvailable(currentSlot, slotEnd, citas)) {
                    availableSlots.add(currentSlot.clone() as Calendar)
                }

                currentSlot.add(Calendar.MINUTE, 20) // Avanzar al siguiente intervalo de tiempo de 20 minutos
            }
        }
    }

    return availableSlots
}

private fun isSlotAvailable(slotStart: Calendar, slotEnd: Calendar, citas: List<Cita>): Boolean {
    return citas.none { cita ->
        val citaStart = Calendar.getInstance().apply { timeInMillis = cita.horaInicio }
        val citaEnd = Calendar.getInstance().apply { timeInMillis = cita.horaInicio + cita.duracion * 60 * 1000 }
        cita.estado == CitaEstado.PENDIENTE &&
                (slotStart.before(citaEnd) && slotEnd.after(citaStart))
    }
}

fun isSameDay(date1: Long, date2: Calendar): Boolean {
    val calendar1 = Calendar.getInstance().apply { timeInMillis = date1 }
    return calendar1.get(Calendar.YEAR) == date2.get(Calendar.YEAR) &&
            calendar1.get(Calendar.DAY_OF_YEAR) == date2.get(Calendar.DAY_OF_YEAR)
}
*/
