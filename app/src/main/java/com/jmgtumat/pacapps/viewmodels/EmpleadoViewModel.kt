package com.jmgtumat.pacapps.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jmgtumat.pacapps.data.Cita
import com.jmgtumat.pacapps.data.Empleado
import com.jmgtumat.pacapps.data.HorariosPorDia
import com.jmgtumat.pacapps.repository.EmpleadoRepository
import kotlinx.coroutines.launch

/**
 * ViewModel para manejar la lógica relacionada con los empleados.
 */
class EmpleadoViewModel(private val empleadoRepository: EmpleadoRepository) : BaseViewModel() {

    private val _empleados = MutableLiveData<List<Empleado>>()
    val empleados: LiveData<List<Empleado>> get() = _empleados

    private val _horariosTrabajo = MutableLiveData<Map<String, HorariosPorDia>?>()
    val horariosTrabajo: LiveData<Map<String, HorariosPorDia>?> get() = _horariosTrabajo

    private val _empleadoId = MutableLiveData<String>()
    val empleadoId: LiveData<String> get() = _empleadoId

    private val _citasAsignadas = MutableLiveData<List<Cita>>()
    val citasAsignadas: LiveData<List<Cita>> get() = _citasAsignadas

    init {
        _empleadoId.value = getAuthenticatedEmpleadoId()
        fetchEmpleados()
    }

    /**
     * Recupera la lista de empleados.
     */
    fun fetchEmpleados() {
        viewModelScope.launch {
            setLoading()
            try {
                val fetchedEmpleados = empleadoRepository.getEmpleados()
                _empleados.value = fetchedEmpleados
                setSuccess()
                fetchedEmpleados.firstOrNull()?.let { empleado ->
                    Log.d("EmpleadoViewModel", "Horarios de trabajo del primer empleado: ${empleado.horariosTrabajo}")
                }
            } catch (e: Exception) {
                setError(e.message)
            }
        }
    }

    /**
     * Obtiene el ID del empleado autenticado.
     */
    fun getAuthenticatedEmpleadoId(): String {
        return empleadoRepository.getAuthenticatedEmpleadoId()
    }

    /**
     * Recupera las citas asignadas a un empleado.
     * @param empleadoId ID del empleado.
     */
    fun fetchCitasAsignadas(empleadoId: String) {
        viewModelScope.launch {
            val citas = empleadoRepository.getCitasAsignadas(empleadoId)
            _citasAsignadas.value = citas
        }
    }

    /**
     * Inserta un nuevo empleado.
     * @param empleado Empleado a insertar.
     */
    fun insertEmpleado(empleado: Empleado) {
        viewModelScope.launch {
            try {
                empleadoRepository.addEmpleado(empleado)
                fetchEmpleados()
            } catch (e: Exception) {
                setError(e.message)
            }
        }
    }

    /**
     * Actualiza los datos de un empleado.
     * @param empleado Empleado a actualizar.
     */
    fun updateEmpleado(empleado: Empleado) {
        viewModelScope.launch {
            try {
                empleadoRepository.updateEmpleado(empleado)
                fetchEmpleados()
            } catch (e: Exception) {
                setError(e.message)
            }
        }
    }

    /**
     * Elimina un empleado.
     * @param empleadoId ID del empleado a eliminar.
     */
    fun deleteEmpleado(empleadoId: String) {
        viewModelScope.launch {
            try {
                empleadoRepository.deleteEmpleado(empleadoId)
                fetchEmpleados()
            } catch (e: Exception) {
                setError(e.message)
            }
        }
    }

    /**
     * Recupera los horarios de trabajo de un empleado.
     * @param empleadoId ID del empleado.
     */
    fun fetchHorariosTrabajo(empleadoId: String) {
        viewModelScope.launch {
            try {
                Log.d("EmpleadoViewModel", "Recuperando horarios de trabajo para empleadoId: $empleadoId")
                val horarioTrabajo = empleadoRepository.getHorariosTrabajo(empleadoId)
                Log.d("EmpleadoViewModel", "Horarios de trabajo obtenidos: $horarioTrabajo")
                _horariosTrabajo.value = horarioTrabajo
                setSuccess()
            } catch (e: Exception) {
                setError(e.message)
                Log.d("EmpleadoViewModel", "Error al recuperar horarios de trabajo: ${e.message}")
            }
        }
    }

    /**
     * Actualiza los horarios de trabajo de un empleado.
     * @param empleadoId ID del empleado.
     * @param horariosTrabajo Mapa que contiene los horarios de trabajo por día.
     */
    fun updateHorariosTrabajo(empleadoId: String, horariosTrabajo: Map<String, HorariosPorDia>) {
        viewModelScope.launch {
            try {
                empleadoRepository.updateHorariosTrabajo(empleadoId, horariosTrabajo)
            } catch (e: Exception) {
                setError(e.message)
            }
        }
    }
}

/**
 * Factoría para crear instancias de EmpleadoViewModel.
 * @param empleadoRepository Repositorio de empleados.
 */
class EmpleadoViewModelFactory(private val empleadoRepository: EmpleadoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EmpleadoViewModel::class.java)) {
            return EmpleadoViewModel(empleadoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}