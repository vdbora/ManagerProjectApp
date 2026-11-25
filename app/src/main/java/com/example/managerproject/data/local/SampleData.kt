package com.example.managerproject.data.local

import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Sample content written once, when the database file is first created.
 * A new user then sees an example board instead of an empty screen, and the
 * demo video starts with realistic data. If the user later deletes it, it does
 * not come back, because this runs only on database creation.
 */
object SampleData {

    private const val DAY = 24 * 60 * 60 * 1000L

    fun insert(db: SupportSQLiteDatabase) {
        val now = System.currentTimeMillis()

        // Board 1 with the three default columns
        db.execSQL("INSERT INTO boards (id, title, colorHex, createdAt) VALUES (1, 'University project', '#F0904A', $now)")
        db.execSQL("INSERT INTO board_columns (id, boardId, title, status, position) VALUES (1, 1, 'To Do', 'TODO', 0)")
        db.execSQL("INSERT INTO board_columns (id, boardId, title, status, position) VALUES (2, 1, 'In Progress', 'IN_PROGRESS', 1)")
        db.execSQL("INSERT INTO board_columns (id, boardId, title, status, position) VALUES (3, 1, 'Done', 'DONE', 2)")

        // Cards: (column, title, description, priority, due in days or null, position)
        val cards = listOf(
            arrayOf(1, "Draw wireframes", "Five key screens in portrait and landscape", 2, 1, 0),
            arrayOf(1, "Write UI tests", "Add card flow, cancel path and navigation", 1, 6, 1),
            arrayOf(2, "Set up Room database", "Entities, DAOs and repository", 2, 2, 0),
            arrayOf(3, "Create Android project", "Compose + Material 3 template", 0, null, 0),
        )
        cards.forEach { c ->
            val due = (c[4] as Int?)?.let { (now + it * DAY).toString() } ?: "NULL"
            db.execSQL(
                "INSERT INTO cards (columnId, title, description, priority, dueDate, photoPath, position, isArchived, createdAt, updatedAt) " +
                    "VALUES (?, ?, ?, ?, $due, NULL, ?, 0, $now, $now)",
                arrayOf(c[0], c[1], c[2], c[3], c[5])
            )
        }
    }
}
