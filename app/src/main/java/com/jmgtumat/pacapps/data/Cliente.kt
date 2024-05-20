package com.jmgtumat.pacapps.data

data class Cliente(
    val id: String,
    val nombre: String,
    val apellidos: String,
    val telefono: String,
    val correoElectronico: String,
    val historialCitas: List<Cita>
)
