package com.jmgtumat.pacapps.data

data class Empleado(
    val id: String = "", // Modificado para que el ID pueda generarse automáticamente
    val nombre: String,
    val apellidos: String,
    val cargo: String,
    val horarioDisponible: List<Horario>,
    val citasAsignadas: List<Cita>
)

