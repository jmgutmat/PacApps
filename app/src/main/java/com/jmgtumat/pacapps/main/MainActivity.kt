// MainActivity.kt
package com.jmgtumat.pacapps.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.jmgtumat.pacapps.navigation.AppNavigation
import com.jmgtumat.pacapps.ui.theme.PacAppsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PacAppsTheme {
                val navController = rememberNavController()
                AppNavigation(navController)
            }
        }
    }
}

