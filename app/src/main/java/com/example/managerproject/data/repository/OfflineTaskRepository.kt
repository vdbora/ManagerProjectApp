package com.example.managerproject.data.repository

import androidx.room.withTransaction
import com.example.managerproject.data.local.AppDatabase
import com.example.managerproject.data.local.entity.BoardEntity
import com.example.managerproject.data.local.entity.BoardSummary
import com.example.managerproject.data.local.entity.CardEntity
import com.example.managerproject.data.local.entity.ColumnEntity
import com.example.managerproject.data.model.TaskStatus
import kotlinx.coroutines.flow.Flow
import java.io.File

/**
 * TaskRepository backed by the local Room database (works fully offline).
 */
class OfflineTaskRepository(private val db: AppDatabase) : TaskRepository {

    private val boardDao = db.boardDao()
    private val columnDao = db.columnDao()
    private val cardDao = db.cardDao()

    // ---------- Boards ----------

    override fun observeBoards(query: String): Flow<List<BoardSummary>> =
        boardDao.observeBoardSummaries(query.trim())

    override fun observeBoard(boardId: Long): Flow<BoardEntity?> = boardDao.observeBoard(boardId)

    override suspend fun createBoard(title: String, colorHex: String): Long =
        // Transaction: either the board and all three columns are saved, or nothing is.
        db.withTransaction {
            val boardId = boardDao.insert(BoardEntity(title = title.trim(), colorHex = colorHex))
            columnDao.insertAll(defaultColumns(boardId))
            boardId
        }

    override suspend fun renameBoard(board: BoardEntity, newTitle: String) =
        boardDao.update(board.copy(title = newTitle.trim()))

    override suspend fun deleteBoard(board: BoardEntity) {
        // Collect photo paths first, because CASCADE removes the card rows with the board.
        val photos = columnDao.getColumns(board.id).flatMap { column ->
            cardDao.getCardsInColumn(column.id).mapNotNull { it.photoPath }
        }
        boardDao.delete(board)
        photos.forEach(::deletePhotoFile)
    }

    // ---------- Columns ----------

    override fun observeColumns(boardId: Long): Flow<List<ColumnEntity>> = columnDao.observeColumns(boardId)

    override suspend fun getColumns(boardId: Long): List<ColumnEntity> = columnDao.getColumns(boardId)

    override suspend fun addColumn(boardId: Long, title: String, status: TaskStatus): Long {
        val position = (columnDao.maxPosition(boardId) ?: -1) + 1
        return columnDao.insert(ColumnEntity(boardId = boardId, title = title.trim(), status = status, position = position))
    }

    override suspend fun deleteColumn(column: ColumnEntity) {
        val photos = cardDao.getCardsInColumn(column.id).mapNotNull { it.photoPath }
        columnDao.delete(column)
        photos.forEach(::deletePhotoFile)
    }

    // ---------- Cards ----------

    override fun observeCardsForBoard(boardId: Long): Flow<List<CardEntity>> = cardDao.observeCardsForBoard(boardId)

    override fun observeCard(cardId: Long): Flow<CardEntity?> = cardDao.observeCard(cardId)

    override suspend fun getCard(cardId: Long): CardEntity? = cardDao.getCard(cardId)

    override suspend fun addCard(card: CardEntity): Long {
        val position = (cardDao.maxPosition(card.columnId) ?: -1) + 1
        val now = System.currentTimeMillis()
        return cardDao.insert(card.copy(id = 0, position = position, createdAt = now, updatedAt = now))
    }

    override suspend fun updateCard(card: CardEntity) =
        cardDao.update(card.copy(updatedAt = System.currentTimeMillis()))

    // The photo file is kept on delete so that Undo can bring the card back with its photo.
    override suspend fun deleteCard(card: CardEntity) = cardDao.delete(card)

    override suspend fun restoreCard(card: CardEntity) {
        cardDao.insert(card)
    }

    override suspend fun moveCard(cardId: Long, toColumnId: Long) {
        val position = (cardDao.maxPosition(toColumnId) ?: -1) + 1
        cardDao.moveToColumn(cardId, toColumnId, position)
    }

    override suspend fun markDone(cardId: Long): Boolean {
        val card = cardDao.getCard(cardId) ?: return false
        val boardId = columnDao.getColumn(card.columnId)?.boardId ?: return false
        val doneColumn = columnDao.getColumns(boardId).firstOrNull { it.status == TaskStatus.DONE } ?: return false
        moveCard(cardId, doneColumn.id)
        return true
    }

    // ---------- Background work ----------

    override suspend fun getCardsDueBetween(from: Long, to: Long): List<CardEntity> =
        cardDao.getCardsDueBetween(from, to)

    override suspend fun deleteArchivedBefore(time: Long): Int {
        cardDao.getArchivedBefore(time).mapNotNull { it.photoPath }.forEach(::deletePhotoFile)
        return cardDao.deleteArchivedBefore(time)
    }

    // ---------- Helpers ----------

    private fun deletePhotoFile(path: String) {
        runCatching { File(path).delete() }
    }

    companion object {
        /** The three columns every new board starts with. */
        fun defaultColumns(boardId: Long) = listOf(
            ColumnEntity(boardId = boardId, title = "To Do", status = TaskStatus.TODO, position = 0),
            ColumnEntity(boardId = boardId, title = "In Progress", status = TaskStatus.IN_PROGRESS, position = 1),
            ColumnEntity(boardId = boardId, title = "Done", status = TaskStatus.DONE, position = 2),
        )
    }
}
