package com.jmgtumat.pacapps.viewmodels


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmgtumat.pacapps.data.Empleado
import com.jmgtumat.pacapps.repository.EmpleadoRepository
import kotlinx.coroutines.launch

class EmpleadoViewModel(private val empleadoRepository: EmpleadoRepository) :
    ViewModel() {

    private val _empleados = MutableLiveData<List<Empleado>>()
    val empleados: LiveData<List<Empleado>> get() = _empleados

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    init {
        fetchEmpleados()
    }

    private fun fetchEmpleados() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val fetchedEmpleados = empleadoRepository.getEmpleados()
                _empleados.value = fetchedEmpleados
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun insertEmpleado(empleado: Empleado) {
        viewModelScope.launch {
            try {
                empleadoRepository.addEmpleado(empleado)
                fetchEmpleados() // Refresh the employee list after adding a new employee
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    // Other methods for updating, deleting, and getting employees
}
