package com.jmgtumat.pacapps.viewmodels

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.activity.result.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.jmgtumat.pacapps.data.Servicio
import com.jmgtumat.pacapps.repository.ServicioRepository
import com.jmgutmat.pacapps.repository.ServicioRepository
import kotlinx.coroutines.launch

class ServicioViewModel(private val servicioRepository: ServicioRepository) :
    ViewModel() {
    val servicios: LiveData<List<Servicio>> = servicioRepository.getAllServicios().asLiveData()

    fun insertServicio(servicio: Servicio) {
        viewModelScope.launch {
            servicioRepository.insertServicio(servicio)
        }
    }

    // Other methods for updating, deleting, and getting servicios
}