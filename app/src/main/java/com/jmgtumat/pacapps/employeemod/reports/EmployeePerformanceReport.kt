package com.jmgtumat.pacapps.employeemod.reports

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jmgtumat.pacapps.data.Cita
import com.jmgtumat.pacapps.data.CitaEstado
import com.jmgtumat.pacapps.data.Empleado

data class EmployeePerformanceReport(
    val employeeName: String,
    val totalAppointments: Int,
    val confirmedAppointments: Int,
    val canceledAppointments: Int
)

suspend fun generateEmployeePerformanceReport(
    empleado: Empleado,
    citas: List<Cita>
): EmployeePerformanceReport {
    val employeeCitas = citas.filter { it.empleadoId == empleado.id }
    val totalAppointments = employeeCitas.size
    val confirmedAppointments = employeeCitas.count { it.estado == CitaEstado.CONFIRMADA }
    val canceledAppointments = employeeCitas.count { it.estado == CitaEstado.CANCELADA }

    return EmployeePerformanceReport(
        employeeName = "${empleado.nombre} ${empleado.apellidos}",
        totalAppointments = totalAppointments,
        confirmedAppointments = confirmedAppointments,
        canceledAppointments = canceledAppointments
    )
}

@Composable
fun EmployeePerformanceReportContent(report: List<EmployeePerformanceReport>) {
    Column {
        Text("Informe de rendimiento de empleados:", style = MaterialTheme.typography.bodyLarge)
        report.forEach { employeeReport ->
            Text(
                text = "Empleado: ${employeeReport.employeeName}, " +
                        "Citas Totales: ${employeeReport.totalAppointments}, " +
                        "Citas Confirmadas: ${employeeReport.confirmedAppointments}, " +
                        "Citas Canceladas: ${employeeReport.canceledAppointments}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}