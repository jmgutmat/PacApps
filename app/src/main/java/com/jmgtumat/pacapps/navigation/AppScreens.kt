package com.jmgtumat.pacapps.navigation

sealed class AppScreens(val route: String) {
    //MainScreens
    object SplashScreen : AppScreens("/splash_screen")
    object MainScreen : AppScreens("/main_screen")

    //ClientModScreens
    object ClientHomeScreen : AppScreens("/client_home_screen")
    object NewAppointmentScreen : AppScreens("/new_appointments_screen")
    object ClientModHistoryScreen : AppScreens("/clientmod_history_screen")
    object ProfileScreen : AppScreens("/profile_screen")

    //EmployeeModScreens
    object ManageAppointmentsScreen : AppScreens("/manage_appointments_screen")
    object ManageEmployeesScreen : AppScreens("/manage_employees_screen")
//    object EmployeeHistoryScreen : AppScreens("/employee_history_screen")
    object ManageServicesScreen : AppScreens("/manage_services_screen")
    object ManageClientsScreen : AppScreens("/manage_clients_screen")
    object EmpModHistoryScreen : AppScreens("/empmod_history_screen")
    object ReportsScreen : AppScreens("/reports_screen")

    //AuthScreens
    object EmailSignUpScreen : AppScreens("/email_sign_up_screen")

    /*companion object {
        fun getRouteForScreen(navController: NavHostController, screen: AppScreens) {
            return navController.navigate(screen.route)
        }
    }*/
}
