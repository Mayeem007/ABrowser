package com.abrowser.modern.domain.repository

import kotlinx.coroutines.flow.Flow
import com.abrowser.modern.domain.model.Bookmark

interface BookmarkRepository {
    fun getAllBookmarks(): Flow<List<Bookmark>>
    fun getBookmarksByFolder(folderId: Long): Flow<List<Bookmark>>
    suspend fun getBookmarkByUrl(url: String): Bookmark?
    suspend fun insertBookmark(bookmark: Bookmark): Long
    suspend fun updateBookmark(bookmark: Bookmark)
    suspend fun deleteBookmark(bookmark: Bookmark)
    suspend fun deleteBookmarkById(id: Long)
    suspend fun isBookmarked(url: String): Boolean
}