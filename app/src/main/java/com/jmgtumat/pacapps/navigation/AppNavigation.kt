package com.jmgtumat.pacapps.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jmgtumat.pacapps.clientmod.ClientmodHistoryScreen
import com.jmgtumat.pacapps.data.UserRole
import com.jmgtumat.pacapps.uiclases.clients.AddClientScreen
import com.jmgtumat.pacapps.uiclases.clients.ViewClientsScreen
import com.jmgtumat.pacapps.uiclases.employees.AddEmployeeScreen
import com.jmgtumat.pacapps.uiclases.employees.ViewEmployeesScreen
import com.jmgtumat.pacapps.main.MainScreen
import com.jmgtumat.pacapps.main.SplashScreen
import com.jmgtumat.pacapps.uiclases.services.AddServiceScreen
import com.jmgtumat.pacapps.uiclases.services.ViewServicesScreen
import com.jmgtumat.pacapps.viewmodels.EmailSignInViewModel

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController, startDestination = AppScreens.SplashScreen.route) {
        composable(AppScreens.SplashScreen.route) {
            SplashScreen(navController)
        }
        composable(AppScreens.MainScreen.route) {
            MainScreen(navController)
        }
        composable(AppScreens.AddClientScreen.route) {
            AddClientScreen(navController, viewModel())
        }
        composable(AppScreens.ViewClientsScreen.route) {
            ViewClientsScreen()
        }
        composable(AppScreens.HistoryScreen.route) {
            ClientmodHistoryScreen()
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
fun redirectToRoleBasedScreen(navController: NavHostController, userId: String, viewModel: EmailSignInViewModel) {
    viewModel.fetchUserRole(userId) { role ->
        when (role) {
            UserRole.CLIENTE -> navController.navigate(AppScreens.ClientScreen.route)
            UserRole.EMPLEADO, UserRole.ADMINISTRADOR -> navController.navigate(AppScreens.EmployeeScreen.route)
        }
    }
}
