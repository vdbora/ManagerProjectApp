package com.example.managerproject.ui.boards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.managerproject.data.local.entity.BoardSummary
import com.example.managerproject.ui.components.BoardColors

/** Longest allowed board name. */
const val MAX_BOARD_NAME = 40

/**
 * Returns an error message for a board name, or null when it is valid.
 * Kept as a plain function so it can be unit tested.
 */
fun validateBoardName(name: String): String? = when {
    name.isBlank() -> "Name is required"
    name.trim().length > MAX_BOARD_NAME -> "Name must be $MAX_BOARD_NAME characters or less"
    else -> null
}

/**
 * Dialog used to create or rename a board.
 * The typed text is kept with rememberSaveable, so it is not lost on rotation.
 */
@Composable
fun BoardNameDialog(
    title: String,
    confirmLabel: String,
    initialName: String,
    showColorPicker: Boolean,
    onConfirm: (name: String, colorHex: String) -> Unit,
    onDismiss: () -> Unit,
) {
    var name by rememberSaveable { mutableStateOf(initialName) }
    var color by rememberSaveable { mutableStateOf(BoardColors.options.first()) }
    // Show the error only after the user tried to confirm, not while the field is still empty
    var showErrors by rememberSaveable { mutableStateOf(false) }
    val error = validateBoardName(name)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Board name") },
                    singleLine = true,
                    isError = showErrors && error != null,
                    supportingText = {
                        if (showErrors && error != null) Text(error) else Text("${name.length}/$MAX_BOARD_NAME")
                    },
                    modifier = Modifier.testTag("board_name_field"),
                )
                if (showColorPicker) {
                    Text("Colour", style = MaterialTheme.typography.labelLarge)
                    ColorPicker(selected = color, onSelect = { color = it })
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    showErrors = true
                    if (error == null) onConfirm(name, color)
                },
                modifier = Modifier.testTag("board_dialog_confirm"),
            ) { Text(confirmLabel) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    )
}

/** Row of colour circles; the selected one shows a tick. */
@Composable
private fun ColorPicker(selected: String, onSelect: (String) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        BoardColors.options.forEachIndexed { index, hex ->
            val isSelected = hex == selected
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(36.dp)
                    .background(BoardColors.parse(hex), CircleShape)
                    .border(
                        width = if (isSelected) 3.dp else 0.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                        shape = CircleShape,
                    )
                    .clickable { onSelect(hex) }
                    .semantics { contentDescription = "Colour ${index + 1}" + if (isSelected) ", selected" else "" },
            ) {
                if (isSelected) Icon(Icons.Filled.Check, contentDescription = null, tint = Color.Black)
            }
        }
    }
}

/** Confirmation before a destructive action: deleting a board removes all its cards. */
@Composable
fun DeleteBoardDialog(board: BoardSummary, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Filled.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
        title = { Text("Delete \"${board.title}\"?") },
        text = { Text("This board and its ${board.cardCount} cards will be deleted. This cannot be undone.") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Delete", color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    )
}
