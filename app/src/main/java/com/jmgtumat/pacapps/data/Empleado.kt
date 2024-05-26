package com.jmgtumat.pacapps.data

data class Empleado(
    override val id: String = "",
    override val nombre: String,
    override val apellidos: String,
    override val telefono: String,
    override val correoElectronico: String,
    val horariosTrabajo: Map<String, HorariosPorDia>, // Mapa de días de la semana a horarios disponibles
    val citasAsignadas: List<Cita>,
    override val rol: UserRole = UserRole.EMPLEADO
) : User(id, nombre, apellidos, telefono, correoElectronico, rol)
