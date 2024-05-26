package com.jmgtumat.pacapps.employeemod.reports

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.jmgtumat.pacapps.employeemod.EmpleadoDashboard
import com.jmgtumat.pacapps.viewmodels.AppViewModel

@Composable
fun ReportsScreen(
    navController: NavHostController = rememberNavController(),
    appViewModel: AppViewModel = viewModel()
) {
    val context = LocalContext.current

    EmpleadoDashboard(
        navController = navController,
        appViewModel = appViewModel
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text("Informes y Estadísticas", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))
            ReportContent(
                context = context,
                appViewModel = appViewModel,
                startDate = remember { mutableStateOf<Long?>(null) },
                endDate = remember { mutableStateOf<Long?>(null) }
            )
        }
    }
}
