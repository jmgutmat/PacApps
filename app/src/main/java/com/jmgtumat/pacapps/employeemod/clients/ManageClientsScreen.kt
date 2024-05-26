package com.jmgtumat.pacapps.employeemod.clients

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.jmgtumat.pacapps.employeemod.EmpleadoDashboard
import com.jmgtumat.pacapps.util.SearchBar
import com.jmgtumat.pacapps.viewmodels.AppViewModel

@Composable
fun ManageClientsScreen(
    navController: NavHostController,
    appViewModel: AppViewModel = viewModel()
) {
    val clientes by appViewModel.clienteViewModel.clientes.observeAsState(emptyList())
    var searchQuery by remember { mutableStateOf("") }

    EmpleadoDashboard(
        navController = navController,
        appViewModel = appViewModel
    ) {
        Column {
            SearchBar(
                searchQuery = searchQuery,
                onSearchQueryChange = { newQuery ->
                    searchQuery = newQuery
                }
            )

            val filteredClientes = filterClientes(clientes, searchQuery)

            AddClientButton(navController, appViewModel.clienteViewModel)

            filteredClientes.forEach { cliente ->
                ClientItem(cliente, appViewModel.clienteViewModel, navController)
            }
        }
    }
}
