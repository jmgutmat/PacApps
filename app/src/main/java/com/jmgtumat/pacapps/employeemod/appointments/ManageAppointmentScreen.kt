package com.jmgtumat.pacapps.employeemod.appointments

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.jmgtumat.pacapps.employeemod.EmpleadoDashboard
import com.jmgtumat.pacapps.util.getCitasEnHorarioTrabajo
import com.jmgtumat.pacapps.util.getDayOfWeekString
import com.jmgtumat.pacapps.viewmodels.AppViewModel
import kotlinx.coroutines.launch
import java.util.Calendar

@Composable
fun ManageAppointmentsScreen(
    navController: NavHostController,
    appViewModel: AppViewModel = viewModel()
) {
    val citas by appViewModel.citaViewModel.citas.observeAsState(emptyList())
    val empleados by appViewModel.empleadoViewModel.empleados.observeAsState(emptyList())
    val empleado = empleados.firstOrNull() ?: return // Obtener el primer empleado

    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }
    var horariosPorDia by remember {
        mutableStateOf(
            empleado.horariosTrabajo[selectedDate.getDayOfWeekString()] ?: defaultHorariosPorDia()
        )
    }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            appViewModel.citaViewModel.fetchCitasByDate(selectedDate.timeInMillis)
        }
    }

    EmpleadoDashboard(
        navController = navController,
        appViewModel = appViewModel
    ) {
        Column {
            DatePicker(selectedDate) { date ->
                selectedDate = date
                horariosPorDia = empleado.horariosTrabajo[date.getDayOfWeekString()] ?: defaultHorariosPorDia()
                coroutineScope.launch {
                    appViewModel.citaViewModel.fetchCitasByDate(date.timeInMillis)
                }
            }

            HorariosTrabajo(horariosPorDia) { updatedHorarios ->
                horariosPorDia = updatedHorarios
                val updatedHorariosTrabajo = empleado.horariosTrabajo.toMutableMap().apply {
                    put(selectedDate.getDayOfWeekString(), updatedHorarios)
                }
                appViewModel.empleadoViewModel.updateEmpleado(empleado.copy(horariosTrabajo = updatedHorariosTrabajo))
            }

            AddAppointmentButton(navController)

            val citasEnHorarioTrabajo = getCitasEnHorarioTrabajo(citas, empleado, selectedDate)
            HorarioCompletoConCitas(
                citas = citasEnHorarioTrabajo,
                onConfirmCita = { citaId ->
                    appViewModel.citaViewModel.confirmarCita(citaId)
                },
                onCancelCita = { citaId ->
                    appViewModel.citaViewModel.cancelarCita(citaId)
                }
            )
        }
    }
}
