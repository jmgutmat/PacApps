package com.jmgtumat.pacapps.clientmod

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jmgtumat.pacapps.navigation.AppScreens
import com.jmgtumat.pacapps.repository.ServicioRepository
import com.jmgtumat.pacapps.ui.theme.Dimens
import com.jmgtumat.pacapps.viewmodels.AppointmentSummaryViewModel
import com.jmgtumat.pacapps.viewmodels.ServicioViewModel
import com.jmgtumat.pacapps.viewmodels.ServicioViewModelFactory

@Composable
fun ServiceSelectionScreen(navController: NavController) {
    val servicioViewModel: ServicioViewModel = viewModel(factory = ServicioViewModelFactory(ServicioRepository()))
    val appointmentSummaryViewModel: AppointmentSummaryViewModel = viewModel()

    val servicios by servicioViewModel.servicios.observeAsState(emptyList())
    var selectedIds by remember { mutableStateOf(setOf<String>()) }

    ClienteDashboard(navController = navController) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Título principal
            Text(
                text = "Selecciona los servicios",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = Dimens.ScreenPadding, vertical = Dimens.CardSpacing)
            )

            // Lista de servicios
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(Dimens.CardSpacing),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = Dimens.ScreenPadding)
            ) {
                items(servicios) { servicio ->
                    val isSelected = selectedIds.contains(servicio.id)
                    val borderColor by animateColorAsState(
                        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                        label = "borderColor"
                    )

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedIds = if (isSelected) {
                                    selectedIds - servicio.id
                                } else {
                                    selectedIds + servicio.id
                                }
                            },
                        shape = RoundedCornerShape(Dimens.CardCornerRadius),
                        elevation = CardDefaults.cardElevation(
                            if (isSelected) Dimens.SelectedCardElevation else Dimens.CardElevation
                        ),
                        border = BorderStroke(1.dp, borderColor),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(Dimens.ScreenPadding)) {
                            Text(
                                servicio.nombre,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                "Duración: ${servicio.duracion} min",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                "Precio: ${"%.2f".format(servicio.precio)} €",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            if (servicio.descripcion.isNotEmpty()) {
                                Text(
                                    servicio.descripcion,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // Zona inferior: resumen + botón
            if (selectedIds.isNotEmpty()) {
                val selectedServices = servicios.filter { selectedIds.contains(it.id) }
                val totalPrecio = selectedServices.sumOf { it.precio }
                val totalTiempo = selectedServices.sumOf { it.duracion }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimens.ScreenPadding)
                ) {
                    Text(
                        text = "${"%.2f".format(totalPrecio)}€ | ${selectedServices.size} servicio(s) - $totalTiempo min",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(bottom = Dimens.CardSpacing)
                    )
                    Button(
                        onClick = {
                            appointmentSummaryViewModel.setSelectedServices(selectedServices) // ✅ Guardamos objetos completos
                            navController.navigate(AppScreens.AppointmentBookingScreen.route)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Dimens.ButtonHeight),
                        shape = MaterialTheme.shapes.large,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            text = "Continuar",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}
