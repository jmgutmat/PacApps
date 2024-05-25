package com.jmgtumat.pacapps.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jmgtumat.pacapps.data.Empleado
import com.jmgtumat.pacapps.data.HorarioDisponible
import com.jmgtumat.pacapps.repository.EmpleadoRepository
import kotlinx.coroutines.launch

class EmpleadoViewModel(private val empleadoRepository: EmpleadoRepository) : BaseViewModel() {

    private val _empleados = MutableLiveData<List<Empleado>>()
    val empleados: LiveData<List<Empleado>> get() = _empleados

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

    fun getHorarioTrabajo(empleadoId: String): LiveData<Map<String, HorarioDisponible>?> {
        val horarioLiveData = MutableLiveData<Map<String, HorarioDisponible>?>()
        viewModelScope.launch {
            try {
                val horarioTrabajo = empleadoRepository.getHorarioTrabajo(empleadoId)
                horarioLiveData.value = horarioTrabajo
                setSuccess()
            } catch (e: Exception) {
                setError(e.message)
            }
        }
        return horarioLiveData
    }

    fun updateHorarioTrabajo(empleadoId: String, horarioTrabajo: Map<String, HorarioDisponible>) {
        viewModelScope.launch {
            try {
                empleadoRepository.updateHorarioTrabajo(empleadoId, horarioTrabajo)
            } catch (e: Exception) {
                setError(e.message)
            }
        }
    }
}