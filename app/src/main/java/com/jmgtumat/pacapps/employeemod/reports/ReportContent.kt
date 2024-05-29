package com.jmgtumat.pacapps.employeemod.reports

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jmgtumat.pacapps.repository.CitaRepository
import com.jmgtumat.pacapps.repository.EmpleadoRepository
import com.jmgtumat.pacapps.repository.ServicioRepository
import com.jmgtumat.pacapps.util.DateSelector
import com.jmgtumat.pacapps.viewmodels.CitaViewModel
import com.jmgtumat.pacapps.viewmodels.CitaViewModelFactory
import com.jmgtumat.pacapps.viewmodels.EmpleadoViewModel
import com.jmgtumat.pacapps.viewmodels.EmpleadoViewModelFactory
import com.jmgtumat.pacapps.viewmodels.ServicioViewModel
import com.jmgtumat.pacapps.viewmodels.ServicioViewModelFactory

@Composable
fun ReportContent(
    context: Context,
    startDate: MutableState<Long?>,
    endDate: MutableState<Long?>
) {
    var financialReport by remember(startDate, endDate) {
        mutableStateOf<FinancialReport?>(null)
    }

    var employeePerformanceReport by remember(startDate, endDate) {
        mutableStateOf<List<EmployeePerformanceReport>?>(null)
    }

    var servicePopularityReport by remember(startDate, endDate) {
        mutableStateOf<List<ServicePopularityReport>?>(null)
    }

    val citaViewModel: CitaViewModel = viewModel(
        factory = CitaViewModelFactory(
            CitaRepository(/* parámetros de configuración si los hay */),
        )
    )
    val empleadoViewModel: EmpleadoViewModel = viewModel(
        factory = EmpleadoViewModelFactory(
            EmpleadoRepository(/* parámetros de configuración si los hay */),
        )
    )
    val servicioViewModel: ServicioViewModel = viewModel(
        factory = ServicioViewModelFactory(
            ServicioRepository(/* parámetros de configuración si los hay */),
        )
    )

    LaunchedEffect(startDate.value, endDate.value) {
        if (startDate.value != null && endDate.value != null) {
            financialReport = generateFinancialReport(
                startDate = startDate.value!!,
                endDate = endDate.value!!,
                citaViewModel = citaViewModel,
                servicioViewModel = servicioViewModel
            )

            val empleados = empleadoViewModel.empleados.value ?: emptyList()
            val citas = citaViewModel.citas.value ?: emptyList()
            val servicios = servicioViewModel.servicios.value ?: emptyList()

            // Generar informe de rendimiento de empleados
            employeePerformanceReport = empleados.map { empleado ->
                generateEmployeePerformanceReport(empleado, citas)
            }

            // Generar informe de popularidad de servicios
            servicePopularityReport = servicios.map { servicio ->
                generateServicePopularityReport(servicio, citas)
            }
        }
    }

    Column {
        DateSelector(
            context = context,
            startDate = startDate,
            endDate = endDate
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (financialReport != null) {
            FinancialReportContent(report = financialReport!!)
        } else {
            Text("Seleccione ambas fechas para generar los informes", style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.height(16.dp))

        PerformanceChart(context, citaViewModel)

        Spacer(modifier = Modifier.height(16.dp))

        if (employeePerformanceReport != null) {
            EmployeePerformanceReportContent(report = employeePerformanceReport!!)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (servicePopularityReport != null) {
            ServicePopularityReportContent(report = servicePopularityReport!!)
        }
    }
}
