package com.jmgtumat.pacapps.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jmgtumat.pacapps.data.Cita
import com.jmgtumat.pacapps.repository.CitaRepository
import kotlinx.coroutines.launch

class CitaViewModel(private val citaRepository: CitaRepository) : BaseViewModel() {

    private val _citas = MutableLiveData<List<Cita>>()
    val citas: LiveData<List<Cita>> get() = _citas

    init {
        fetchCitas()
    }

    private fun fetchCitas() {
        viewModelScope.launch {
            setLoading()
            try {
                val fetchedCitas = citaRepository.getCitas()
                _citas.value = fetchedCitas
                setSuccess()
            } catch (e: Exception) {
                setError(e.message)
            }
        }
    }

    fun insertCita(cita: Cita) {
        viewModelScope.launch {
            try {
                citaRepository.addCita(cita)
                fetchCitas()
            } catch (e: Exception) {
                setError(e.message)
            }
        }
    }

    fun updateCita(cita: Cita) {
        viewModelScope.launch {
            try {
                citaRepository.updateCita(cita)
                fetchCitas()
            } catch (e: Exception) {
                setError(e.message)
            }
        }
    }

    fun deleteCita(citaId: String) {
        viewModelScope.launch {
            try {
                citaRepository.deleteCita(citaId)
                fetchCitas()
            } catch (e: Exception) {
                setError(e.message)
            }
        }
    }
}
