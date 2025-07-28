package com.abrowser.modern.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "downloads")
data class DownloadEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fileName: String,
    val url: String,
    val filePath: String,
    val fileSize: Long,
    val downloadedSize: Long = 0,
    val mimeType: String,
    val status: DownloadStatus,
    val progress: Float = 0f,
    val createdAt: Date = Date(),
    val completedAt: Date? = null
)

enum class DownloadStatus {
    PENDING,
    DOWNLOADING,
    COMPLETED,
    FAILED,
    CANCELLED,
    PAUSED
}