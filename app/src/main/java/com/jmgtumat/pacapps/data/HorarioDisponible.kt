package com.jmgtumat.pacapps.data

/**
 * Clase que representa los horarios disponibles por día para un empleado.
 * Contiene información sobre los horarios de trabajo disponibles para la mañana y la tarde.
 *
 * @property manana El intervalo de horario disponible por la mañana.
 * @property tarde El intervalo de horario disponible por la tarde.
 * @constructor Crea un nuevo objeto [HorariosPorDia] con los horarios especificados.
 */
data class HorariosPorDia(
    val manana: Intervalo = Intervalo("", "", false),
    val tarde: Intervalo = Intervalo("", "", false)
)

/**
 * Clase que representa un intervalo de tiempo.
 * Contiene la hora de inicio, la hora de finalización y un indicador de disponibilidad.
 *
 * @property horaInicio La hora de inicio del intervalo.
 * @property horaFin La hora de finalización del intervalo.
 * @property disponible Indica si el intervalo está disponible o no.
 * @constructor Crea un nuevo objeto [Intervalo] con la hora de inicio, la hora de finalización y la disponibilidad especificadas.
 */
data class Intervalo(
    var horaInicio: String = "",
    var horaFin: String = "",
    val disponible: Boolean = false
)

/*// Clase para representar la disponibilidad de horarios para un día específico
data class HorarioDisponible(
    val horariosPorDia: Map<String, HorariosPorDia> = emptyMap()
)*/