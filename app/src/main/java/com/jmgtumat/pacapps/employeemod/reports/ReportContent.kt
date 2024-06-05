package com.jmgtumat.pacapps.employeemod.reports

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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

/**
 * @Composable que muestra el contenido de los informes financieros, de rendimiento de empleados y de popularidad de servicios.
 *
 * @param context El contexto de la aplicación.
 * @param startDate El estado mutable que representa la fecha de inicio del período del informe.
 * @param endDate El estado mutable que representa la fecha de fin del período del informe.
 */
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
    Log.d("ReportContent", "Citas: ${citaViewModel.citas.value}")

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

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            DateSelectorReport(context = context, startDate = startDate, endDate = endDate)
            Spacer(modifier = Modifier.height(16.dp))
        }

        financialReport?.let {
            item {
                FinancialReportContent(report = it)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        employeePerformanceReport?.let {
            item {
                EmployeePerformanceReportContent(report = it)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        servicePopularityReport?.let {
            item {
                ServicePopularityReportContent(report = it)
            }
        }
    }
}

/**
 * @Composable que muestra el selector de fechas para filtrar los informes.
 *
 * @param context El contexto de la aplicación.
 * @param startDate El estado mutable que representa la fecha de inicio del período del informe.
 * @param endDate El estado mutable que representa la fecha de fin del período del informe.
 */
@Composable
fun DateSelectorReport(
    context: Context,
    startDate: MutableState<Long?>,
    endDate: MutableState<Long?>
) {
    Column {
        DateSelector(
            context = LocalContext.current,
            startDate = startDate,
            endDate = endDate
        )
    }
}
