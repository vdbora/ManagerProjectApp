package com.example.managerproject.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.managerproject.data.local.entity.BoardEntity
import com.example.managerproject.data.local.entity.BoardSummary
import kotlinx.coroutines.flow.Flow

/**
 * Database access for boards.
 * Reads return Flow, so the UI updates by itself whenever the table changes.
 * Writes are suspend functions and run off the main thread.
 */
@Dao
interface BoardDao {

    /** CREATE: returns the id of the new board. */
    @Insert
    suspend fun insert(board: BoardEntity): Long

    /** UPDATE: e.g. rename. */
    @Update
    suspend fun update(board: BoardEntity)

    /** DELETE: its columns and cards are removed by the CASCADE foreign keys. */
    @Delete
    suspend fun delete(board: BoardEntity)

    /**
     * READ: boards whose title contains [query] (empty = all), newest first,
     * with the number of cards and how many are done.
     */
    @Query(
        """
        SELECT b.id, b.title, b.colorHex, b.createdAt,
               COUNT(c.id) AS cardCount,
               SUM(CASE WHEN col.status = 'DONE' AND c.id IS NOT NULL THEN 1 ELSE 0 END) AS doneCount
        FROM boards b
        LEFT JOIN board_columns col ON col.boardId = b.id
        LEFT JOIN cards c ON c.columnId = col.id AND c.isArchived = 0
        WHERE b.title LIKE '%' || :query || '%'
        GROUP BY b.id
        ORDER BY b.createdAt DESC
        """
    )
    fun observeBoardSummaries(query: String = ""): Flow<List<BoardSummary>>

    /** READ: one board, used for the Board screen title. */
    @Query("SELECT * FROM boards WHERE id = :id")
    fun observeBoard(id: Long): Flow<BoardEntity?>

    @Query("SELECT * FROM boards WHERE id = :id")
    suspend fun getBoard(id: Long): BoardEntity?

    /** Used on first launch to check whether sample data must be created. */
    @Query("SELECT COUNT(*) FROM boards")
    suspend fun count(): Int
}
