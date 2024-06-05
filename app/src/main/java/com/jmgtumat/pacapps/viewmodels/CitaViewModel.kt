package com.jmgtumat.pacapps.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jmgtumat.pacapps.data.Cita
import com.jmgtumat.pacapps.data.CitaEstado
import com.jmgtumat.pacapps.repository.CitaRepository
import com.jmgtumat.pacapps.repository.ClienteRepository
import com.jmgtumat.pacapps.repository.EmpleadoRepository
import kotlinx.coroutines.launch

class CitaViewModel(private val citaRepository: CitaRepository,
                    private val clienteRepository: ClienteRepository,
                    private val empleadoRepository: EmpleadoRepository
) : BaseViewModel() {

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

    fun fetchCitasByDate(dateInMillis: Long) {
        viewModelScope.launch {
            setLoading()
            try {
                val fetchedCitas = citaRepository.getCitasByDate(dateInMillis)
                _citas.value = fetchedCitas
                setSuccess()
            } catch (e: Exception) {
                setError(e.message)
            }
        }
    }

    fun fetchCitasByEmpleadoId(empleadoId: String) {
        viewModelScope.launch {
            setLoading()
            try {
                val fetchedCitas = empleadoRepository.getCitasAsignadas(empleadoId)
                _citas.value = fetchedCitas
                setSuccess()
            } catch (e: Exception) {
                setError(e.message)
            }
        }
    }

    suspend fun fetchCitasByEmpleadoIdAndDate(empleadoId: String, dateInMillis: Long) {
        try {
            setLoading()
            val citas = citaRepository.getCitasByEmpleadoIdAndDate(empleadoId, dateInMillis)
            _citas.value = citas
            setSuccess()
        } catch (e: Exception) {
            setError(e.message)
        }
    }



    fun insertCita(cita: Cita, clienteId: String) {
        viewModelScope.launch {
            try {
                Log.d("CitaViewModel", "Fecha de la cita: ${cita.fecha}")
                val citaId = citaRepository.addCita(cita) // Agrega la cita y obtiene su ID
                cita.id = citaId // Asegura que la cita tenga el ID asignado
                fetchCitas()
                Log.d("CitaViewModel", "Cita añadida: $cita")

                // Obtener el historial de citas actual del cliente
                val historialCitas = clienteRepository.getHistorialCitas(clienteId).toMutableList()
                Log.d("CitaViewModel", "Historial de citas antes de agregar: $historialCitas")

                // Agregar la nueva cita al historial de citas
                historialCitas.add(cita)
                Log.d("CitaViewModel", "Historial de citas después de agregar: $historialCitas")

                // Actualizar el historial de citas del cliente con los IDs correctos
                clienteRepository.updateHistorialCitas(clienteId, historialCitas)
                Log.d("CitaViewModel", "Historial de citas actualizado para el cliente $clienteId")

                // Actualizar citas asignadas del empleado
                val empleadoId = cita.empleadoId
                val citasAsignadas = empleadoRepository.getCitasAsignadas(empleadoId).map { it.id }.toMutableList()
                citasAsignadas.add(cita.id)
                empleadoRepository.updateCitasAsignadas(empleadoId, citasAsignadas)
                Log.d("CitaViewModel", "Citas asignadas del empleado $empleadoId actualizadas")
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

//    fun deleteCita(citaId: String) {
//        viewModelScope.launch {
//            try {
//                citaRepository.deleteCita(citaId)
//                fetchCitas()
//            } catch (e: Exception) {
//                setError(e.message)
//            }
//        }
//    }

    suspend fun getCitaById(citaId: String): Cita {
        return citaRepository.getCitaById(citaId)
    }

    fun getCitasByDateRange(startDate: Long, endDate: Long): List<Cita> {
        val citas = _citas.value ?: emptyList()
        return citas.filter { it.fecha in startDate..endDate }
    }


    fun confirmarCita(citaId: String) {
        viewModelScope.launch {
            try {
                val cita = getCitaById(citaId)
                cita.estado = CitaEstado.CONFIRMADA
                updateCita(cita)
            } catch (e: Exception) {
                setError(e.message)
            }
        }
    }

    fun cancelarCita(citaId: String) {
        viewModelScope.launch {
            try {
                val cita = getCitaById(citaId)
                cita.estado = CitaEstado.CANCELADA
                updateCita(cita)
            } catch (e: Exception) {
                setError(e.message)
            }
        }
    }

}

class CitaViewModelFactory(private val citaRepository: CitaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CitaViewModel::class.java)) {
            return CitaViewModel(citaRepository, ClienteRepository(), EmpleadoRepository()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

