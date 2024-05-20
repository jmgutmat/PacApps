package com.jmgtumat.pacapps.uiclases.services

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
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
import com.jmgtumat.pacapps.data.Servicio
import com.jmgtumat.pacapps.viewmodels.ServicioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceSelectionScreen(
    viewModel: ServicioViewModel,
    onServiceSelected: (Servicio) -> Unit,
    onNavigateToAddService: () -> Unit
) {
    val servicios by viewModel.servicios.observeAsState(initial = emptyList())

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text("Select Service") },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAddService) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Service"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            servicios.forEach { servicio ->
                Button(
                    onClick = { onServiceSelected(servicio) },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = servicio.nombre)
                }
            }
        }
    }
}
