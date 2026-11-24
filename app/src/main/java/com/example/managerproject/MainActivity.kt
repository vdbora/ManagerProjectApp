package com.example.managerproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.managerproject.ui.theme.ManagerProjectTheme
import com.example.managerproject.ui.theme.ThemeShowcase

/**
 * Single activity of the app. All screens are composables hosted here.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Draw behind the status and navigation bars (insets are handled by Scaffold)
        enableEdgeToEdge()
        setContent {
            ManagerProjectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Temporary: shows the theme until the navigation graph is added
                    ThemeShowcase(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
