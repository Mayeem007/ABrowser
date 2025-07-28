package com.abrowser.modern.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryItem(
    @PrimaryKey val id: String,
    val title: String,
    val url: String,
    val favicon: String? = null,
    val visitCount: Int = 1,
    val lastVisited: Long = System.currentTimeMillis()
)