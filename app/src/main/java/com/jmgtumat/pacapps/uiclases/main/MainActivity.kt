package com.jmgtumat.pacapps.uiclases.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.jmgtumat.pacapps.navigation.AppNavigation
import com.jmgtumat.pacapps.ui.theme.PacAppsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PacAppsTheme {
                AppNavigation()
            }
        }
    }
}



