package com.abrowser.modern.data.model

data class VideoInfo(
    val title: String,
    val url: String,
    val thumbnailUrl: String? = null,
    val duration: Long = 0,
    val quality: String = "720p",
    val format: String = "mp4",
    val fileSize: Long = 0,
    val downloadUrl: String = url
)

data class VideoQuality(
    val quality: String,
    val url: String,
    val fileSize: Long
)

enum class DownloadStatus {
    PENDING,
    DOWNLOADING,
    COMPLETED,
    FAILED,
    CANCELLED,
    PAUSED
}