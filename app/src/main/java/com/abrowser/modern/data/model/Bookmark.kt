package com.abrowser.modern.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class Bookmark(
    @PrimaryKey val id: String,
    val title: String,
    val url: String,
    val favicon: String? = null,
    val folderId: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)