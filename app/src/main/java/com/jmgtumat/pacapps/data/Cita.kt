package com.jmgtumat.pacapps.data

data class Cita(
    val id: String,
    val cliente: Cliente,
    val empleado: Empleado,
    val servicio: Servicio,
    val fechaHora: Long, // Timestamp en milisegundos
    val duracion: Int, // En minutos
    val estado: CitaEstado,
    val notas: String?
)

enum class CitaEstado {
    PENDIENTE, CONFIRMADA, CANCELADA
}
