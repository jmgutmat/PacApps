package com.jmgtumat.pacapps.data

import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName

/**
 * Clase base que representa a un usuario en la aplicación.
 * Contiene información básica como el ID, nombre, apellidos, teléfono, correo electrónico y rol del usuario.
 *
 * @property id El identificador único del usuario.
 * @property nombre El nombre del usuario.
 * @property apellidos Los apellidos del usuario.
 * @property telefono El número de teléfono del usuario.
 * @property correoElectronico El correo electrónico del usuario.
 * @property rol El rol del usuario en la aplicación.
 * @constructor Crea un nuevo objeto [User] con los detalles especificados.
 */
@IgnoreExtraProperties
open class User(
    @get:PropertyName("id")
    open val id: String = "",
    @get:PropertyName("nombre")
    open val nombre: String = "",
    @get:PropertyName("apellidos")
    open val apellidos: String = "",
    @get:PropertyName("telefono")
    open val telefono: String = "",
    @get:PropertyName("correoElectronico")
    open val correoElectronico: String = "",
    @get:PropertyName("rol")
    open val rol: UserRole = UserRole.CLIENTE
)
/**
 * Enumeración que representa los roles de usuario en la aplicación.
 */
enum class UserRole {
    CLIENTE,
    EMPLEADO,
    ADMINISTRADOR
}
