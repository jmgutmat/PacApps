package com.jmgtumat.pacapps.navigation

import androidx.navigation.NavHostController

sealed class AppScreens(val route: String) {
    //MainScreens
    object SplashScreen : AppScreens("/splash_screen")
    object MainScreen : AppScreens("/main_screen")

    //ClientScreens
    object AddClientScreen : AppScreens("/add_client_screen")
    object ClientScreen : AppScreens("/client_screen")
    object RegisterScreen : AppScreens("register_screen")
    object ViewClientsScreen : AppScreens("/view_clients_screen")

    //EmployeesScreens
    object AddEmployeeScreen : AppScreens("/add_employee_screen")
    object EmployeeScreen : AppScreens("/employee_screen")
    object ViewEmployeesScreen : AppScreens("/view_employees_screen")

    //ServicesScreens
    object AddServiceScreen : AppScreens("/add_service_screen")
    object ViewServicesScreen : AppScreens("/view_services_screen")
    object SelectServiceScreen : AppScreens("/select_services_screen")

    //AppointmentsScreens
    object SelectDateScreen : AppScreens("/select_date_screen")
    object AddAppointmentScreen : AppScreens("/add_appointment_screen")
    object SelectTimeScreen : AppScreens("/select_time_screen")
    object ConfirmAppointmentScreen : AppScreens("/confirm_appointment_screen")

    //ManageScreens
    object ManageAppointmentsScreen : AppScreens("/manage_appointments_screen")
    object ManageEmployeesScreen : AppScreens("/manage_employees_screen")
    object ManageServicesScreen : AppScreens("/manage_services_screen")
    object ManageClientsScreen : AppScreens("/manage_clients_screen")
    object ReportsScreen : AppScreens("/reports_screen")

    companion object {
        fun getRouteForScreen(navController: NavHostController, screen: AppScreens) {
            return navController.navigate(screen.route)
        }
    }
}
