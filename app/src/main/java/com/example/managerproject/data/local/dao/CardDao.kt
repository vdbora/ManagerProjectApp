package com.example.managerproject.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.managerproject.data.local.entity.CardEntity
import kotlinx.coroutines.flow.Flow

/** Database access for task cards. */
@Dao
interface CardDao {

    /** CREATE: returns the id of the new card. Also used to restore a deleted card (Undo). */
    @Insert
    suspend fun insert(card: CardEntity): Long

    /** UPDATE: save edits from the Add/Edit form (title, priority, photo path, ...). */
    @Update
    suspend fun update(card: CardEntity)

    /** DELETE one card. */
    @Delete
    suspend fun delete(card: CardEntity)

    /** READ: one card for the Card Detail and Edit screens. */
    @Query("SELECT * FROM cards WHERE id = :id")
    fun observeCard(id: Long): Flow<CardEntity?>

    @Query("SELECT * FROM cards WHERE id = :id")
    suspend fun getCard(id: Long): CardEntity?

    /** READ: all visible cards of a board, grouped later by column in the ViewModel. */
    @Query(
        """
        SELECT c.* FROM cards c
        INNER JOIN board_columns col ON col.id = c.columnId
        WHERE col.boardId = :boardId AND c.isArchived = 0
        ORDER BY c.position, c.createdAt
        """
    )
    fun observeCardsForBoard(boardId: Long): Flow<List<CardEntity>>

    /** UPDATE: move a card to another column ("Move to…" and "Mark done"). */
    @Query("UPDATE cards SET columnId = :columnId, position = :position, updatedAt = :now WHERE id = :cardId")
    suspend fun moveToColumn(cardId: Long, columnId: Long, position: Int, now: Long = System.currentTimeMillis())

    /** Highest position in a column, so a new or moved card goes to the bottom. */
    @Query("SELECT MAX(position) FROM cards WHERE columnId = :columnId")
    suspend fun maxPosition(columnId: Long): Int?

    /**
     * READ for the reminder worker: cards due between [from] and [to]
     * that are not archived and not already done.
     */
    @Query(
        """
        SELECT c.* FROM cards c
        INNER JOIN board_columns col ON col.id = c.columnId
        WHERE c.dueDate BETWEEN :from AND :to
          AND c.isArchived = 0
          AND col.status != 'DONE'
        ORDER BY c.dueDate
        """
    )
    suspend fun getCardsDueBetween(from: Long, to: Long): List<CardEntity>

    /** READ for the cleanup worker: archived cards last changed before [time]. */
    @Query("SELECT * FROM cards WHERE isArchived = 1 AND updatedAt < :time")
    suspend fun getArchivedBefore(time: Long): List<CardEntity>

    /** DELETE for the cleanup worker. Returns how many rows were removed. */
    @Query("DELETE FROM cards WHERE isArchived = 1 AND updatedAt < :time")
    suspend fun deleteArchivedBefore(time: Long): Int
}
