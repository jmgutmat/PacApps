package com.jmgtumat.pacapps.uiclases.employees

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
import com.jmgtumat.pacapps.data.Empleado
import com.jmgtumat.pacapps.viewmodels.EmpleadoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEmployeeScreen(
    viewModel: EmpleadoViewModel,
    onNavigateBack: () -> Unit
) {
    var nombre by rememberSaveable {
        mutableStateOf(
            ""
        )
    }
    var puesto by rememberSaveable {
        mutableStateOf(
            ""
        )
    }
    var telefono by rememberSaveable {
        mutableStateOf(
            ""
        )
    }
    var email by rememberSaveable {
        mutableStateOf(
            ""
        )
    }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text("Add Employee") },
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
                value = puesto,
                onValueChange = { puesto = it },
                label = { Text("Position") }
            )
            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Phone") }
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") }
            )
            Button(
                onClick = {
                    viewModel.insertEmpleado(Empleado(0, nombre, puesto, telefono, email))
                    onNavigateBack()
                }
            ) {
                Text("Save")
            }
        }
    }
}