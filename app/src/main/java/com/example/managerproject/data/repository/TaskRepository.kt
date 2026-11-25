package com.example.managerproject.data.repository

import com.example.managerproject.data.local.entity.BoardEntity
import com.example.managerproject.data.local.entity.BoardSummary
import com.example.managerproject.data.local.entity.CardEntity
import com.example.managerproject.data.local.entity.ColumnEntity
import com.example.managerproject.data.model.TaskStatus
import kotlinx.coroutines.flow.Flow

/**
 * Single source of truth for boards, columns and cards.
 *
 * ViewModels talk only to this interface, never to Room directly. That keeps the UI
 * independent from the database and lets unit tests use a fake implementation.
 */
interface TaskRepository {

    // ---------- Boards ----------
    fun observeBoards(query: String = ""): Flow<List<BoardSummary>>
    fun observeBoard(boardId: Long): Flow<BoardEntity?>

    /** Creates a board together with the default To Do / In Progress / Done columns. */
    suspend fun createBoard(title: String, colorHex: String): Long
    suspend fun renameBoard(board: BoardEntity, newTitle: String)
    suspend fun deleteBoard(board: BoardEntity)

    // ---------- Columns ----------
    fun observeColumns(boardId: Long): Flow<List<ColumnEntity>>
    suspend fun getColumns(boardId: Long): List<ColumnEntity>
    suspend fun addColumn(boardId: Long, title: String, status: TaskStatus): Long
    suspend fun deleteColumn(column: ColumnEntity)

    // ---------- Cards ----------
    fun observeCardsForBoard(boardId: Long): Flow<List<CardEntity>>
    fun observeCard(cardId: Long): Flow<CardEntity?>
    suspend fun getCard(cardId: Long): CardEntity?

    /** Adds a card at the bottom of its column and returns its id. */
    suspend fun addCard(card: CardEntity): Long
    suspend fun updateCard(card: CardEntity)
    suspend fun deleteCard(card: CardEntity)

    /** Puts a deleted card back with the same id (used by Undo). */
    suspend fun restoreCard(card: CardEntity)

    /** Moves a card to the bottom of another column. */
    suspend fun moveCard(cardId: Long, toColumnId: Long)

    /** Moves a card to the DONE column of its board. Returns false if the board has none. */
    suspend fun markDone(cardId: Long): Boolean

    // ---------- Background work ----------
    suspend fun getCardsDueBetween(from: Long, to: Long): List<CardEntity>

    /** Deletes archived cards older than [time] and their photo files. Returns how many. */
    suspend fun deleteArchivedBefore(time: Long): Int
}
