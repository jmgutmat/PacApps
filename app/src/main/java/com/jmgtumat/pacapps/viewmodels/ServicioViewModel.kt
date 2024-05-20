package com.jmgtumat.pacapps.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmgtumat.pacapps.data.Servicio
import com.jmgtumat.pacapps.repository.ServicioRepository
import kotlinx.coroutines.launch

class ServicioViewModel(private val servicioRepository: ServicioRepository) :
    ViewModel() {

    private val _servicios = MutableLiveData<List<Servicio>>()
    val servicios: LiveData<List<Servicio>> get() = _servicios

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    init {
        fetchServicios()
    }

    private fun fetchServicios() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val fetchedServicios = servicioRepository.getServicios()
                _servicios.value = fetchedServicios
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun insertServicio(servicio: Servicio) {
        viewModelScope.launch {
            try {
                servicioRepository.addServicio(servicio)
                fetchServicios() // Refresh the service list after adding a new service
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun updateServicio(servicio: Servicio) {
        viewModelScope.launch {
            try {
                servicioRepository.updateServicio(servicio)
                fetchServicios() // Refresh the service list after updating a service
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun deleteServicio(servicioId: String) {
        viewModelScope.launch {
            try {
                servicioRepository.deleteServicio(servicioId)
                fetchServicios() // Refresh the service list after deleting a service
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
