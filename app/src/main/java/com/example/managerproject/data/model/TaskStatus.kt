package com.example.managerproject.data.model

/**
 * The three standard stages of a task. Every board starts with one column per status.
 * The status decides the colour of the column and its cards (red / purple / green).
 */
enum class TaskStatus {
    TODO,
    IN_PROGRESS,
    DONE,
}
