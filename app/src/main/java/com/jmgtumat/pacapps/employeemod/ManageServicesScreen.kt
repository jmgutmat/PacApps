package com.jmgtumat.pacapps.employeemod

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.jmgtumat.pacapps.viewmodels.ServicioViewModel

@Composable
fun ManageServicesScreen(
    servicioViewModel: ServicioViewModel
) {
    val servicios by servicioViewModel.servicios.observeAsState(emptyList())

    EmpleadoDashboard { innerPadding -> // Llamamos a EmpleadoDashboard y pasamos un @Composable como parámetro
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
