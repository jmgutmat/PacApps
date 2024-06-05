package com.jmgtumat.pacapps.employeemod.reports

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jmgtumat.pacapps.data.CitaEstado
import com.jmgtumat.pacapps.viewmodels.CitaViewModel
import com.jmgtumat.pacapps.viewmodels.ServicioViewModel

data class FinancialReport(
    val ingresosPorServicio: Map<String, Double>,
    val ingresoTotal: Double
)

suspend fun generateFinancialReport(
    startDate: Long,
    endDate: Long,
    citaViewModel: CitaViewModel,
    servicioViewModel: ServicioViewModel
): FinancialReport {
    // Obtén las citas confirmadas dentro del rango de fechas
    val citasInRange = citaViewModel.getCitasByDateRange(startDate, endDate)
    val confirmedCitas = citasInRange.filter { it.estado == CitaEstado.CONFIRMADA }

    // Obtén la lista de servicios
    val servicios = servicioViewModel.servicios.value ?: emptyList()

    // Mapa para almacenar ingresos por servicio
    val ingresosPorServicio = mutableMapOf<String, Double>()
    var ingresoTotal = 0.0

    // Calcula ingresos por servicio
    for (cita in confirmedCitas) {
        val servicio = servicios.find { it.id == cita.servicioId }
        if (servicio != null) {
            val ingresoActual = ingresosPorServicio.getOrDefault(servicio.nombre, 0.0)
            ingresosPorServicio[servicio.nombre] = ingresoActual + servicio.precio
            ingresoTotal += servicio.precio
        }
    }

    return FinancialReport(
        ingresosPorServicio = ingresosPorServicio,
        ingresoTotal = ingresoTotal
    )
}


@Composable
fun FinancialReportContent(report: FinancialReport) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            "Ingresos Totales:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "${report.ingresoTotal} €",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            "Ingresos por Servicio:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        report.ingresosPorServicio.forEach { (servicio, ingreso) ->
            Text(
                text = "$servicio: $ingreso €",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}
