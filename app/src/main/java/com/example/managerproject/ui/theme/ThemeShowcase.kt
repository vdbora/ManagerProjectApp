package com.example.managerproject.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.managerproject.data.model.TaskStatus

/**
 * TEMPORARY screen that shows the theme: accent buttons and the three status colours.
 * It lets us check the palette in light and dark mode before the real screens exist.
 * It is replaced by the navigation graph in a later commit.
 */
@Composable
fun ThemeShowcase(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("ManagerProject theme", style = MaterialTheme.typography.headlineSmall)

        // Accent (soft orange) on the standard Material 3 buttons
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {}) { Text("Save") }
            FilledTonalButton(onClick = {}) { Text("Mark done") }
            OutlinedButton(onClick = {}) { Text("Cancel") }
        }
        ExtendedFloatingActionButton(
            onClick = {},
            icon = { Icon(Icons.Filled.Add, contentDescription = null) },
            text = { Text("New board") }
        )

        // One sample card per status: To Do = red, In Progress = purple, Done = green
        StatusSample(TaskStatus.TODO, "To Do", "Draw wireframes")
        StatusSample(TaskStatus.IN_PROGRESS, "In Progress", "Set up Room database")
        StatusSample(TaskStatus.DONE, "Done", "Create project")
    }
}

@Composable
private fun StatusSample(status: TaskStatus, columnName: String, taskTitle: String) {
    val colors = MaterialTheme.statusColors.of(status)
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainer,
        shape = MaterialTheme.shapes.large,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(10.dp).background(colors.main, CircleShape))
                Spacer(Modifier.width(8.dp))
                Text(columnName, style = MaterialTheme.typography.titleMedium)
            }
            Card(
                colors = CardDefaults.cardColors(containerColor = colors.container),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(12.dp)) {
                    Text(taskTitle, style = MaterialTheme.typography.titleSmall, color = colors.onContainer)
                    AssistChip(onClick = {}, label = { Text(columnName) })
                }
            }
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Composable
private fun ThemeShowcaseLightPreview() {
    ManagerProjectTheme(darkTheme = false) {
        Surface { ThemeShowcase() }
    }
}

@Preview(name = "Dark", showBackground = true, backgroundColor = 0xFF161210)
@Composable
private fun ThemeShowcaseDarkPreview() {
    ManagerProjectTheme(darkTheme = true) {
        Surface { ThemeShowcase() }
    }
}
