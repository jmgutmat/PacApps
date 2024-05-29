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

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jmgtumat.pacapps.employeemod.EmpleadoDashboard
import com.jmgtumat.pacapps.repository.CitaRepository
import com.jmgtumat.pacapps.repository.EmpleadoRepository
import com.jmgtumat.pacapps.util.getCitasEnHorarioTrabajo
import com.jmgtumat.pacapps.util.getDayOfWeekString
import com.jmgtumat.pacapps.viewmodels.CitaViewModel
import com.jmgtumat.pacapps.viewmodels.CitaViewModelFactory
import com.jmgtumat.pacapps.viewmodels.EmpleadoViewModel
import com.jmgtumat.pacapps.viewmodels.EmpleadoViewModelFactory
import kotlinx.coroutines.launch
import java.util.Calendar

@Composable
fun ManageAppointmentsScreen(navController: NavController) {
    Log.d("ManageAppointmentsScreen", "Loading ManageAppointmentsScreen")

    val citaViewModel: CitaViewModel = viewModel(
        factory = CitaViewModelFactory(
            CitaRepository()
        )
    )
    val empleadoViewModel: EmpleadoViewModel = viewModel(
        factory = EmpleadoViewModelFactory(
            EmpleadoRepository()
        )
    )

    val citas by citaViewModel.citas.observeAsState(emptyList())
    val empleados by empleadoViewModel.empleados.observeAsState(emptyList())
    val loading by empleadoViewModel.loading.observeAsState(false)
    val error by empleadoViewModel.error.observeAsState(null)

    if (loading) {
        CircularProgressIndicator()
        return
    }

    if (error != null) {
        Text("Error: $error")
        return
    }

    if (empleados.isEmpty()) {
        Log.d("ManageAppointmentsScreen", "No employees found")
        Text("No employees found. Please add an employee to manage appointments.")
        return
    }

    val empleado = empleados.firstOrNull() ?: return

    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }
    var horariosPorDia by remember {
        mutableStateOf(
            empleado.horariosTrabajo[selectedDate.getDayOfWeekString()] ?: defaultHorariosPorDia()
        )
    }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            citaViewModel.fetchCitasByDate(selectedDate.timeInMillis)
            Log.d("ManageAppointmentsScreen", "Fetched citas for date: ${selectedDate.timeInMillis}")
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
                horariosPorDia = empleado.horariosTrabajo[date.getDayOfWeekString()] ?: defaultHorariosPorDia()
                coroutineScope.launch {
                    citaViewModel.fetchCitasByDate(date.timeInMillis)
                }
            }

            HorariosTrabajo(horariosPorDia) { updatedHorarios ->
                horariosPorDia = updatedHorarios
                val updatedHorariosTrabajo = empleado.horariosTrabajo.toMutableMap().apply {
                    put(selectedDate.getDayOfWeekString(), updatedHorarios)
                }
                empleadoViewModel.updateEmpleado(empleado.copy(horariosTrabajo = updatedHorariosTrabajo))
            }

            AddAppointmentButton(navController)

            if (citas.isEmpty()) {
                Text("No appointments found for the selected date.")
            } else {
                val citasEnHorarioTrabajo = getCitasEnHorarioTrabajo(citas, empleado, selectedDate)
                HorarioCompletoConCitas(
                    citas = citasEnHorarioTrabajo,
                    onConfirmCita = { citaId ->
                        citaViewModel.confirmarCita(citaId)
                    },
                    onCancelCita = { citaId ->
                        citaViewModel.cancelarCita(citaId)
                    }
                )
            }
        }
    }
}
