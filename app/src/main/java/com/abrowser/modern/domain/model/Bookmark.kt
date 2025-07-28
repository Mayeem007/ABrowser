package com.abrowser.modern.domain.model

import java.util.Date

data class Bookmark(
    val id: Long = 0,
    val title: String,
    val url: String,
    val favicon: String? = null,
    val folderId: Long? = null,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)