package com.example.managerproject

import android.content.Context
import com.example.managerproject.data.local.AppDatabase
import com.example.managerproject.data.repository.OfflineTaskRepository
import com.example.managerproject.data.repository.TaskRepository

/**
 * Simple manual dependency injection: one place that creates the app's shared objects.
 * ViewModels get their dependencies from here (see the ViewModel factory in a later commit),
 * and tests can provide a different container with fake objects.
 */
interface AppContainer {
    val taskRepository: TaskRepository
}

/** Real container used by the running app. */
class DefaultAppContainer(context: Context) : AppContainer {

    private val database: AppDatabase by lazy { AppDatabase.getDatabase(context) }

    // lazy: the database is only opened the first time the repository is used
    override val taskRepository: TaskRepository by lazy { OfflineTaskRepository(database) }
}
