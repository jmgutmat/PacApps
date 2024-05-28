package com.jmgtumat.pacapps.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jmgtumat.pacapps.auth.EmailSignUpScreen
import com.jmgtumat.pacapps.clientmod.ClientHomeScreen
import com.jmgtumat.pacapps.clientmod.ClientmodHistoryScreen
import com.jmgtumat.pacapps.clientmod.ProfileScreen
import com.jmgtumat.pacapps.data.UserRole
import com.jmgtumat.pacapps.employeemod.appointments.ManageAppointmentsScreen
import com.jmgtumat.pacapps.employeemod.clients.EmpModHistoryScreen
import com.jmgtumat.pacapps.employeemod.clients.ManageClientsScreen
import com.jmgtumat.pacapps.employeemod.employees.ManageEmployeesScreen
import com.jmgtumat.pacapps.employeemod.reports.ReportsScreen
import com.jmgtumat.pacapps.employeemod.services.ManageServicesScreen
import com.jmgtumat.pacapps.main.MainScreen
import com.jmgtumat.pacapps.main.SplashScreen
import com.jmgtumat.pacapps.viewmodels.MainViewModel

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController, startDestination = AppScreens.SplashScreen.route) {
        composable(AppScreens.SplashScreen.route) {
            SplashScreen(navController)
        }
        composable(AppScreens.MainScreen.route) {
            val viewModel: MainViewModel = viewModel()
            MainScreen(navController, viewModel)
        }

        composable(AppScreens.EmailSignUpScreen.route) {
            EmailSignUpScreen(viewModel(), navController)
        }

        composable(AppScreens.ClientHomeScreen.route) {
            ClientHomeScreen(navController)
        }
        composable(AppScreens.ClientModHistoryScreen.route) {
            ClientmodHistoryScreen(navController, viewModel())
        }
        composable(AppScreens.ProfileScreen.route) {
            ProfileScreen(navController, viewModel())
        }
        composable(AppScreens.ManageAppointmentsScreen.route) {
            ManageAppointmentsScreen(navController, viewModel())
        }
        composable(AppScreens.ManageEmployeesScreen.route) {
            ManageEmployeesScreen(navController, viewModel())
        }
        composable(AppScreens.ManageServicesScreen.route) {
            ManageServicesScreen(viewModel())
        }
        composable(AppScreens.ManageClientsScreen.route) {
            ManageClientsScreen(navController = navController, viewModel())
        }

        composable(AppScreens.EmpModHistoryScreen.route) { backStackEntry ->
            val clienteId = backStackEntry.arguments?.getString("clienteId") ?: ""
            EmpModHistoryScreen(clienteId, viewModel(), navController)
        }

        composable(AppScreens.ReportsScreen.route) {
            ReportsScreen(navController, viewModel())
        }
    }
}

fun redirectToRoleBasedScreen(navController: NavHostController, userId: String, roleProvider: (String, (UserRole) -> Unit) -> Unit) {
    roleProvider(userId) { role ->
        when (role) {
            UserRole.CLIENTE -> {
                navController.navigate(AppScreens.ClientHomeScreen.route) {
                    popUpTo(AppScreens.MainScreen.route) { inclusive = true }
                }
            }
            UserRole.EMPLEADO, UserRole.ADMINISTRADOR -> {
                navController.navigate(AppScreens.ManageAppointmentsScreen.route) {
                    popUpTo(AppScreens.MainScreen.route) { inclusive = true }
                }
            }
        }
    }
}
