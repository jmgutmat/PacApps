package com.jmgtumat.pacapps.employeemod.reports

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jmgtumat.pacapps.data.Cita
import com.jmgtumat.pacapps.data.Servicio

/**
 * Clase que representa un informe de popularidad de servicio.
 *
 * @property serviceName Nombre del servicio.
 * @property totalAppointments Número total de citas para el servicio.
 */
data class ServicePopularityReport(
    val serviceName: String,
    val totalAppointments: Int
)

/**
 * Función suspendida que genera un informe de popularidad de servicio basado en las citas proporcionadas.
 *
 * @param servicio El servicio para el cual se generará el informe de popularidad.
 * @param citas La lista de citas para analizar.
 * @return El informe de popularidad del servicio.
 */
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

/**
 * Composable que muestra el contenido del informe de popularidad de servicios.
 *
 * @param report La lista de informes de popularidad de servicios a mostrar.
 */
@Composable
fun ServicePopularityReportContent(report: List<ServicePopularityReport>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            "Informe de popularidad de servicios:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        report.forEach { serviceReport ->
            Text(
                text = "Servicio: ${serviceReport.serviceName}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Citas Totales: ${serviceReport.totalAppointments}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}
