package com.jmgtumat.pacapps.viewmodels

import CitaRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmgtumat.pacapps.data.Cita
import com.jmgtumat.pacapps.data.CitaEstado
import com.jmgtumat.pacapps.data.Servicio
import kotlinx.coroutines.launch

class CitaViewModel(private val citaRepository: CitaRepository) : ViewModel() {

    private val _fechasDisponibles = MutableLiveData<List<Long>>()
    val fechasDisponibles: LiveData<List<Long>> get() = _fechasDisponibles

    private val _horasDisponibles = MutableLiveData<List<String>>()
    val horasDisponibles: LiveData<List<String>> get() = _horasDisponibles

    private val _selectedFecha = MutableLiveData<Long>()
    val selectedFecha: LiveData<Long> get() = _selectedFecha

    private val _selectedHora = MutableLiveData<String>()
    val selectedHora: LiveData<String> get() = _selectedHora

    private val _selectedServicio = MutableLiveData<Servicio>()
    val selectedServicio: LiveData<Servicio> get() = _selectedServicio

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun fetchFechasDisponibles(servicioId: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val fechas = citaRepository.getFechasDisponibles(servicioId)
                _fechasDisponibles.value = fechas
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun fetchHorasDisponibles(servicioId: String, fechaSeleccionada: Long) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val horas = citaRepository.getHorasDisponibles(servicioId, fechaSeleccionada)
                _horasDisponibles.value = horas
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun selectFecha(fecha: Long) {
        _selectedFecha.value = fecha
    }

    fun selectHora(hora: String) {
        _selectedHora.value = hora
    }

    fun selectServicio(servicio: Servicio) {
        _selectedServicio.value = servicio
    }

    fun confirmarCita(clienteId: String, empleadoId: String) {
        val fechaSeleccionada = _selectedFecha.value ?: return
        val horaSeleccionada = _selectedHora.value ?: return
        val servicio = _selectedServicio.value ?: return

        val cita = Cita(
            clienteId = clienteId,
            empleadoId = empleadoId,
            servicioId = servicio.id,
            fecha = fechaSeleccionada,
            horaInicio = calcularHoraInicio(fechaSeleccionada, horaSeleccionada),
            duracion = servicio.duracion,
            estado = CitaEstado.PENDIENTE,
            notas = null
        )

        viewModelScope.launch {
            try {
                citaRepository.insertarCita(cita)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    private fun calcularHoraInicio(fecha: Long, hora: String): Long {
        // Implementa la lógica para calcular el timestamp de inicio de la cita
        // Combina la fecha seleccionada con la hora seleccionada
        return 0L // Reemplaza con la implementación real
    }

    // Función para obtener las citas del cliente
    fun getCitasCliente(clienteId: String): LiveData<List<Cita>> {
        return citaRepository.getCitasCliente(clienteId)
    }

    // Función para obtener las citas del empleado
    fun getCitasEmpleado(empleadoId: String): LiveData<List<Cita>> {
        return citaRepository.getCitasEmpleado(empleadoId)
    }

    // Función para cancelar una cita
    fun cancelarCita(citaId: String) {
        viewModelScope.launch {
            try {
                citaRepository.cancelarCita(citaId)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}

