package com.jmgtumat.pacapps.employeemod.services

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jmgtumat.pacapps.employeemod.EmpleadoDashboard
import com.jmgtumat.pacapps.viewmodels.AppViewModel

@Composable
fun ManageServicesScreen(
    appViewModel: AppViewModel = viewModel()
) {
    val servicios by appViewModel.servicioViewModel.servicios.observeAsState(emptyList())

    EmpleadoDashboard(appViewModel = appViewModel) { innerPadding -> // Llamamos a EmpleadoDashboard y pasamos un @Composable como parámetro
        Column(
            modifier = Modifier.padding(innerPadding) // Usamos innerPadding proporcionado por EmpleadoDashboard
        ) {
            // Botón para añadir un nuevo servicio
            AddServiceButton(appViewModel.servicioViewModel)

            // Listado de servicios
            ServicesList(servicios, appViewModel.servicioViewModel)
        }
    }
}