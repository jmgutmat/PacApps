// ManageClientsScreen.kt
package com.jmgtumat.pacapps.uiclases.clients

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jmgtumat.pacapps.data.Cliente
import com.jmgtumat.pacapps.uiclases.employees.EmployeeDashboard
import com.jmgtumat.pacapps.viewmodels.ClienteViewModel

@Composable
fun ManageClientsScreen(
    navController: NavController,
    clienteViewModel: ClienteViewModel = viewModel(),
    onEditClient: (Cliente) -> Unit
) {
    val clientes by clienteViewModel.clientes.observeAsState(emptyList())
    val loading by clienteViewModel.loading.observeAsState(false)
    val error by clienteViewModel.error.observeAsState(null)

    EmployeeDashboard(
        currentSection = "Manage Clients",
        navController = navController
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (loading) {
                Text(text = "Loading clients...")
            } else if (error != null) {
                Text(text = "Error: $error")
            } else {
                if (clientes.isEmpty()) {
                    Text(text = "No clients available.")
                } else {
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(clientes) { cliente ->
                            ClientItem(cliente = cliente, onClick = { onEditClient(cliente) })
                        }
                    }
                }
            }
        }
    }
}
