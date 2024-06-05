package com.jmgtumat.pacapps.data

data class Cliente(
    override val id: String = "",
    override val nombre: String = "",
    override val apellidos: String = "",
    override val telefono: String = "",
    override val correoElectronico: String = "",
    val historialCitas: List<String> = emptyList(),
    override val rol: UserRole = UserRole.CLIENTE
) : User(id, nombre, apellidos, telefono, correoElectronico, rol)
