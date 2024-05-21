// ReportsScreen.kt
package com.jmgtumat.pacapps.uiclases.reports

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jmgtumat.pacapps.uiclases.employees.EmployeeDashboard

@Composable
fun ReportsScreen(
    navController: NavController
) {
    EmployeeDashboard(
        currentSection = "View Reports",
        navController = navController
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Aquí irá el contenido principal de la pantalla de informes
            // Por ejemplo, gráficos y estadísticas
            Text(text = "Reportes de citas")
            Text(text = "Número de citas por día/semana/mes/año")
            Text(text = "Dinero facturado por día/semana/mes/año")
            // Más contenido según lo necesites
        }
    }
}
