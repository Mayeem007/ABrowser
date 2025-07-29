package com.abrowser.modern.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.abrowser.modern.data.database.entities.DownloadStatus

@Entity(tableName = "downloads")
data class DownloadItem(
    @PrimaryKey val id: String,
    val fileName: String,
    val url: String,
    val filePath: String,
    val fileSize: Long,
    val downloadedSize: Long = 0,
    val status: DownloadStatus,
    val mimeType: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null
)

enum class DownloadStatus {
    PENDING,
    DOWNLOADING,
    COMPLETED,
    FAILED,
    CANCELLED,
    PAUSED
}