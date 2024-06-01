package com.jmgtumat.pacapps.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jmgtumat.pacapps.data.Empleado
import com.jmgtumat.pacapps.data.HorariosPorDia
import com.jmgtumat.pacapps.repository.EmpleadoRepository
import kotlinx.coroutines.launch

class EmpleadoViewModel(private val empleadoRepository: EmpleadoRepository) : BaseViewModel() {

    private val _empleados = MutableLiveData<List<Empleado>>()
    val empleados: LiveData<List<Empleado>> get() = _empleados

    private val _horariosTrabajo = MutableLiveData<Map<String, HorariosPorDia>?>()
    val horariosTrabajo: LiveData<Map<String, HorariosPorDia>?> get() = _horariosTrabajo

    init {
        fetchEmpleados()
    }

    private fun fetchEmpleados() {
        viewModelScope.launch {
            setLoading()
            try {
                val fetchedEmpleados = empleadoRepository.getEmpleados()
                _empleados.value = fetchedEmpleados
                setSuccess()
                // Log de los horarios de trabajo del primer empleado
                fetchedEmpleados.firstOrNull()?.let { empleado ->
                    Log.d("EmpleadoViewModel", "Horarios de trabajo del primer empleado: ${empleado.horariosTrabajo}")
                }
            } catch (e: Exception) {
                setError(e.message)
            }
        }
    }

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

    fun fetchHorariosTrabajo(empleadoId: String) {
        viewModelScope.launch {
            try {
                val horarioTrabajo = empleadoRepository.getHorariosTrabajo(empleadoId)
                _horariosTrabajo.value = horarioTrabajo
                setSuccess()
            } catch (e: Exception) {
                setError(e.message)
            }
        }
    }

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

class EmpleadoViewModelFactory(private val empleadoRepository: EmpleadoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EmpleadoViewModel::class.java)) {
            return EmpleadoViewModel(empleadoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
