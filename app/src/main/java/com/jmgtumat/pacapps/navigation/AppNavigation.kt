package com.jmgtumat.pacapps.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jmgtumat.pacapps.auth.EmailSignUpScreen
import com.jmgtumat.pacapps.clientmod.ClientHomeScreen
import com.jmgtumat.pacapps.clientmod.ClientmodHistoryScreen
import com.jmgtumat.pacapps.clientmod.NewAppointmentScreen
import com.jmgtumat.pacapps.clientmod.ProfileScreen
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
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = AppScreens.SplashScreen.route) {
        composable(AppScreens.SplashScreen.route) {
            SplashScreen(navController)
        }
        composable(AppScreens.MainScreen.route) {
            val viewModel: MainViewModel = viewModel()
            MainScreen(navController)
        }

        composable(AppScreens.EmailSignUpScreen.route) {
            EmailSignUpScreen(navController)
        }

        composable(AppScreens.ClientHomeScreen.route) {
            ClientHomeScreen(navController)
        }

        composable(AppScreens.NewAppointmentScreen.route) {
            NewAppointmentScreen(navController)
        }

        composable(AppScreens.ClientModHistoryScreen.route) {
            ClientmodHistoryScreen(navController)
        }
        composable(AppScreens.ProfileScreen.route) {
            ProfileScreen(navController)
        }
        composable(AppScreens.ManageAppointmentsScreen.route) {
            ManageAppointmentsScreen(navController)
        }
        composable(AppScreens.ManageEmployeesScreen.route) {
            ManageEmployeesScreen(navController)
        }
        composable(AppScreens.ManageServicesScreen.route) {
            ManageServicesScreen(navController)
        }
        composable(AppScreens.ManageClientsScreen.route) {
            ManageClientsScreen(navController)
        }

        composable(AppScreens.EmpModHistoryScreen.route) { backStackEntry ->
            val clienteId = backStackEntry.arguments?.getString("clienteId") ?: ""
            EmpModHistoryScreen(clienteId, navController)
        }

        composable(AppScreens.ReportsScreen.route) {
            ReportsScreen(navController)
        }
    }
}

fun redirectToRoleBasedScreen(navController: NavController, userId: String, getUserType: (String, (Boolean) -> Unit) -> Unit) {
    getUserType(userId) { isClient ->
        if (isClient) {
            navController.navigate("/client_home_screen")
        } else {
            navController.navigate("/manage_appointments_screen")
        }
    }
}
