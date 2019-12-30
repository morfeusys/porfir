package com.porfir.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.idanatz.oneadapter.external.interfaces.Diffable

@Entity(tableName = "history_items")
data class HistoryItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "generated_text")
    val text: String,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long
): Diffable {
    override fun areContentTheSame(other: Any) = other is HistoryItem && other.id == id

    override fun getUniqueIdentifier(): Long = id
}