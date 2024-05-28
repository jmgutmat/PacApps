package com.jmgtumat.pacapps.data

import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName

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

enum class UserRole {
    CLIENTE,
    EMPLEADO,
    ADMINISTRADOR
}
