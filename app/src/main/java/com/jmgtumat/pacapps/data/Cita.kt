package com.jmgtumat.pacapps.data

data class Cita(
    val id: String = "", // Modificado para que el ID pueda generarse automáticamente
    val clienteId: String, // ID del cliente que realiza la cita
    val empleadoId: String, // ID del empleado que atenderá la cita
    val servicioId: String, // ID del servicio para el que se reserva la cita
    val fecha: Long, // Fecha de la cita en milisegundos (sin la hora exacta)
    val horaInicio: Long, // Hora de inicio de la cita en milisegundos
    val duracion: Int, // Duración de la cita en minutos
    var estado: CitaEstado,
    val notas: String? = null
)

enum class CitaEstado {
    PENDIENTE, CONFIRMADA, CANCELADA
}
