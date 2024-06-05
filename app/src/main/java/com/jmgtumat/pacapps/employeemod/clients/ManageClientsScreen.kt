package com.jmgtumat.pacapps.employeemod.clients

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jmgtumat.pacapps.employeemod.EmpleadoDashboard
import com.jmgtumat.pacapps.repository.ClienteRepository
import com.jmgtumat.pacapps.util.SearchBar
import com.jmgtumat.pacapps.viewmodels.ClienteViewModel
import com.jmgtumat.pacapps.viewmodels.ClienteViewModelFactory

/**
 * Pantalla para administrar clientes.
 *
 * @param navController El controlador de navegación.
 */
@Composable
fun ManageClientsScreen(navController: NavController) {
    val clienteViewModel: ClienteViewModel = viewModel(
        factory = ClienteViewModelFactory(
            ClienteRepository(),
        )
    )
    val clientes by clienteViewModel.clientes.observeAsState(emptyList())
    var searchQuery by remember { mutableStateOf("") }

    /**
     * Diseño de la pantalla de administración de clientes.
     */
    EmpleadoDashboard(navController = navController) {
        Column {
            // Barra de búsqueda para filtrar clientes
            SearchBar(
                searchQuery = searchQuery,
                onSearchQueryChange = { newQuery ->
                    searchQuery = newQuery
                }
            )

            // Filtrar clientes según la consulta de búsqueda
            val filteredClientes = filterClientes(clientes, searchQuery)

            // Botón para añadir un nuevo cliente
            AddClientButton(navController, clienteViewModel)

            // Mostrar la lista de clientes filtrada
            filteredClientes.forEach { cliente ->
                ClientItem(cliente, clienteViewModel, navController)
            }
        }
    }
}
