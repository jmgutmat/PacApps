package com.jmgtumat.pacapps.data

data class Servicio(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val duracion: Int, // En minutos
    val precio: Double
)
