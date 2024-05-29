package com.jmgtumat.pacapps.data

data class Servicio(
    var id: String = "", // Modificado para que el ID pueda generarse automáticamente
    val nombre: String = "",
    val descripcion: String = "",
    val duracion: Int = 0, // En minutos
    val precio: Double = 0.0
)
