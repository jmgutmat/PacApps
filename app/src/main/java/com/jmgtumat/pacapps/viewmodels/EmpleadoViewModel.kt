package com.jmgtumat.pacapps.viewmodels

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.activity.result.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.jmgtumat.pacapps.data.Empleado
import com.jmgtumat.pacapps.repository.EmpleadoRepository
import com.jmgutmat.pacapps.repository.EmpleadoRepository
import kotlinx.coroutines.launch

class EmpleadoViewModel(private val empleadoRepository: EmpleadoRepository) :
    ViewModel() {
    val empleados: LiveData<List<Empleado>> = empleadoRepository.getAllEmpleados().asLiveData()

    fun insertEmpleado(empleado: Empleado) {
        viewModelScope.launch {
            empleadoRepository.insertEmpleado(empleado)
        }
    }

    // Other methods for updating, deleting, and getting empleados
}