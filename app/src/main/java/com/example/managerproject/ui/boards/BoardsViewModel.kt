package com.example.managerproject.ui.boards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.managerproject.data.local.entity.BoardEntity
import com.example.managerproject.data.local.entity.BoardSummary
import com.example.managerproject.data.repository.TaskRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** Which dialog is open on the Boards screen. */
sealed interface BoardDialog {
    data object None : BoardDialog
    data object Create : BoardDialog
    data class Rename(val board: BoardSummary) : BoardDialog
    data class Delete(val board: BoardSummary) : BoardDialog
}

/** Everything the Boards screen needs to draw itself. */
data class BoardsUiState(
    val boards: List<BoardSummary> = emptyList(),
    val query: String = "",
    val dialog: BoardDialog = BoardDialog.None,
    val isLoading: Boolean = true,
)

/**
 * State holder for the Boards (home) screen.
 *
 * Unidirectional data flow: the screen calls the event functions below,
 * the ViewModel updates the database or its own state, and the new BoardsUiState
 * flows back down to the screen through [uiState].
 */
@OptIn(ExperimentalCoroutinesApi::class)
class BoardsViewModel(private val repository: TaskRepository) : ViewModel() {

    private val query = MutableStateFlow("")
    private val dialog = MutableStateFlow<BoardDialog>(BoardDialog.None)

    // When the search text changes, switch to a new database query (flatMapLatest)
    private val boards = query.flatMapLatest { repository.observeBoards(it) }

    val uiState: StateFlow<BoardsUiState> =
        combine(boards, query, dialog) { boards, query, dialog ->
            BoardsUiState(boards = boards, query = query, dialog = dialog, isLoading = false)
        }.stateIn(
            scope = viewModelScope,
            // Keep the query alive for 5 s after the screen stops, so a rotation does not restart it
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = BoardsUiState(),
        )

    // ---------- Events from the UI ----------

    fun onQueryChange(text: String) {
        query.value = text
    }

    fun showCreateDialog() { dialog.value = BoardDialog.Create }
    fun showRenameDialog(board: BoardSummary) { dialog.value = BoardDialog.Rename(board) }
    fun showDeleteDialog(board: BoardSummary) { dialog.value = BoardDialog.Delete(board) }
    fun dismissDialog() { dialog.value = BoardDialog.None }

    /** CREATE. The dialog already checked that the title is not blank. */
    fun createBoard(title: String, colorHex: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            repository.createBoard(title, colorHex)
            dismissDialog()
        }
    }

    /** UPDATE: rename. */
    fun renameBoard(board: BoardSummary, newTitle: String) {
        if (newTitle.isBlank()) return
        viewModelScope.launch {
            repository.renameBoard(board.toEntity(), newTitle)
            dismissDialog()
        }
    }

    /** DELETE: also removes the board's columns and cards (CASCADE). */
    fun deleteBoard(board: BoardSummary) {
        viewModelScope.launch {
            repository.deleteBoard(board.toEntity())
            dismissDialog()
        }
    }

    private fun BoardSummary.toEntity() =
        BoardEntity(id = id, title = title, colorHex = colorHex, createdAt = createdAt)
}

/** Helper used by the UI and the tests: is the board list empty because of the search? */
val BoardsUiState.isSearchEmpty: Boolean get() = boards.isEmpty() && query.isNotBlank()

/** Progress 0..1 for the bar on a board card. */
val BoardSummary.progress: Float get() = if (cardCount == 0) 0f else doneCount.toFloat() / cardCount
