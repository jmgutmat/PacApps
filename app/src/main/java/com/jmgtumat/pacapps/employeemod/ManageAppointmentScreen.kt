package com.jmgtumat.pacapps.employeemod

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.jmgtumat.pacapps.util.getCitasEnHorarioTrabajo
import com.jmgtumat.pacapps.util.getDayOfWeekString
import com.jmgtumat.pacapps.viewmodels.CitaViewModel
import com.jmgtumat.pacapps.viewmodels.EmpleadoViewModel
import java.util.Calendar

@Composable
fun ManageAppointmentsScreen(
    navController: NavController,
    citaViewModel: CitaViewModel,
    empleadoViewModel: EmpleadoViewModel
) {
    val citas by citaViewModel.citas.observeAsState(emptyList())
    val empleados by empleadoViewModel.empleados.observeAsState(emptyList())
    val empleado = empleados.firstOrNull() ?: return // Obtener el primer empleado

    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }
    var horariosPorDia by remember {
        mutableStateOf(
            empleado.horariosTrabajo[selectedDate.getDayOfWeekString()] ?: defaultHorariosPorDia()
        )
    }

    EmpleadoDashboard(
        citaViewModel = citaViewModel,
        empleadoViewModel = empleadoViewModel
    ) {
        Column {
            DatePicker(selectedDate) { date ->
                selectedDate = date
                horariosPorDia = empleado.horariosTrabajo[date.getDayOfWeekString()] ?: defaultHorariosPorDia()
            }

            HorariosTrabajo(horariosPorDia) { updatedHorarios ->
                horariosPorDia = updatedHorarios
                val updatedHorariosTrabajo = empleado.horariosTrabajo.toMutableMap().apply {
                    put(selectedDate.getDayOfWeekString(), updatedHorarios)
                }
                empleadoViewModel.updateEmpleado(empleado.copy(horariosTrabajo = updatedHorariosTrabajo))
            }

            AddAppointmentButton(navController)

            val citasEnHorarioTrabajo = getCitasEnHorarioTrabajo(citas, empleado, selectedDate)
            HorarioCompletoConCitas(citasEnHorarioTrabajo)
        }
    }
}

