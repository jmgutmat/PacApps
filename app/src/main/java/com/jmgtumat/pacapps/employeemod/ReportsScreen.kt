package com.jmgtumat.pacapps.employeemod

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.jmgtumat.pacapps.viewmodels.CitaViewModel
import com.jmgtumat.pacapps.viewmodels.ClienteViewModel
import com.jmgtumat.pacapps.viewmodels.EmpleadoViewModel
import com.jmgtumat.pacapps.viewmodels.ServicioViewModel

@Composable
fun ReportsScreen(
    navController: NavHostController = rememberNavController(),
    citaViewModel: CitaViewModel = viewModel(),
    clienteViewModel: ClienteViewModel = viewModel(),
    empleadoViewModel: EmpleadoViewModel = viewModel(),
    servicioViewModel: ServicioViewModel = viewModel()
) {
    val context = LocalContext.current

    EmpleadoDashboard(
        navController = navController,
        citaViewModel = citaViewModel,
        clienteViewModel = clienteViewModel,
        empleadoViewModel = empleadoViewModel,
        servicioViewModel = servicioViewModel
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text("Informes y Estadísticas", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))
            ReportContent(context, citaViewModel, clienteViewModel, empleadoViewModel, servicioViewModel)
        }
    }
}

@Composable
fun ReportContent(
    context: Context,
    citaViewModel: CitaViewModel,
    clienteViewModel: ClienteViewModel,
    empleadoViewModel: EmpleadoViewModel,
    servicioViewModel: ServicioViewModel
) {
    // Aquí agregarás los diferentes gráficos y tablas
    Column {
        PerformanceChart(context, citaViewModel)
        Spacer(modifier = Modifier.height(16.dp))
        FinancialReport(clienteViewModel)
        Spacer(modifier = Modifier.height(16.dp))
        EmployeePerformanceReport(empleadoViewModel)
        Spacer(modifier = Modifier.height(16.dp))
        ServicePopularityReport(servicioViewModel)
    }
}

@Composable
fun PerformanceChart(context: Context, citaViewModel: CitaViewModel) {
    // Obtenemos las citas reales desde el ViewModel
    val citas = citaViewModel.citas.value ?: emptyList()

    // Convertimos las citas a puntos de datos para el gráfico
    val points = citas.mapIndexed { index, cita ->
        Entry(index.toFloat(), cita.duracion.toFloat()) // Modificar la obtención de la duración real según la estructura de la cita
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Desempeño Mensual", style = MaterialTheme.typography.titleMedium)
            LineChart(context).apply {
                data = LineData(LineDataSet(points, "Performance").apply {
                    color = Color.Blue.toArgb()
                    setCircleColor(Color.Blue.toArgb())
                    setDrawValues(false)
                })
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                axisRight.isEnabled = false
                description.isEnabled = false
                legend.isEnabled = false
                axisLeft.setDrawGridLines(false)
                xAxis.setDrawGridLines(false)
                invalidate()
            }
        }
    }
}

@Composable
fun FinancialReport(clienteViewModel: ClienteViewModel) {
    // Obtenemos los clientes reales desde el ViewModel
    val clientes = clienteViewModel.clientes.value ?: emptyList()

    // Aquí puedes agregar lógica para generar un informe financiero utilizando los datos de los clientes
    // Por ejemplo, puedes calcular estadísticas financieras basadas en los datos de los clientes
}

@Composable
fun EmployeePerformanceReport(empleadoViewModel: EmpleadoViewModel) {
    // Obtenemos los empleados reales desde el ViewModel
    val empleados = empleadoViewModel.empleados.value ?: emptyList()

    // Aquí puedes agregar lógica para generar un informe de rendimiento de empleados utilizando los datos de los empleados
}

@Composable
fun ServicePopularityReport(servicioViewModel: ServicioViewModel) {
    // Obtenemos los servicios reales desde el ViewModel
    val servicios = servicioViewModel.servicios.value ?: emptyList()

    // Aquí puedes agregar lógica para generar un informe de popularidad de servicios utilizando los datos de los servicios
}
