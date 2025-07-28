package com.abrowser.modern.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val url: String,
    val favicon: String? = null,
    val visitCount: Int = 1,
    val lastVisited: Date = Date(),
    val createdAt: Date = Date()
)