package com.jmgtumat.pacapps.data

data class Cliente(
    val id: String = "", // Modificado para que el ID pueda generarse automáticamente
    val nombre: String,
    val apellidos: String,
    val telefono: String,
    val correoElectronico: String,
    val historialCitas: List<Cita>
)
