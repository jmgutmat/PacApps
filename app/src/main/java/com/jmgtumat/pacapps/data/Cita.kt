package com.jmgtumat.pacapps.data

/**
 * Clase de datos que representa una cita en el sistema.
 * Una cita se realiza entre un cliente y un empleado para un servicio específico en una fecha y hora determinadas.
 *
 * @property id El ID único de la cita.
 * @property clienteId El ID del cliente que realiza la cita.
 * @property empleadoId El ID del empleado que atenderá la cita.
 * @property servicioId El ID del servicio para el que se reserva la cita.
 * @property fecha La fecha de la cita en milisegundos (sin la hora exacta).
 * @property horaInicio La hora de inicio de la cita en milisegundos.
 * @property duracion La duración de la cita en minutos.
 * @property estado El estado actual de la cita, que puede ser [CitaEstado.PENDIENTE], [CitaEstado.CONFIRMADA],
 * [CitaEstado.CANCELADA] o [CitaEstado.DISPONIBLE].
 * @property notas Notas adicionales asociadas a la cita, si las hay.
 * @constructor Crea una nueva cita con los detalles especificados.
 */
data class Cita(
    var id: String = "",
    val clienteId: String = "",
    val empleadoId: String = "",
    val servicioId: String = "",
    val fecha: Long = 0L,
    val horaInicio: Long = 0L,
    val duracion: Int = 0,
    var estado: CitaEstado = CitaEstado.PENDIENTE,
    val notas: String? = null
)

/**
 * Enumeración que representa los posibles estados de una cita.
 * Una cita puede estar pendiente de confirmación, confirmada, cancelada o disponible.
 */
enum class CitaEstado {
    PENDIENTE, CONFIRMADA, CANCELADA, DISPONIBLE
}
