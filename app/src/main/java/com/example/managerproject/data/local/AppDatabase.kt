package com.example.managerproject.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.managerproject.data.local.dao.BoardDao
import com.example.managerproject.data.local.dao.CardDao
import com.example.managerproject.data.local.dao.ColumnDao
import com.example.managerproject.data.local.entity.BoardEntity
import com.example.managerproject.data.local.entity.CardEntity
import com.example.managerproject.data.local.entity.ColumnEntity

/**
 * The Room database of the app (file: manager_project.db).
 * Room generates the implementation (AppDatabase_Impl) with KSP at compile time.
 *
 * exportSchema = true writes the schema to app/schemas/ so it can be reviewed in Git.
 */
@Database(
    entities = [BoardEntity::class, ColumnEntity::class, CardEntity::class],
    version = 1,
    exportSchema = true,
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun boardDao(): BoardDao
    abstract fun columnDao(): ColumnDao
    abstract fun cardDao(): CardDao

    companion object {
        private const val DATABASE_NAME = "manager_project.db"

        // @Volatile makes the value visible to all threads straight away
        @Volatile
        private var instance: AppDatabase? = null

        /**
         * Returns the single database instance for the whole app.
         * Opening a database is expensive, so it is created only once (singleton).
         */
        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME,
                ).build().also { instance = it }
            }
    }
}
