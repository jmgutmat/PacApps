package com.jmgtumat.pacapps.clientmod

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.jmgtumat.pacapps.data.Cita
import com.jmgtumat.pacapps.data.Servicio
import com.jmgtumat.pacapps.navigation.AppScreens
import com.jmgtumat.pacapps.repository.CitaRepository
import com.jmgtumat.pacapps.repository.ClienteRepository
import com.jmgtumat.pacapps.repository.EmpleadoRepository
import com.jmgtumat.pacapps.ui.theme.Dimens
import com.jmgtumat.pacapps.viewmodels.AppointmentSummaryViewModel
import com.jmgtumat.pacapps.viewmodels.CitaViewModel
import com.jmgtumat.pacapps.viewmodels.CitaViewModelFactory
import com.jmgtumat.pacapps.viewmodels.ClienteViewModel
import com.jmgtumat.pacapps.viewmodels.ClienteViewModelFactory
import com.jmgtumat.pacapps.viewmodels.EmpleadoViewModel
import com.jmgtumat.pacapps.viewmodels.EmpleadoViewModelFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun AppointmentBookingScreen(navController: NavController) {
    // 🆕 Obtenemos el back stack entry de la ruta actual
    val currentBackStackEntry = remember(navController.currentBackStackEntry) {
        navController.currentBackStackEntry
    }

    // 🆕 Creamos una instancia de CitaViewModel asociada al back stack entry actual
    val citaViewModel: CitaViewModel = viewModel(
        viewModelStoreOwner = currentBackStackEntry!!, // Aseguramos que el ViewModel esté atado al ciclo de vida de esta ruta
        factory = CitaViewModelFactory(CitaRepository())
    )

    val parentEntryState = remember { mutableStateOf<NavBackStackEntry?>(null) }

    LaunchedEffect(Unit) {
        parentEntryState.value = navController.getBackStackEntry(AppScreens.ServiceSelectionScreen.route)
    }

    val appointmentSummaryViewModel: AppointmentSummaryViewModel? = parentEntryState.value?.let {
        viewModel(it)
    }

    if (appointmentSummaryViewModel == null) {
        Text("Cargando...")
        return
    }

    val empleadoViewModel: EmpleadoViewModel = viewModel(factory = EmpleadoViewModelFactory(EmpleadoRepository(),
        ClienteRepository()))
    val empleados by empleadoViewModel.empleados.observeAsState(emptyList())
    val clienteViewModel: ClienteViewModel = viewModel(factory = ClienteViewModelFactory(ClienteRepository()))

    val horasDisponibles by appointmentSummaryViewModel.horasDisponibles.collectAsState()

    val hoy = LocalDate.now()
    val diasDisponibles = (0..60).map { hoy.plusDays(it.toLong()) }

    val selectedServicesState = appointmentSummaryViewModel.selectedServices.collectAsState()
    val selectedServices = selectedServicesState.value

    var diaSeleccionado by remember { mutableStateOf(diasDisponibles.first()) }
    var horaSeleccionada by remember { mutableStateOf("") }
    var especialistaSeleccionado by remember { mutableStateOf("") }

    // Inicializamos el especialista seleccionado por defecto con el primero si está vacío
    LaunchedEffect(empleados) {
        if (empleados.isNotEmpty() && especialistaSeleccionado.isEmpty()) {
            especialistaSeleccionado = empleados.first().nombre
        }
    }

    LaunchedEffect(diaSeleccionado) {
        appointmentSummaryViewModel.selectedDate = diaSeleccionado
        appointmentSummaryViewModel.calcularHorasDisponibles()
    }

    ClienteDashboard(navController = navController) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimens.CardSpacing),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(Dimens.ScreenPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Botón "Atrás" como icono
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver atrás",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            // Resumen de servicios seleccionados
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(Dimens.CardCornerRadius),
                elevation = CardDefaults.cardElevation(Dimens.CardElevation),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(Dimens.ScreenPadding)) {
                    Text(
                        text = "Servicios seleccionados",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if (selectedServices.isEmpty()) {
                        Text(
                            text = "No se han seleccionado servicios.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        selectedServices.forEach { servicio: Servicio ->
                            Text(
                                "- ${servicio.nombre} (${servicio.duracion} min, ${"%.2f".format(servicio.precio)}€)",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Selección de día
            Text(
                text = "Selecciona el día",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(diasDisponibles) { dia ->
                    val formato = dia.format(DateTimeFormatter.ofPattern("dd MMM"))
                    val isSelected = dia == diaSeleccionado
                    val backgroundColor by animateColorAsState(
                        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                    )
                    val textColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(Dimens.CardCornerRadius))
                            .background(backgroundColor)
                            .clickable { diaSeleccionado = dia }
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = formato,
                            style = MaterialTheme.typography.bodyMedium,
                            color = textColor
                        )
                    }
                }
            }

            // Selección de especialista
            if (empleados.isNotEmpty()) {
                Text(
                    text = "Selecciona especialista",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(empleados) { empleado ->
                        val isSelected = empleado.nombre == especialistaSeleccionado
                        val backgroundColor by animateColorAsState(
                            if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                        )
                        val textColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(Dimens.CardCornerRadius))
                                .background(backgroundColor)
                                .clickable { especialistaSeleccionado = empleado.nombre }
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            Text(
                                text = empleado.nombre,
                                style = MaterialTheme.typography.bodyMedium,
                                color = textColor
                            )
                        }
                    }
                }
            }

            // Selección de hora
            Text(
                text = "Selecciona la hora",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(horasDisponibles) { hora ->
                    val isSelected = hora == horaSeleccionada
                    val backgroundColor by animateColorAsState(
                        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                    )
                    val textColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(Dimens.CardCornerRadius))
                            .background(backgroundColor)
                            .clickable { horaSeleccionada = hora }
                            .padding(horizontal = 20.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = hora,
                            style = MaterialTheme.typography.bodyMedium,
                            color = textColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Botón para continuar
            Button(
                onClick = {
                    appointmentSummaryViewModel.selectedDate = diaSeleccionado
                    appointmentSummaryViewModel.selectedTime = horaSeleccionada
                    // Podrías también guardar el especialista si lo necesitas
                    // ✅ Obtenemos el clienteId del cliente autenticado
                    val clienteId = clienteViewModel.getAuthenticatedClienteIdOrNull()
                    if (!clienteId.isNullOrEmpty()) {
                        // 🆕 Creamos la cita temporal y la guardamos en el ViewModel
                        val citaTemporal = Cita(
                            clienteId = clienteId,
                            empleadoId = "tfD2pxzZpddHklnx7d1Dbq7xYlp1", // Asigna aquí el empleadoId real
                            servicioId = selectedServices.joinToString(",") { it.id },
                            fecha = diaSeleccionado.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli(),
                            horaInicio = horaSeleccionada.let { timeStr ->
                                val time = java.time.LocalTime.parse(timeStr)
                                val dateTime = diaSeleccionado.atTime(time)
                                dateTime.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
                            },
                            duracion = selectedServices.sumOf { it.duracion }
                        )
                        // 🟩 LOG para ver qué estamos guardando
                        Log.d("BookingScreen", "Cita temporal creada: $citaTemporal")

                        // Guarda la cita temporal en el ViewModel que está atado a la ruta actual
                        citaViewModel.citaActual.value = citaTemporal

                        Log.d("BookingScreen", "Cita guardada en ViewModel: ${citaViewModel.citaActual.value}")


                        // Navega
                        navController.navigate(AppScreens.AppointmentSummaryScreen.route + "/$clienteId")
                    }
                },
                enabled = horaSeleccionada.isNotEmpty(),
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