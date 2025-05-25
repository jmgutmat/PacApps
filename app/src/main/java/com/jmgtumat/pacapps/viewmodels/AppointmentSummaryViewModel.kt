package com.jmgtumat.pacapps.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmgtumat.pacapps.data.Cita
import com.jmgtumat.pacapps.data.Empleado
import com.jmgtumat.pacapps.repository.CitaRepository
import com.jmgtumat.pacapps.repository.EmpleadoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class AppointmentSummaryViewModel(
    private val citaRepository: CitaRepository = CitaRepository(),
    private val empleadoRepository: EmpleadoRepository = EmpleadoRepository()
) : ViewModel() {

    private val _horasDisponibles = MutableStateFlow<List<String>>(emptyList())
    val horasDisponibles: StateFlow<List<String>> = _horasDisponibles

    var selectedServices: List<String> = emptyList()
    var selectedDate: LocalDate? = null
    var selectedTime: String? = null

    private val empleadoIdDefault = "-OL_6myd8kEiS60lWykm" // Francisco Reina Gil

    fun calcularHorasDisponibles() {
        if (selectedDate == null) return

        viewModelScope.launch {
            try {
                Log.d("AppointmentVM", "Calculando horas disponibles para: $selectedDate")
                val empleados = empleadoRepository.getEmpleados()
                val empleado: Empleado? = empleados.find { it.id == empleadoIdDefault }
                Log.d("AppointmentVM", "Empleado obtenido: ${empleado?.nombre}")

                if (empleado == null || empleado.horariosTrabajo.isEmpty()) {
                    Log.d("AppointmentVM", "No hay horarios definidos para el empleado.")
                    _horasDisponibles.value = emptyList()
                    return@launch
                }

                val diaSemana = selectedDate!!.dayOfWeek.name
                val horarios = empleado.horariosTrabajo[diaSemana] ?: run {
                    Log.d("AppointmentVM", "No hay horarios para el día $diaSemana")
                    _horasDisponibles.value = emptyList()
                    return@launch
                }

                val citasDia: List<Cita> = citaRepository.getCitasByEmpleadoIdAndDate(
                    empleadoIdDefault,
                    selectedDate!!.toEpochDay()
                )
                Log.d("AppointmentVM", "Citas del día: ${citasDia.size}")

                val duracionTotal = selectedServices.sumOf { it.toIntOrNull() ?: 0 }
                Log.d("AppointmentVM", "Duración total de los servicios: $duracionTotal min")

                val horasDisponibles = mutableListOf<String>()
                val formato = DateTimeFormatter.ofPattern("HH:mm")

                listOfNotNull(horarios.manana, horarios.tarde).forEach { intervalo ->
                    if (!intervalo.disponible) return@forEach

                    var horaActual = LocalTime.parse(intervalo.horaInicio, formato)
                    val horaFin = LocalTime.parse(intervalo.horaFin, formato)

                    while (horaActual.plusMinutes(duracionTotal.toLong()) <= horaFin) {
                        val choque = citasDia.any { cita ->
                            val citaInicio = LocalTime.ofSecondOfDay(cita.horaInicio)
                            val citaFin = citaInicio.plusMinutes(cita.duracion.toLong())
                            val nuevaCitaFin = horaActual.plusMinutes(duracionTotal.toLong())

                            (horaActual < citaFin && nuevaCitaFin > citaInicio)
                        }
                        if (!choque) {
                            horasDisponibles.add(horaActual.format(formato))
                        }

                        horaActual = horaActual.plusMinutes(30)
                    }
                }

                Log.d("AppointmentVM", "Horas disponibles finales: $horasDisponibles")
                _horasDisponibles.value = horasDisponibles
            } catch (e: Exception) {
                Log.e("AppointmentVM", "Error calculando horas disponibles: ${e.message}")
                _horasDisponibles.value = emptyList()
            }
        }
    }
}
