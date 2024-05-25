package com.jmgtumat.pacapps.employeemod

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.jmgtumat.pacapps.uiclases.clients.ClientItem
import com.jmgtumat.pacapps.util.SearchBar
import com.jmgtumat.pacapps.viewmodels.ClienteViewModel

@Composable
fun ManageClientsScreen(
    navController: NavController,
    clienteViewModel: ClienteViewModel
) {
    val clientes by clienteViewModel.clientes.observeAsState(emptyList())
    var searchQuery by remember { mutableStateOf("") }

    EmpleadoDashboard(
        clienteViewModel = clienteViewModel
    ) {
        Column {
            SearchBar(
                searchQuery = searchQuery,
                onSearchQueryChange = { newQuery ->
                    searchQuery = newQuery
                }
            )

            val filteredClientes = filterClientes(clientes, searchQuery)

            AddClientButton(navController, clienteViewModel)

            filteredClientes.forEach { cliente ->
                ClientItem(cliente = cliente) {
                    // Handle click on client item, if needed
                }
            }
        }
    }
}

