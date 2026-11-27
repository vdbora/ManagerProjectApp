package com.example.managerproject.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** A button shown on a temporary screen: its label and what it does. */
data class TempAction(val label: String, val onClick: () -> Unit)

/**
 * TEMPORARY destination used while the real screens are being built.
 * It already uses the real navigation callbacks, so every route, argument and
 * back path can be tested. Each one is replaced by its real screen in a later commit.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemporaryScreen(
    title: String,
    description: String,
    actions: List<TempAction>,
    onNavigateUp: (() -> Unit)? = null,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    // Up arrow only on screens that have somewhere to go back to
                    if (onNavigateUp != null) {
                        IconButton(onClick = onNavigateUp) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(description, style = MaterialTheme.typography.bodyLarge)
            actions.forEachIndexed { index, action ->
                if (index == 0) {
                    Button(onClick = action.onClick, modifier = Modifier.fillMaxWidth()) { Text(action.label) }
                } else {
                    OutlinedButton(onClick = action.onClick, modifier = Modifier.fillMaxWidth()) { Text(action.label) }
                }
            }
        }
    }
}
