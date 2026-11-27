package com.example.managerproject.ui.components

import androidx.compose.ui.graphics.Color

/** Colours a user can pick for a board (shown as the strip on the board card). */
object BoardColors {
    val options = listOf(
        "#F0904A", // soft orange (app accent)
        "#E57373", // red
        "#9575CD", // purple
        "#66BB6A", // green
        "#4FC3F7", // light blue
        "#FFD54F", // yellow
    )

    /** "#RRGGBB" -> Color. Falls back to the accent if the text is not a valid colour. */
    fun parse(hex: String): Color =
        runCatching { Color(android.graphics.Color.parseColor(hex)) }
            .getOrDefault(Color(0xFFF0904A))
}
