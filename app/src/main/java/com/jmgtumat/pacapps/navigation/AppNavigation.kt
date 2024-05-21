package com.jmgtumat.pacapps.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jmgtumat.pacapps.uiclases.clients.AddClientScreen
import com.jmgtumat.pacapps.uiclases.clients.ViewClientsScreen
import com.jmgtumat.pacapps.uiclases.employees.AddEmployeeScreen
import com.jmgtumat.pacapps.uiclases.employees.ViewEmployeesScreen
import com.jmgtumat.pacapps.main.MainScreen
import com.jmgtumat.pacapps.main.SplashScreen
import com.jmgtumat.pacapps.uiclases.services.AddServiceScreen
import com.jmgtumat.pacapps.uiclases.services.ViewServicesScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController, startDestination = AppScreens.SplashScreen.route) {
        composable(AppScreens.SplashScreen.route) {
            SplashScreen(navController)
        }
        composable(AppScreens.MainScreen.route) {
            MainScreen(navController, launchGoogleSignIn)
        }
        composable(AppScreens.AddClientScreen.route) {
            AddClientScreen(navController, viewModel(), onNavigateBack = {})
        }
        composable(AppScreens.ViewClientsScreen.route) {
            ViewClientsScreen()
        }
        composable(AppScreens.AddEmployeeScreen.route) {
            AddEmployeeScreen()
        }
        composable(AppScreens.ViewEmployeesScreen.route) {
            ViewEmployeesScreen()
        }
        composable(AppScreens.AddServiceScreen.route) {
            AddServiceScreen()
        }
        composable(AppScreens.ViewServicesScreen.route) {
            ViewServicesScreen()
        }
    }
}
