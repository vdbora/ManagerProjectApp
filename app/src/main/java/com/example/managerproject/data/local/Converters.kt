package com.example.managerproject.data.local

import androidx.room.TypeConverter
import com.example.managerproject.data.model.TaskStatus

/**
 * SQLite has no enum type, so Room needs to know how to store TaskStatus.
 * It is saved as its name ("TODO", "IN_PROGRESS", "DONE"), which is readable
 * in Database Inspector and in the SQL queries.
 */
class Converters {
    @TypeConverter
    fun statusToString(status: TaskStatus): String = status.name

    @TypeConverter
    fun stringToStatus(value: String): TaskStatus = TaskStatus.valueOf(value)
}
