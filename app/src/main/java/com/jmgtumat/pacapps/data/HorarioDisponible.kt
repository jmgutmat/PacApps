package com.jmgtumat.pacapps.data

// Clase para representar la disponibilidad de horarios para un día específico
data class HorarioDisponible(
    val horariosPorDia: Map<String, HorariosPorDia>
)

// Clase para representar los horarios disponibles por día
data class HorariosPorDia(
    val manana: Intervalo,
    val tarde: Intervalo
)

// Clase para representar un intervalo de tiempo
data class Intervalo(
    val horaInicio: String,
    val horaFin: String,
    val disponible: Boolean
)
