package com.jmgtumat.pacapps.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.jmgtumat.pacapps.data.Cita
import com.jmgutmat.pacapps.repository.CitaRepository
import kotlinx.coroutines.launch

class CitaViewModel(private val citaRepository: CitaRepository) : ViewModel() {
    val citas: LiveData<List<Cita>> = citaRepository.getAllCitas().asLiveData()

    fun insertCita(cita: Cita) {
        viewModelScope.launch {
            citaRepository.insertCita(cita)
        }
    }

    // Other methods for updating, deleting, and getting citas
}