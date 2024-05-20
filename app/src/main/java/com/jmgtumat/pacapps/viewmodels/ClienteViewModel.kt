package com.jmgtumat.pacapps.viewmodels

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.activity.result.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.jmgtumat.pacapps.data.Cliente
import com.jmgtumat.pacapps.repository.ClienteRepository
import com.jmgutmat.pacapps.repository.ClienteRepository
import kotlinx.coroutines.launch

class ClienteViewModel(private val clienteRepository: ClienteRepository) :
    ViewModel() {
    val clientes: LiveData<List<Cliente>> = clienteRepository.getAllClientes().asLiveData()

    fun insertCliente(cliente: Cliente) {
        viewModelScope.launch {
            clienteRepository.insertCliente(cliente)
        }
    }

    // Other methods for updating, deleting, and getting clientes
}