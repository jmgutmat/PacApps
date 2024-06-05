package com.jmgtumat.pacapps.data

/**
 * Clase de datos que representa a un empleado en el sistema.
 * Un empleado es un usuario que proporciona servicios y puede tener citas asignadas.
 *
 * @property id El ID único del empleado.
 * @property nombre El nombre del empleado.
 * @property apellidos Los apellidos del empleado.
 * @property telefono El número de teléfono del empleado.
 * @property correoElectronico El correo electrónico del empleado.
 * @property horariosTrabajo Los horarios de trabajo del empleado, representados por un mapa de días de la semana a horarios disponibles.
 * @property citasAsignadas Las citas asignadas al empleado, representadas por una lista de IDs de citas.
 * @property rol El rol del empleado, que siempre es [UserRole.EMPLEADO].
 * @constructor Crea un nuevo empleado con los detalles especificados.
 */
data class Empleado(
    override val id: String = "",
    override val nombre: String = "",
    override val apellidos: String = "",
    override val telefono: String = "",
    override val correoElectronico: String = "",
    val horariosTrabajo: Map<String, HorariosPorDia> = emptyMap(),
    val citasAsignadas: List<String> = emptyList(),
    override val rol: UserRole = UserRole.EMPLEADO
) : User(id, nombre, apellidos, telefono, correoElectronico, rol)
