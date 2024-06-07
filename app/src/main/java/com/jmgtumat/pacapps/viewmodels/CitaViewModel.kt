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

/**
 * ViewModel para manejar la lógica relacionada con las citas.
 * @param citaRepository Repositorio de citas para interactuar con la capa de datos.
 * @param clienteRepository Repositorio de clientes para interactuar con la capa de datos.
 * @param empleadoRepository Repositorio de empleados para interactuar con la capa de datos.
 */
class CitaViewModel(
    private val citaRepository: CitaRepository,
    private val clienteRepository: ClienteRepository,
    private val empleadoRepository: EmpleadoRepository
) : BaseViewModel() {

    val _citas = MutableLiveData<List<Cita>>()
    val citas: LiveData<List<Cita>> get() = _citas

    init {
        fetchCitas()
    }

    fun fetchCitas() {
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

    /**
     * Método suspendido para obtener citas por ID de empleado y fecha.
     * @param empleadoId ID del empleado.
     * @param dateInMillis Fecha en milisegundos.
     */
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

    /**
     * Método para insertar una nueva cita.
     * @param cita Cita a insertar.
     * @param clienteId ID del cliente asociado a la cita.
     */
    fun insertCita(cita: Cita, clienteId: String) {
        viewModelScope.launch {
            try {
                Log.d("CitaViewModel", "Fecha de la cita: ${cita.fecha}")
                val citaId = citaRepository.addCita(cita)
                cita.id = citaId
                fetchCitas()

                val historialCitas = clienteRepository.getHistorialCitas(clienteId).toMutableList()
                historialCitas.add(cita)

                clienteRepository.updateHistorialCitas(clienteId, historialCitas)

                val empleadoId = cita.empleadoId
                val citasAsignadas = empleadoRepository.getCitasAsignadas(empleadoId).map { it.id }.toMutableList()
                citasAsignadas.add(cita.id)
                empleadoRepository.updateCitasAsignadas(empleadoId, citasAsignadas)
            } catch (e: Exception) {
                setError(e.message)
            }
        }
    }

    /**
     * Método para actualizar una cita existente.
     * @param cita Cita a actualizar.
     */
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

    /**
     * Método suspendido para obtener una cita por su ID.
     * @param citaId ID de la cita.
     * @return La cita correspondiente al ID proporcionado.
     */
    suspend fun getCitaById(citaId: String): Cita {
        return citaRepository.getCitaById(citaId)
    }

    /**
     * Método para obtener citas dentro de un rango de fechas.
     * @param startDate Fecha de inicio del rango en milisegundos.
     * @param endDate Fecha de fin del rango en milisegundos.
     * @return Lista de citas dentro del rango de fechas.
     */
    fun getCitasByDateRange(startDate: Long, endDate: Long): List<Cita> {
        val citas = _citas.value ?: emptyList()
        return citas.filter { it.fecha in startDate..endDate }
    }

    /**
     * Método para confirmar una cita.
     * @param citaId ID de la cita a confirmar.
     */
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

    /**
     * Método para cancelar una cita.
     * @param citaId ID de la cita a cancelar.
     */
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

/**
 * Clase Factory para proporcionar una instancia de [CitaViewModel].
 * @param citaRepository Repositorio de citas para la inyección de dependencias.
 */
class CitaViewModelFactory(private val citaRepository: CitaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CitaViewModel::class.java)) {
            return CitaViewModel(citaRepository, ClienteRepository(), EmpleadoRepository()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
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