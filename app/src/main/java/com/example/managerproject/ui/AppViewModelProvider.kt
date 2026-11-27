package com.example.managerproject.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.managerproject.ManagerApplication
import com.example.managerproject.ui.boards.BoardsViewModel

/**
 * Factory that creates every ViewModel of the app with its dependencies
 * (taken from the AppContainer in ManagerApplication).
 *
 * Usage in a screen: viewModel(factory = AppViewModelProvider.Factory)
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            BoardsViewModel(managerApplication().container.taskRepository)
        }
    }
}

/** Gets the ManagerApplication from the ViewModel creation extras. */
fun CreationExtras.managerApplication(): ManagerApplication =
    this[APPLICATION_KEY] as ManagerApplication
