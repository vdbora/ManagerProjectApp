package com.example.managerproject.data.local.entity

/**
 * Read-only result of the Boards list query: a board plus card counts.
 * Room fills it from the SELECT in BoardDao (it is not a table).
 */
data class BoardSummary(
    val id: Long,
    val title: String,
    val colorHex: String,
    val createdAt: Long,
    /** All cards on the board that are not archived. */
    val cardCount: Int,
    /** Cards that are in a DONE column, used for the progress bar. */
    val doneCount: Int,
)
