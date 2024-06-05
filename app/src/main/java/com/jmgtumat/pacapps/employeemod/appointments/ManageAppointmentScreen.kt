//package com.jmgtumat.pacapps.employeemod.appointments
//
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.livedata.observeAsState
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//import com.jmgtumat.pacapps.employeemod.EmpleadoDashboard
//import com.jmgtumat.pacapps.repository.CitaRepository
//import com.jmgtumat.pacapps.repository.EmpleadoRepository
//import com.jmgtumat.pacapps.util.getCitasEnHorarioTrabajo
//import com.jmgtumat.pacapps.util.getDayOfWeekString
//import com.jmgtumat.pacapps.viewmodels.CitaViewModel
//import com.jmgtumat.pacapps.viewmodels.CitaViewModelFactory
//import com.jmgtumat.pacapps.viewmodels.EmpleadoViewModel
//import com.jmgtumat.pacapps.viewmodels.EmpleadoViewModelFactory
//import kotlinx.coroutines.launch
//import java.util.Calendar
//
//@Composable
//fun ManageAppointmentsScreen(navController: NavController) {
//    val citaViewModel: CitaViewModel = viewModel(
//        factory = CitaViewModelFactory(
//            CitaRepository(/* parámetros de configuración si los hay */),
//        )
//    )
//    val empleadoViewModel: EmpleadoViewModel = viewModel(
//        factory = EmpleadoViewModelFactory(
//            EmpleadoRepository(/* parámetros de configuración si los hay */),
//        )
//    )
//    val citas by citaViewModel.citas.observeAsState(emptyList())
//    val empleados by empleadoViewModel.empleados.observeAsState(emptyList())
//    val empleado = empleados.firstOrNull() ?: return // Obtener el primer empleado
//
//    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }
//    var horariosPorDia by remember {
//        mutableStateOf(
//            empleado.horariosTrabajo[selectedDate.getDayOfWeekString()] ?: defaultHorariosPorDia()
//        )
//    }
//    val coroutineScope = rememberCoroutineScope()
//
//    LaunchedEffect(Unit) {
//        coroutineScope.launch {
//            citaViewModel.fetchCitasByDate(selectedDate.timeInMillis)
//        }
//    }
//
//    EmpleadoDashboard(navController = navController) { innerPadding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//                .padding(16.dp),
//            verticalArrangement = Arrangement.Center
//        )  {
//            DatePicker(selectedDate) { date ->
//                selectedDate = date
//                horariosPorDia = empleado.horariosTrabajo[date.getDayOfWeekString()] ?: defaultHorariosPorDia()
//                coroutineScope.launch {
//                    citaViewModel.fetchCitasByDate(date.timeInMillis)
//                }
//            }
//
//            HorariosTrabajo(horariosPorDia) { updatedHorarios ->
//                horariosPorDia = updatedHorarios
//                val updatedHorariosTrabajo = empleado.horariosTrabajo.toMutableMap().apply {
//                    put(selectedDate.getDayOfWeekString(), updatedHorarios)
//                }
//                empleadoViewModel.updateEmpleado(empleado.copy(horariosTrabajo = updatedHorariosTrabajo))
//            }
//
//            AddAppointmentButton(navController)
//
//            val citasEnHorarioTrabajo = getCitasEnHorarioTrabajo(citas, empleado, selectedDate)
//            HorarioCompletoConCitas(
//                citas = citasEnHorarioTrabajo,
//                onConfirmCita = { citaId ->
//                    citaViewModel.confirmarCita(citaId)
//                },
//                onCancelCita = { citaId ->
//                    citaViewModel.cancelarCita(citaId)
//                }
//            )
//        }
//    }
//}

package com.jmgtumat.pacapps.employeemod.appointments

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jmgtumat.pacapps.data.Cita
import com.jmgtumat.pacapps.data.CitaEstado
import com.jmgtumat.pacapps.employeemod.EmpleadoDashboard
import com.jmgtumat.pacapps.repository.CitaRepository
import com.jmgtumat.pacapps.repository.ClienteRepository
import com.jmgtumat.pacapps.repository.EmpleadoRepository
import com.jmgtumat.pacapps.util.formatTimeNew
import com.jmgtumat.pacapps.viewmodels.CitaViewModel
import com.jmgtumat.pacapps.viewmodels.CitaViewModelFactory
import com.jmgtumat.pacapps.viewmodels.ClienteViewModel
import com.jmgtumat.pacapps.viewmodels.ClienteViewModelFactory
import com.jmgtumat.pacapps.viewmodels.EmpleadoViewModel
import com.jmgtumat.pacapps.viewmodels.EmpleadoViewModelFactory
import kotlinx.coroutines.launch
import java.util.Calendar

@Composable
fun ManageAppointmentsScreen(navController: NavController) {
    val citaViewModel: CitaViewModel = viewModel(
        factory = CitaViewModelFactory(CitaRepository())
    )
    val empleadoViewModel: EmpleadoViewModel = viewModel(
        factory = EmpleadoViewModelFactory(EmpleadoRepository())
    )
    val clienteViewModel: ClienteViewModel = viewModel(
        factory = ClienteViewModelFactory(ClienteRepository())
    )

    val citas by citaViewModel.citas.observeAsState(emptyList())
    val loading by empleadoViewModel.loading.observeAsState(false)
    val error by empleadoViewModel.error.observeAsState(null)
    val serviciosList by clienteViewModel.servicios.observeAsState(emptyList())
    val empleadosList by clienteViewModel.empleados.observeAsState(emptyList())
    val citasList by clienteViewModel.clientes.observeAsState(emptyList())

    val coroutineScope = rememberCoroutineScope()

    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }

    // Observe the employee ID retrieved by EmpleadoViewModel
    val empleadoId by empleadoViewModel.empleadoId.observeAsState()

    if (loading) {
        CircularProgressIndicator()
        return
    }

    if (error != null) {
        Text("Error: $error")
        return
    }

    LaunchedEffect(empleadoId, selectedDate) {
        if (empleadoId != null) { // Only fetch if we have a valid employee ID
            empleadoId?.let { id ->
                coroutineScope.launch {
                    citaViewModel.fetchCitasByEmpleadoIdAndDate(id, selectedDate.timeInMillis)
                }
            }
        }
    }

    EmpleadoDashboard(navController = navController) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            DatePicker(selectedDate) { date ->
                selectedDate = date
                // Re-fetch citas when the selected date changes
                empleadoId?.let { id ->
                    coroutineScope.launch {
                        citaViewModel.fetchCitasByEmpleadoIdAndDate(id, selectedDate.timeInMillis)
                    }
                }
            }

//            AddAppointmentButton(navController)

            if (citas.isEmpty()) {
                Text("No appointments found for the selected date.")
            } else {
                // Filter citas by employee ID and pending/confirmed state
                val filteredCitas = citas.filter { it.empleadoId == empleadoId && (it.estado == CitaEstado.PENDIENTE || it.estado == CitaEstado.CONFIRMADA) }

                // Enable vertical scrolling for long appointment lists
                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(filteredCitas) { cita ->
                            AppointmentCard(cita, citaViewModel, clienteViewModel)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun CitasList(citas: List<Cita>, citaViewModel: CitaViewModel, clienteViewModel: ClienteViewModel) {
    Column {
        for (cita in citas) {
            AppointmentCard(cita, citaViewModel, clienteViewModel)
        }
    }
}

@Composable
fun AppointmentCard(cita: Cita, citaViewModel: CitaViewModel, clienteViewModel: ClienteViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val backgroundColor = if (cita.estado == CitaEstado.CONFIRMADA) Color(0xFFCCE5FF) else Color(0xFFE0F2F1)
    val serviciosList by clienteViewModel.servicios.observeAsState(emptyList())
    val empleadosList by clienteViewModel.empleados.observeAsState(emptyList())
    val citasList by clienteViewModel.clientes.observeAsState(emptyList())
    val servicio = serviciosList.find { it.id == cita.servicioId }
    val empleado = empleadosList.find { it.id == cita.empleadoId }
    val cliente = citasList.find { it.id == cita.clienteId }

    Box(
        modifier = Modifier
            .padding(8.dp)
            .clickable { expanded = !expanded }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundColor)
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = formatTimeNew(cita.horaInicio), style = MaterialTheme.typography.titleLarge)
                        if (cliente != null) {
                            Text(text = cliente.nombre, style = MaterialTheme.typography.titleLarge)
                        }
                    }

                    if (expanded) {
                        if (servicio != null) {
                            Text(text = "Servicio: ${servicio.nombre}", style = MaterialTheme.typography.titleLarge)
                        }

                        // Hide buttons for confirmed appointments
                        if (cita.estado != CitaEstado.CONFIRMADA) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(onClick = { citaViewModel.confirmarCita(cita.id) }) {
                                    Text("Confirmar")
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(onClick = { citaViewModel.cancelarCita(cita.id) }) {
                                    Text("Cancelar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
