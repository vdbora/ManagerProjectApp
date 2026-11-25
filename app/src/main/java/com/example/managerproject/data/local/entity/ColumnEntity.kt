package com.example.managerproject.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.managerproject.data.model.TaskStatus

/**
 * A column on a board, e.g. "To Do". Table: board_columns
 * ("columns" alone is avoided because it is close to an SQL keyword).
 *
 * onDelete = CASCADE: deleting a board automatically deletes its columns.
 */
@Entity(
    tableName = "board_columns",
    foreignKeys = [
        ForeignKey(
            entity = BoardEntity::class,
            parentColumns = ["id"],
            childColumns = ["boardId"],
            onDelete = ForeignKey.CASCADE,
        )
    ],
    // Index on the foreign key so "columns of board X" is fast
    indices = [Index("boardId")],
)
data class ColumnEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val boardId: Long,
    val title: String,
    /** Decides the colour of the column: red / purple / green. */
    val status: TaskStatus,
    /** Order of the column on the board (0 = first). */
    val position: Int,
)
