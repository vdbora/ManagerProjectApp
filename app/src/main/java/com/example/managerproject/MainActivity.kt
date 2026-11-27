package com.example.managerproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.managerproject.ui.navigation.AppNavHost
import com.example.managerproject.ui.theme.ManagerProjectTheme

/**
 * Single activity of the app. Every screen is a composable destination inside AppNavHost.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Draw behind the status and navigation bars (each screen's Scaffold handles the insets)
        enableEdgeToEdge()
        setContent {
            ManagerProjectTheme {
                AppNavHost()
            }
        }
    }
}
