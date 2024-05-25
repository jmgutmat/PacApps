package com.jmgtumat.pacapps.data

data class Administrador(
    override val id: String = "",
    override val nombre: String,
    override val apellidos: String,
    override val telefono: String,
    override val correoElectronico: String,
    override val rol: UserRole = UserRole.ADMINISTRADOR
) : User(id, nombre, apellidos, telefono, correoElectronico, rol)

