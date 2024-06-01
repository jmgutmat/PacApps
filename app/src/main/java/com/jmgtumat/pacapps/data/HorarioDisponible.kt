package com.jmgtumat.pacapps.data

// Clase para representar la disponibilidad de horarios para un día específico
data class HorarioDisponible(
    val horariosPorDia: Map<String, HorariosPorDia> = emptyMap()
)


// Clase para representar los horarios disponibles por día
data class HorariosPorDia(
    val manana: Intervalo = Intervalo("", "", false),
    val tarde: Intervalo = Intervalo("", "", false)
)

// Clase para representar un intervalo de tiempo
data class Intervalo(
    var horaInicio: String = "",
    var horaFin: String = "",
    val disponible: Boolean = false
)
