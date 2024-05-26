package com.jmgtumat.pacapps.clientmod

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.jmgtumat.pacapps.viewmodels.AppViewModel

@Composable
fun ClientmodHistoryScreen(navController: NavHostController, appViewModel: AppViewModel = viewModel()) {
    val citasList by appViewModel.clienteViewModel.historialCitas.observeAsState(emptyList())

    ClienteDashboard(navController = navController, viewModel = appViewModel) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(citasList) { cita ->
                ClientAppointmentItem(cita)
            }
        }
    }
}
