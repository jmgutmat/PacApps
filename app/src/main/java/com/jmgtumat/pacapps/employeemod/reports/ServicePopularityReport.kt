package com.jmgtumat.pacapps.employeemod.reports

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jmgtumat.pacapps.data.Cita
import com.jmgtumat.pacapps.data.Servicio

data class ServicePopularityReport(
    val serviceName: String,
    val totalAppointments: Int
)

suspend fun generateServicePopularityReport(
    servicio: Servicio,
    citas: List<Cita>
): ServicePopularityReport {
    val serviceCitas = citas.filter { it.servicioId == servicio.id }
    val totalAppointments = serviceCitas.size

    return ServicePopularityReport(
        serviceName = servicio.nombre,
        totalAppointments = totalAppointments
    )
}

@Composable
fun ServicePopularityReportContent(report: List<ServicePopularityReport>) {
    Column {
        Text("Informe de popularidad de servicios:", style = MaterialTheme.typography.bodyLarge)
        report.forEach { serviceReport ->
            Text(
                text = "Servicio: ${serviceReport.serviceName}, " +
                        "Citas Totales: ${serviceReport.totalAppointments}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}
