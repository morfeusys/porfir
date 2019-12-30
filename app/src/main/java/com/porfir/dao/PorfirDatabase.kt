package com.porfir.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.porfir.model.HistoryItem

@Database(entities = [HistoryItem::class], version = 1, exportSchema = false)
abstract class PorfirDatabase: RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}