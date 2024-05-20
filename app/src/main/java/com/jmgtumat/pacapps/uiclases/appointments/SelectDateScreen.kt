package com.jmgtumat.pacapps.uiclases.appointments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jmgtumat.pacapps.viewmodels.CitaViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SelectDateScreen(viewModel: CitaViewModel = viewModel()) {
    val fechasDisponibles by viewModel.fechasDisponibles.observeAsState(emptyList())
    val loading by viewModel.loading.observeAsState(false)
    val error by viewModel.error.observeAsState(null)
    val selectedDate = viewModel.selectedFecha.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (loading) {
            Text(text = "Cargando fechas disponibles...")
        } else if (error != null) {
            Text(text = "Error: $error")
        } else {
            if (fechasDisponibles.isNotEmpty()) {
                Text(text = "Selecciona una fecha disponible:")
                LazyColumn {
                    items(fechasDisponibles.size) { index ->
                        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        val dateString = dateFormatter.format(Date(fechasDisponibles[index]))
                        Button(onClick = {
                            viewModel.selectFecha(fechasDisponibles[index])
                        }) {
                            Text(text = dateString)
                        }
                    }
                }
            } else {
                Text(text = "No hay fechas disponibles.")
            }
        }
    }
}
