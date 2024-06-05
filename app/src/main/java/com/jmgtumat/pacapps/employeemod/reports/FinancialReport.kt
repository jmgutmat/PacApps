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

/**
 * Clase de datos que representa un informe financiero.
 *
 * @property ingresosPorServicio Mapa que almacena los ingresos por servicio.
 * @property ingresoTotal El ingreso total generado durante el período especificado.
 */
data class FinancialReport(
    val ingresosPorServicio: Map<String, Double>,
    val ingresoTotal: Double
)

/**
 * Genera un informe financiero que muestra los ingresos totales y los ingresos por servicio durante un período específico.
 *
 * @param startDate La fecha de inicio del período.
 * @param endDate La fecha de fin del período.
 * @param citaViewModel El ViewModel de citas para acceder a los datos de las citas.
 * @param servicioViewModel El ViewModel de servicios para acceder a los datos de los servicios.
 * @return El informe financiero generado.
 */
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

/**
 * Composable que muestra el contenido del informe financiero.
 *
 * @param report El informe financiero a mostrar.
 */
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
