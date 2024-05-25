package com.jmgtumat.pacapps.data

open class User(
    open val id: String,
    open val nombre: String,
    open val apellidos: String,
    open val telefono: String,
    open val correoElectronico: String,
    open val rol: UserRole
)
enum class UserRole {
    CLIENTE,
    EMPLEADO,
    ADMINISTRADOR
}
