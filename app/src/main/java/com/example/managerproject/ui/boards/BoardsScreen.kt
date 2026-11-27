package com.example.managerproject.ui.boards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.managerproject.data.local.entity.BoardSummary
import com.example.managerproject.ui.AppViewModelProvider
import com.example.managerproject.ui.components.BoardColors

/**
 * Home screen: the list of boards.
 * This stateful version gets the ViewModel; the stateless [BoardsContent] below only
 * draws the state it is given, which also makes it easy to preview and test.
 */
@Composable
fun BoardsScreen(
    onOpenBoard: (Long) -> Unit,
    onOpenSettings: () -> Unit,
    viewModel: BoardsViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    // collectAsStateWithLifecycle stops collecting while the app is in the background
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BoardsContent(
        uiState = uiState,
        onQueryChange = viewModel::onQueryChange,
        onOpenBoard = onOpenBoard,
        onOpenSettings = onOpenSettings,
        onNewBoard = viewModel::showCreateDialog,
        onRename = viewModel::showRenameDialog,
        onDelete = viewModel::showDeleteDialog,
    )

    // Dialogs are part of the UI state, so they survive rotation
    when (val dialog = uiState.dialog) {
        BoardDialog.None -> Unit
        BoardDialog.Create -> BoardNameDialog(
            title = "New board",
            confirmLabel = "Create",
            initialName = "",
            showColorPicker = true,
            onConfirm = viewModel::createBoard,
            onDismiss = viewModel::dismissDialog,
        )
        is BoardDialog.Rename -> BoardNameDialog(
            title = "Rename board",
            confirmLabel = "Save",
            initialName = dialog.board.title,
            showColorPicker = false,
            onConfirm = { name, _ -> viewModel.renameBoard(dialog.board, name) },
            onDismiss = viewModel::dismissDialog,
        )
        is BoardDialog.Delete -> DeleteBoardDialog(
            board = dialog.board,
            onConfirm = { viewModel.deleteBoard(dialog.board) },
            onDismiss = viewModel::dismissDialog,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardsContent(
    uiState: BoardsUiState,
    onQueryChange: (String) -> Unit,
    onOpenBoard: (Long) -> Unit,
    onOpenSettings: () -> Unit,
    onNewBoard: () -> Unit,
    onRename: (BoardSummary) -> Unit,
    onDelete: (BoardSummary) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text("My Boards") },
                actions = {
                    IconButton(onClick = onOpenSettings) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNewBoard,
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = { Text("New board") },
                modifier = Modifier.testTag("new_board_fab"),
            )
        },
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding).fillMaxSize()) {
            // Search: filters boards by title with a LIKE query in Room
            OutlinedTextField(
                value = uiState.query,
                onValueChange = onQueryChange,
                placeholder = { Text("Search boards") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                trailingIcon = {
                    if (uiState.query.isNotEmpty()) {
                        IconButton(onClick = { onQueryChange("") }) {
                            Icon(Icons.Filled.Clear, contentDescription = "Clear search")
                        }
                    }
                },
                singleLine = true,
                shape = MaterialTheme.shapes.extraLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )

            when {
                uiState.isLoading -> Unit
                uiState.boards.isEmpty() -> EmptyBoards(isSearch = uiState.isSearchEmpty)
                else -> LazyVerticalGrid(
                    // One column on a phone in portrait, 2–3 columns when there is more width
                    columns = GridCells.Adaptive(minSize = 300.dp),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(uiState.boards, key = { it.id }) { board ->
                        BoardCard(
                            board = board,
                            onClick = { onOpenBoard(board.id) },
                            onRename = { onRename(board) },
                            onDelete = { onDelete(board) },
                        )
                    }
                }
            }
        }
    }
}

/** One board in the list: colour strip, title, card counts, progress and a menu. */
@Composable
private fun BoardCard(
    board: BoardSummary,
    onClick: () -> Unit,
    onRename: () -> Unit,
    onDelete: () -> Unit,
) {
    var menuOpen by remember { mutableStateOf(false) }
    ElevatedCard(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth().testTag("board_${board.title}"),
    ) {
        Row(Modifier.height(IntrinsicSize.Min)) {
            Box(
                Modifier
                    .width(8.dp)
                    .fillMaxHeight()
                    .background(BoardColors.parse(board.colorHex))
            )
            Column(
                Modifier
                    .weight(1f)
                    .padding(start = 16.dp, top = 12.dp, bottom = 16.dp)
            ) {
                Text(
                    board.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "${board.cardCount} cards · ${board.doneCount} done",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(12.dp))
                LinearProgressIndicator(
                    progress = { board.progress },
                    strokeCap = StrokeCap.Round,
                    drawStopIndicator = {},
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Box {
                IconButton(onClick = { menuOpen = true }) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "More options for ${board.title}")
                }
                DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                    DropdownMenuItem(
                        text = { Text("Rename") },
                        leadingIcon = { Icon(Icons.Filled.Edit, contentDescription = null) },
                        onClick = { menuOpen = false; onRename() },
                    )
                    DropdownMenuItem(
                        text = { Text("Delete") },
                        leadingIcon = { Icon(Icons.Filled.Delete, contentDescription = null) },
                        onClick = { menuOpen = false; onDelete() },
                    )
                }
            }
        }
    }
}

/** Shown when there are no boards, or when the search finds nothing. */
@Composable
private fun EmptyBoards(isSearch: Boolean) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            Icons.Outlined.Dashboard,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(64.dp),
        )
        Spacer(Modifier.height(16.dp))
        Text(
            if (isSearch) "No boards match your search" else "No boards yet",
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            if (isSearch) "Try a different word." else "Tap \"New board\" to create your first one.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}
