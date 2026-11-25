package com.example.managerproject

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Application class: created once per process, before any activity.
 * It owns the AppContainer, so the database and repository live as long as the app.
 * Registered in AndroidManifest.xml with android:name=".ManagerApplication".
 */
class ManagerApplication : Application() {

    lateinit var container: AppContainer
        private set

    /** Scope for app-wide background work that should not be tied to one screen. */
    val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)

        // Open the database early on a background thread, so it is created (with the
        // sample board on first launch) before the first screen asks for data.
        applicationScope.launch(Dispatchers.IO) {
            container.taskRepository.observeBoards().first()
        }
    }
}
