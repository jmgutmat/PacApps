// ManageServicesScreen.kt
package com.jmgtumat.pacapps.uiclases.services

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
import com.jmgtumat.pacapps.data.Servicio
import com.jmgtumat.pacapps.uiclases.employees.EmployeeDashboard
import com.jmgtumat.pacapps.viewmodels.ServicioViewModel

@Composable
fun ManageServicesScreen(
    navController: NavController,
    servicioViewModel: ServicioViewModel = viewModel(),
    onEditService: (Servicio) -> Unit
) {
    val servicios by servicioViewModel.servicios.observeAsState(emptyList())
    val loading by servicioViewModel.loading.observeAsState(false)
    val error by servicioViewModel.error.observeAsState(null)

    EmployeeDashboard(
        currentSection = "Manage Services",
        navController = navController
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (loading) {
                Text(text = "Loading services...")
            } else if (error != null) {
                Text(text = "Error: $error")
            } else {
                if (servicios.isEmpty()) {
                    Text(text = "No services available.")
                } else {
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(servicios) { servicio ->
                            ServiceItem(servicio = servicio, onClick = { onEditService(servicio) })
                        }
                    }
                }
            }
        }
    }
}
