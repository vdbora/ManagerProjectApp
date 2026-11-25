package com.example.managerproject.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * A task card inside a column. Table: cards.
 *
 * onDelete = CASCADE: deleting a column (or its board) deletes its cards.
 * The photo itself is a JPEG file in app storage; only its path is stored here.
 */
@Entity(
    tableName = "cards",
    foreignKeys = [
        ForeignKey(
            entity = ColumnEntity::class,
            parentColumns = ["id"],
            childColumns = ["columnId"],
            onDelete = ForeignKey.CASCADE,
        )
    ],
    indices = [Index("columnId"), Index("dueDate")],
)
data class CardEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val columnId: Long,
    val title: String,
    val description: String = "",
    /** 0 = Low, 1 = Medium, 2 = High (see Priority). */
    val priority: Int = Priority.MEDIUM,
    /** Due date in epoch milliseconds, or null when the task has no deadline. */
    val dueDate: Long? = null,
    /** Absolute path of the photo taken with CameraX, or null. */
    val photoPath: String? = null,
    /** Order inside the column (0 = top). */
    val position: Int = 0,
    /** Archived cards are hidden and removed by the cleanup worker after 30 days. */
    val isArchived: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)

/** Priority values stored in CardEntity.priority. */
object Priority {
    const val LOW = 0
    const val MEDIUM = 1
    const val HIGH = 2
}
