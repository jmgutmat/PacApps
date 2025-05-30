package com.jmgtumat.pacapps.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmgtumat.pacapps.data.Cita
import com.jmgtumat.pacapps.data.Empleado
import com.jmgtumat.pacapps.data.Servicio
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

    // Ahora observable
    private val _selectedServices = MutableStateFlow<List<Servicio>>(emptyList())
    val selectedServices: StateFlow<List<Servicio>> = _selectedServices

    var selectedDate: LocalDate? = null
    var selectedTime: String? = null

    private val empleadoIdDefault = "tfD2pxzZpddHklnx7d1Dbq7xYlp1"

    fun setSelectedServices(services: List<Servicio>) {
        _selectedServices.value = services
    }

    fun calcularHorasDisponibles() {
        if (selectedDate == null) return

        viewModelScope.launch {
            try {
                val empleados = empleadoRepository.getEmpleados()
                val empleado: Empleado? = empleados.find { it.id == empleadoIdDefault }
                if (empleado == null || empleado.horariosTrabajo.isEmpty()) {
                    _horasDisponibles.value = emptyList()
                    return@launch
                }

                val diasSemanaMap = mapOf(
                    "MONDAY" to "Lunes",
                    "TUESDAY" to "Martes",
                    "WEDNESDAY" to "Miércoles",
                    "THURSDAY" to "Jueves",
                    "FRIDAY" to "Viernes",
                    "SATURDAY" to "Sábado",
                    "SUNDAY" to "Domingo"
                )
                val diaSemanaIngles = selectedDate!!.dayOfWeek.name
                val diaSemana = diasSemanaMap[diaSemanaIngles] ?: diaSemanaIngles

                val horarios = empleado.horariosTrabajo[diaSemana] ?: run {
                    _horasDisponibles.value = emptyList()
                    return@launch
                }

                if (diaSemana == "Domingo") {
                    _horasDisponibles.value = emptyList()
                    return@launch
                }

                val citasDia: List<Cita> = citaRepository.getCitasByEmpleadoIdAndDate(
                    empleadoIdDefault,
                    selectedDate!!.toEpochDay()
                )

                val duracionTotal = _selectedServices.value.sumOf { it.duracion }

                val horasDisponibles = mutableListOf<String>()
                val formato = DateTimeFormatter.ofPattern("HH:mm")

                listOf(horarios.manana, horarios.tarde).forEach { intervalo ->
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

                _horasDisponibles.value = horasDisponibles
            } catch (e: Exception) {
                _horasDisponibles.value = emptyList()
            }
        }
    }
}
