package com.example.managerproject.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A board, e.g. "University project". Table: boards.
 * A board owns its columns, and the columns own the cards (see the foreign keys there).
 */
@Entity(tableName = "boards")
data class BoardEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    /** Accent colour of the board in the list, stored as "#RRGGBB". */
    val colorHex: String = "#F0904A",
    /** Creation time in epoch milliseconds, used to sort the list. */
    val createdAt: Long = System.currentTimeMillis(),
)
