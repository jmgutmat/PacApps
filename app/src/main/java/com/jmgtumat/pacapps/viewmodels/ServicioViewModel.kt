package com.jmgtumat.pacapps.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jmgtumat.pacapps.data.Servicio
import com.jmgtumat.pacapps.repository.ServicioRepository
import kotlinx.coroutines.launch

class ServicioViewModel(private val servicioRepository: ServicioRepository) : BaseViewModel() {

    private val _servicios = MutableLiveData<List<Servicio>>()
    val servicios: LiveData<List<Servicio>> get() = _servicios

    init {
        fetchServicios()
    }

    private fun fetchServicios() {
        viewModelScope.launch {
            setLoading()
            try {
                val fetchedServicios = servicioRepository.getServicios()
                _servicios.value = fetchedServicios
                setSuccess()
            } catch (e: Exception) {
                setError(e.message)
            }
        }
    }

    fun insertServicio(servicio: Servicio) {
        viewModelScope.launch {
            try {
                servicioRepository.addServicio(servicio)
                fetchServicios()
            } catch (e: Exception) {
                setError(e.message)
            }
        }
    }

    fun updateServicio(servicio: Servicio) {
        viewModelScope.launch {
            try {
                servicioRepository.updateServicio(servicio)
                fetchServicios()
            } catch (e: Exception) {
                setError(e.message)
            }
        }
    }

    fun deleteServicio(servicioId: String) {
        viewModelScope.launch {
            try {
                servicioRepository.deleteServicio(servicioId)
                fetchServicios()
            } catch (e: Exception) {
                setError(e.message)
            }
        }
    }
}
