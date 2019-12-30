package com.porfir.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.porfir.model.HistoryItem

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history_items ORDER BY timestamp DESC")
    fun getAll(): List<HistoryItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: HistoryItem)
}