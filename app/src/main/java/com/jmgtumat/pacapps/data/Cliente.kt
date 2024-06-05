package com.jmgtumat.pacapps.data

/**
 * Clase de datos que representa a un cliente en el sistema.
 * Un cliente es un usuario que puede reservar citas y acceder a servicios proporcionados por la aplicación.
 *
 * @property id El ID único del cliente.
 * @property nombre El nombre del cliente.
 * @property apellidos Los apellidos del cliente.
 * @property telefono El número de teléfono del cliente.
 * @property correoElectronico El correo electrónico del cliente.
 * @property historialCitas El historial de citas del cliente, representado por una lista de IDs de citas.
 * @property rol El rol del cliente, que siempre es [UserRole.CLIENTE].
 * @constructor Crea un nuevo cliente con los detalles especificados.
 */
data class Cliente(
    override val id: String = "",
    override val nombre: String = "",
    override val apellidos: String = "",
    override val telefono: String = "",
    override val correoElectronico: String = "",
    val historialCitas: List<String> = emptyList(),
    override val rol: UserRole = UserRole.CLIENTE
) : User(id, nombre, apellidos, telefono, correoElectronico, rol)
