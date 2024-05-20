package com.jmgtumat.pacapps.uiclases.clients

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jmgtumat.pacapps.data.Cliente
import com.jmgtumat.pacapps.viewmodels.ClienteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewClientsScreen(
    viewModel: ClienteViewModel,
    onNavigateToAddClient: () -> Unit,
    onNavigateToEditClient: (Cliente) -> Unit
) {
    val clientes by viewModel.clientes.observeAsState(initial = emptyList())
    val loading by viewModel.loading.observeAsState(initial = false)
    val error by viewModel.error.observeAsState()

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text("View Clients") },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAddClient) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Client"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { padding ->
        when {
            loading -> {
                // Show a loading indicator
                Text("Loading...")
            }
            error != null -> {
                // Show error message
                Text("Error: $error")
            }
            else -> {
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    clientes.forEach { cliente ->
                        ClientItem(
                            cliente = cliente,
                            onClick = { onNavigateToEditClient(cliente) }
                        )
                    }
                }
            }
        }
    }
}
