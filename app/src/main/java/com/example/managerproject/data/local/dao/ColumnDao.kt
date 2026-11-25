package com.example.managerproject.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.managerproject.data.local.entity.ColumnEntity
import kotlinx.coroutines.flow.Flow

/** Database access for the columns of a board. */
@Dao
interface ColumnDao {

    /** CREATE one column. */
    @Insert
    suspend fun insert(column: ColumnEntity): Long

    /** CREATE several columns at once (the default To Do / In Progress / Done). */
    @Insert
    suspend fun insertAll(columns: List<ColumnEntity>)

    /** UPDATE: rename or reorder. */
    @Update
    suspend fun update(column: ColumnEntity)

    /** DELETE: its cards are removed by CASCADE. */
    @Delete
    suspend fun delete(column: ColumnEntity)

    /** READ: columns of a board in display order. */
    @Query("SELECT * FROM board_columns WHERE boardId = :boardId ORDER BY position")
    fun observeColumns(boardId: Long): Flow<List<ColumnEntity>>

    @Query("SELECT * FROM board_columns WHERE boardId = :boardId ORDER BY position")
    suspend fun getColumns(boardId: Long): List<ColumnEntity>

    @Query("SELECT * FROM board_columns WHERE id = :id")
    suspend fun getColumn(id: Long): ColumnEntity?

    /** Highest position on the board, so a new column goes to the end. */
    @Query("SELECT MAX(position) FROM board_columns WHERE boardId = :boardId")
    suspend fun maxPosition(boardId: Long): Int?
}
