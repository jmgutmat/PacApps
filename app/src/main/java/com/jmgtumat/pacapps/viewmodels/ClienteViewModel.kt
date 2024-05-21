package com.jmgtumat.pacapps.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmgtumat.pacapps.data.Cliente
import com.jmgtumat.pacapps.repository.ClienteRepository
import kotlinx.coroutines.launch

class ClienteViewModel(private val clienteRepository: ClienteRepository) : ViewModel() {

    private val _clientes = MutableLiveData<List<Cliente>>()
    val clientes: LiveData<List<Cliente>> get() = _clientes

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    init {
        fetchClientes()
    }

    private fun fetchClientes() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val fetchedClientes = clienteRepository.getClientes()
                _clientes.value = fetchedClientes
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun insertCliente(cliente: Cliente) {
        viewModelScope.launch {
            try {
                clienteRepository.addCliente(cliente)
                fetchClientes() // Refresh the client list after adding a new client
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun updateCliente(cliente: Cliente) {
        viewModelScope.launch {
            try {
                clienteRepository.updateCliente(cliente)
                fetchClientes() // Refresh the client list after updating a client
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun deleteCliente(clienteId: String) {
        viewModelScope.launch {
            try {
                clienteRepository.deleteCliente(clienteId)
                fetchClientes() // Refresh the client list after deleting a client
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
