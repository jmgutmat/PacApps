package com.jmgtumat.pacapps.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.jmgtumat.pacapps.data.Cita
import com.jmgtumat.pacapps.data.CitaEstado
import com.jmgtumat.pacapps.data.Cliente
import com.jmgtumat.pacapps.data.Empleado
import com.jmgtumat.pacapps.data.Servicio
import com.jmgtumat.pacapps.repository.CitaRepository
import com.jmgtumat.pacapps.repository.ClienteRepository
import com.jmgtumat.pacapps.repository.EmpleadoRepository
import com.jmgtumat.pacapps.repository.ServicioRepository
import kotlinx.coroutines.launch

/**
 * ViewModel para manejar la lógica relacionada con los clientes.
 * @param clienteRepository Repositorio de clientes para interactuar con la capa de datos.
 * @param servicioRepository Repositorio de servicios para interactuar con la capa de datos.
 * @param empleadoRepository Repositorio de empleados para interactuar con la capa de datos.
 * @param citaRepository Repositorio de citas para interactuar con la capa de datos.
 */
class ClienteViewModel(
    private val clienteRepository: ClienteRepository,
    private val servicioRepository: ServicioRepository,
    private val empleadoRepository: EmpleadoRepository,
    private val citaRepository: CitaRepository
) : BaseViewModel() {

    private val _clientes = MutableLiveData<List<Cliente>>()
    val clientes: LiveData<List<Cliente>> get() = _clientes

    private val _servicios = MutableLiveData<List<Servicio>>()
    val servicios: LiveData<List<Servicio>> get() = _servicios

    private val _empleados = MutableLiveData<List<Empleado>>()
    val empleados: LiveData<List<Empleado>> get() = _empleados

    private val _historialCitas = MutableLiveData<List<Cita>>()
    val historialCitas: LiveData<List<Cita>> get() = _historialCitas

    private val _clienteId = MutableLiveData<String>()
    val clienteId: LiveData<String> get() = _clienteId

    private val _citaPendiente = MutableLiveData<Cita?>()
    val citaPendiente: LiveData<Cita?> get() = _citaPendiente

    init {
        fetchClientes()
        fetchServicios()
        fetchEmpleados()
        _clienteId.value = getAuthenticatedClienteId()
        fetchHistorialCitasClienteAutenticado()
        fetchCitaPendienteClienteAutenticado()
    }

    /**
     * Método para obtener el ID del cliente autenticado.
     * @return El ID del cliente autenticado.
     * @throws IllegalStateException Si el usuario no está autenticado.
     */
    private fun getAuthenticatedClienteId(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser?.uid ?: throw IllegalStateException("Usuario no autenticado")
    }

    private fun fetchClientes() {
        viewModelScope.launch {
            setLoading()
            try {
                val fetchedClientes = clienteRepository.getClientes()
                _clientes.value = fetchedClientes
                setSuccess()
            } catch (e: Exception) {
                setError(e.message)
                Log.e("ClienteViewModel", "Error al obtener clientes: ${e.message}")
            }
        }
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

    private fun fetchEmpleados() {
        viewModelScope.launch {
            setLoading()
            try {
                val fetchedEmpleados = empleadoRepository.getEmpleados()
                _empleados.value = fetchedEmpleados
                setSuccess()
            } catch (e: Exception) {
                setError(e.message)
            }
        }
    }

    /**
     * Método para insertar un nuevo cliente.
     * @param cliente Cliente a insertar.
     */
    fun insertCliente(cliente: Cliente) {
        viewModelScope.launch {
            try {
                clienteRepository.addCliente(cliente)
                fetchClientes()
            } catch (e: Exception) {
                setError(e.message)
            }
        }
    }

    /**
     * Método para actualizar un cliente existente.
     * @param cliente Cliente a actualizar.
     */
    fun updateCliente(cliente: Cliente) {
        viewModelScope.launch {
            try {
                clienteRepository.updateCliente(cliente)
                fetchClientes()
            } catch (e: Exception) {
                setError(e.message)
            }
        }
    }

    /**
     * Método para eliminar un cliente.
     * @param clienteId ID del cliente a eliminar.
     */
    fun deleteCliente(clienteId: String) {
        viewModelScope.launch {
            try {
                clienteRepository.deleteCliente(clienteId)
                fetchClientes()
            } catch (e: Exception) {
                setError(e.message)
            }
        }
    }

    fun fetchCitaPendienteClienteAutenticado() {
        viewModelScope.launch {
            setLoading()
            try {
                val clienteId = getAuthenticatedClienteId()
                val historialCitas = getCitasFromHistorial(clienteId)
                val citaPendiente = historialCitas.find { it.estado == CitaEstado.PENDIENTE }
                _citaPendiente.value = citaPendiente
                setSuccess()
            } catch (e: Exception) {
                setError(e.message)
            }
        }
    }

    private fun fetchHistorialCitasClienteAutenticado() {
        viewModelScope.launch {
            setLoading()
            try {
                val clienteId = getAuthenticatedClienteId()
                val fetchedHistorialCitas = getCitasFromHistorial(clienteId)
                _historialCitas.value = fetchedHistorialCitas.sortedByDescending { it.fecha }
                setSuccess()
            } catch (e: Exception) {
                setError(e.message)
            }
        }
    }

    /**
     * Método suspendido para obtener las citas del historial de un cliente.
     * @param clienteId ID del cliente.
     * @return Lista de citas del historial del cliente.
     */
    private suspend fun getCitasFromHistorial(clienteId: String): List<Cita> {
        val citaIds = clienteRepository.getHistorialCitas(clienteId)
        val citas = mutableListOf<Cita>()
        for (citaId in citaIds) {
            val cita = citaRepository.getCitaById(citaId.id)
            citas.add(cita)
        }
        return citas
    }

    /**
     * Método para obtener el historial de citas de un cliente.
     * @param clienteId ID del cliente.
     */
    fun fetchHistorialCitas(clienteId: String) {
        viewModelScope.launch {
            setLoading()
            try {
                val fetchedHistorialCitas = getCitasFromHistorial(clienteId)
                _historialCitas.value = fetchedHistorialCitas
                setSuccess()
            } catch (e: Exception) {
                setError(e.message)
            }
        }
    }
}

/**
 * Clase Factory para proporcionar una instancia de [ClienteViewModel].
 * @param clienteRepository Repositorio de clientes para la inyección de dependencias.
 */
class ClienteViewModelFactory(private val clienteRepository: ClienteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClienteViewModel::class.java)) {
            return ClienteViewModel(clienteRepository, ServicioRepository(), EmpleadoRepository(), CitaRepository()) as T
        }
        throw IllegalArgumentException("Clase ViewModel desconocida")
    }
}


//    fun fetchServicioNombre(servicioId: String) {
//        viewModelScope.launch {
//            setLoading()
//            try {
//                val servicio = servicioRepository.getServicioById(servicioId)
//                _servicioNombre.value = servicio.nombre
//                setSuccess()
//            } catch (e: Exception) {
//                setError(e.message)
//            }
//        }
//    }

//    fun updateHistorialCitas(clienteId: String, citas: List<Cita>) {
//        viewModelScope.launch {
//            try {
//                clienteRepository.updateHistorialCitas(clienteId, citas)
//            } catch (e: Exception) {
//                setError(e.message)
//            }
//        }
//    }