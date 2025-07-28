package com.abrowser.modern.domain.model

data class VideoInfo(
    val title: String,
    val url: String,
    val thumbnailUrl: String? = null,
    val duration: Long = 0,
    val quality: String = "720p",
    val size: String = "Unknown",
    val format: String = "mp4"
)