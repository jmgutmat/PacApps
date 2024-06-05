package com.jmgtumat.pacapps.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jmgtumat.pacapps.data.Servicio
import com.jmgtumat.pacapps.repository.ServicioRepository
import kotlinx.coroutines.launch

/**
 * ViewModel para la gestión de servicios.
 */
class ServicioViewModel(private val servicioRepository: ServicioRepository) : BaseViewModel() {

    private val _servicios = MutableLiveData<List<Servicio>>()
    val servicios: LiveData<List<Servicio>> get() = _servicios

    /**
     * Inicializa la carga de servicios al crear la instancia del ViewModel.
     */
    init {
        fetchServicios()
    }

    /**
     * Obtiene la lista de servicios desde el repositorio.
     */
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

    /**
     * Inserta un nuevo servicio en el repositorio.
     * @param servicio El servicio a insertar.
     */
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

    /**
     * Actualiza un servicio existente en el repositorio.
     * @param servicio El servicio a actualizar.
     */
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

    /**
     * Elimina un servicio del repositorio.
     * @param servicioId El ID del servicio a eliminar.
     */
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

/**
 * Factoría para crear instancias de ServicioViewModel.
 */
class ServicioViewModelFactory(private val servicioRepository: ServicioRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ServicioViewModel::class.java)) {
            return ServicioViewModel(servicioRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}