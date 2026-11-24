package com.example.managerproject.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.example.managerproject.data.model.TaskStatus

/**
 * Colours for one task status: a strong colour for accents (dot, border, icon),
 * a soft container colour for backgrounds and a text colour that is readable on it.
 */
@Immutable
data class StatusColor(
    val main: Color,
    val container: Color,
    val onContainer: Color,
)

/**
 * Extra colours that Material 3's ColorScheme does not have.
 * They are provided through a CompositionLocal so every composable can read them
 * with MaterialTheme.statusColors, just like MaterialTheme.colorScheme.
 */
@Immutable
data class StatusColors(
    val todo: StatusColor,
    val inProgress: StatusColor,
    val done: StatusColor,
) {
    /** Returns the colour set for a status. */
    fun of(status: TaskStatus): StatusColor = when (status) {
        TaskStatus.TODO -> todo
        TaskStatus.IN_PROGRESS -> inProgress
        TaskStatus.DONE -> done
    }
}

val LightStatusColors = StatusColors(
    todo = StatusColor(TodoLight, TodoContainerLight, OnTodoContainerLight),
    inProgress = StatusColor(InProgressLight, InProgressContainerLight, OnInProgressContainerLight),
    done = StatusColor(DoneLight, DoneContainerLight, OnDoneContainerLight),
)

val DarkStatusColors = StatusColors(
    todo = StatusColor(TodoDark, TodoContainerDark, OnTodoContainerDark),
    inProgress = StatusColor(InProgressDark, InProgressContainerDark, OnInProgressContainerDark),
    done = StatusColor(DoneDark, DoneContainerDark, OnDoneContainerDark),
)

/** Holds the status colours for the current theme (light by default). */
val LocalStatusColors = staticCompositionLocalOf { LightStatusColors }
