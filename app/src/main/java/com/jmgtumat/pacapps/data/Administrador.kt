package com.jmgtumat.pacapps.data

/**
 * Clase de datos que representa a un administrador en el sistema.
 * Extiende la clase [User] y agrega el rol de [UserRole.ADMINISTRADOR].
 *
 * @property id El ID del administrador.
 * @property nombre El nombre del administrador.
 * @property apellidos Los apellidos del administrador.
 * @property telefono El número de teléfono del administrador.
 * @property correoElectronico El correo electrónico del administrador.
 * @property rol El rol del administrador, que siempre es [UserRole.ADMINISTRADOR].
 * @constructor Crea un nuevo administrador con los detalles especificados.
 */
data class Administrador(
    override val id: String = "",
    override val nombre: String,
    override val apellidos: String,
    override val telefono: String,
    override val correoElectronico: String,
    override val rol: UserRole = UserRole.ADMINISTRADOR
) : User(id, nombre, apellidos, telefono, correoElectronico, rol)
