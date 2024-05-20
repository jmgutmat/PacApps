package com.jmgtumat.pacapps.navigation

import androidx.navigation.NavHostController

sealed class AppScreens(val route: String) {
    object SplashScreen : AppScreens("/splash_screen")
    object MainScreen : AppScreens("/main_screen")
    object AddClientScreen : AppScreens("/add_client_screen")
    object ViewClientsScreen : AppScreens("/view_clients_screen")
    object AddEmployeeScreen : AppScreens("/add_employee_screen")
    object ViewEmployeesScreen : AppScreens("/view_employees_screen")
    object AddServiceScreen : AppScreens("/add_service_screen")
    object ViewServicesScreen : AppScreens("/view_services_screen")

    companion object {
        fun getRouteForScreen(navController: NavHostController, screen: AppScreens) {
            return navController.navigate(screen.route)
        }
    }
}
