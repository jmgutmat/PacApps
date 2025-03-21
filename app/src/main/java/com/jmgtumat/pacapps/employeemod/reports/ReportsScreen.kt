package com.jmgtumat.pacapps.employeemod.reports

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jmgtumat.pacapps.employeemod.EmpleadoDashboard

/**
 * @Composable que muestra la pantalla de informes y estadísticas.
 *
 * @param navController El controlador de navegación para la navegación entre destinos.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(navController: NavController) {
    val context = LocalContext.current

    EmpleadoDashboard(navController = navController) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            TopAppBar(
                title = { Text("Informes y Estadísticas") },
                navigationIcon = {
                    IconButton(onClick = { /* Manejar clic en el ícono de navegación */ }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            ReportContent(
                context = context,
                startDate = remember { mutableStateOf<Long?>(null) },
                endDate = remember { mutableStateOf<Long?>(null) }
            )
        }
    }
}
