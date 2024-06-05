package com.jmgtumat.pacapps.data

/**
 * Clase que representa un servicio ofrecido por la aplicación.
 * Contiene información como el nombre, la descripción, la duración en minutos y el precio del servicio.
 *
 * @property id El identificador único del servicio.
 * @property nombre El nombre del servicio.
 * @property descripcion La descripción del servicio.
 * @property duracion La duración del servicio en minutos.
 * @property precio El precio del servicio.
 * @constructor Crea un nuevo objeto [Servicio] con los detalles especificados.
 */
data class Servicio(
    var id: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val duracion: Int = 0,
    val precio: Double = 0.0
)
