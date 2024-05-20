package com.jmgtumat.pacapps.uiclases.services

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jmgtumat.pacapps.data.Servicio
import com.jmgtumat.pacapps.viewmodels.ServicioViewModel
import kotlin.text.toDouble

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddServiceScreen(
    viewModel: ServicioViewModel,
    onNavigateBack: () -> Unit
) {
    var nombre by rememberSaveable {
        mutableStateOf(
            ""
        )
    }
    var descripcion by rememberSaveable {
        mutableStateOf(
            ""
        )
    }
    var precio by rememberSaveable {
        mutableStateOf(
            ""
        )
    }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text("Add Service") },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Name") }
            )
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Description") }
            )
            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Price") }
            )
            Button(
                onClick = {
                    viewModel.insertServicio(Servicio(0, nombre, descripcion, precio.toDouble()))
                    onNavigateBack()
                }
            ) {
                Text("Save")
            }
        }
    }
}