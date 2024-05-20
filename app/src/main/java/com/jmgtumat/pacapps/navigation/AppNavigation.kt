package com.jmgtumat.pacapps.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

fun NavGraphBuilder.addNavigationGraph(navController: NavHostController) {
    composable(AppScreens.SplashScreen.route) {
        navController.navigate(AppScreens.SplashScreen.route)
    }

    composable(AppScreens.MainScreen.route) {
        navController.navigate(AppScreens.MainScreen.route)
    }
    // Add composables for other screens here
    composable(AppScreens.AddClientScreen.route) {
        navController.navigate(AppScreens.AddClientScreen.route)
    }

    composable(AppScreens.ViewClientsScreen.route) {
        navController.navigate(AppScreens.ViewClientsScreen.route)
    }

    composable(AppScreens.AddEmployeeScreen.route) {
        navController.navigate(AppScreens.AddEmployeeScreen.route)
    }

    composable(AppScreens.ViewEmployeesScreen.route) {
        navController.navigate(AppScreens.ViewEmployeesScreen.route)
    }

    composable(AppScreens.AddServiceScreen.route) {
        navController.navigate(AppScreens.AddServiceScreen.route)
    }

    composable(AppScreens.ViewServicesScreen.route) {
        navController.navigate(AppScreens.ViewServicesScreen.route)
    }
}
