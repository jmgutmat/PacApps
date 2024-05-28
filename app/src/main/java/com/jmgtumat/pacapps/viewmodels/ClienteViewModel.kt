package com.jmgtumat.pacapps.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jmgtumat.pacapps.data.Cita
import com.jmgtumat.pacapps.data.Cliente
import com.jmgtumat.pacapps.data.Empleado
import com.jmgtumat.pacapps.data.Servicio
import com.jmgtumat.pacapps.repository.ClienteRepository
import com.jmgtumat.pacapps.repository.EmpleadoRepository
import com.jmgtumat.pacapps.repository.ServicioRepository
import kotlinx.coroutines.launch

class ClienteViewModel(
    private val clienteRepository: ClienteRepository,
    private val servicioRepository: ServicioRepository,
    private val empleadoRepository: EmpleadoRepository
) : BaseViewModel() {

    private val _clientes = MutableLiveData<List<Cliente>>()
    val clientes: LiveData<List<Cliente>> get() = _clientes

    private val _servicios = MutableLiveData<List<Servicio>>()
    val servicios: LiveData<List<Servicio>> get() = _servicios

    private val _empleados = MutableLiveData<List<Empleado>>()
    val empleados: LiveData<List<Empleado>> get() = _empleados

    private val _historialCitas = MutableLiveData<List<Cita>>()
    val historialCitas: LiveData<List<Cita>> get() = _historialCitas


    init {
        fetchClientes()
        fetchServicios()
        fetchEmpleados()
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

    fun fetchHistorialCitas(clienteId: String) {
        viewModelScope.launch {
            setLoading()
            try {
                // Aquí recuperamos el historial de citas del cliente
                val fetchedHistorialCitas = clienteRepository.getHistorialCitas(clienteId)
                _historialCitas.value = fetchedHistorialCitas
                setSuccess()
            } catch (e: Exception) {
                setError(e.message)
            }
        }
    }
}

class ClienteViewModelFactory(private val clienteRepository: ClienteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClienteViewModel::class.java)) {
            return ClienteViewModel(clienteRepository, ServicioRepository(), EmpleadoRepository()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
