package com.jmgtumat.pacapps.data

data class Empleado(
    val id: String,
    val nombre: String,
    val apellidos: String,
    val cargo: String,
    val horarioDisponible: List<Horario>,
    val citasAsignadas: List<Cita>
)

data class Horario(
    val diaSemana: String,
    val horaInicio: String,
    val horaFin: String
)
