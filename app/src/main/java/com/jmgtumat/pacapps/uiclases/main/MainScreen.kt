package com.jmgtumat.pacapps.uiclases.main


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jmgtumat.pacapps.navigation.AppScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text("PAC Apps") },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {}) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MainScreenButton(text = "View Clients", onClick = {
                navController.navigate(AppScreens.ViewClientsScreen.route)
            })
            MainScreenButton(text = "View Employees", onClick = {
                navController.navigate(AppScreens.ViewEmployeesScreen.route)
            })
            // ... botones para otras pantallas
        }
    }
}

@Composable
fun MainScreenButton(text: String, onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(text)
    }
}
