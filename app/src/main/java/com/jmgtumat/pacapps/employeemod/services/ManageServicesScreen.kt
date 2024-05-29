package com.jmgtumat.pacapps.employeemod.services

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jmgtumat.pacapps.employeemod.EmpleadoDashboard
import com.jmgtumat.pacapps.repository.ServicioRepository
import com.jmgtumat.pacapps.viewmodels.ServicioViewModel
import com.jmgtumat.pacapps.viewmodels.ServicioViewModelFactory

@Composable
fun ManageServicesScreen(navController: NavController) {
    val servicioViewModel: ServicioViewModel = viewModel(
        factory = ServicioViewModelFactory(
            ServicioRepository(/* parámetros de configuración si los hay */),
        )
    )
    val servicios by servicioViewModel.servicios.observeAsState(emptyList())

    EmpleadoDashboard(navController = navController) { innerPadding -> // Llamamos a EmpleadoDashboard y pasamos un @Composable como parámetro
        Column(
            modifier = Modifier.padding(innerPadding) // Usamos innerPadding proporcionado por EmpleadoDashboard
        ) {
            // Botón para añadir un nuevo servicio
            AddServiceButton(servicioViewModel)

            // Listado de servicios
            ServicesList(servicios, servicioViewModel)
        }
    }
}