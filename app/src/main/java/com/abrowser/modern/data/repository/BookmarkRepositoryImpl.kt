package com.abrowser.modern.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.abrowser.modern.data.database.dao.BookmarkDao
import com.abrowser.modern.data.database.entities.BookmarkEntity
import com.abrowser.modern.domain.model.Bookmark
import com.abrowser.modern.domain.repository.BookmarkRepository
import javax.inject.Inject

class BookmarkRepositoryImpl @Inject constructor(
    private val bookmarkDao: BookmarkDao
) : BookmarkRepository {

    override fun getAllBookmarks(): Flow<List<Bookmark>> {
        return bookmarkDao.getAllBookmarks().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getBookmarksByFolder(folderId: Long): Flow<List<Bookmark>> {
        return bookmarkDao.getBookmarksByFolder(folderId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun getBookmarkByUrl(url: String): Bookmark? {
        return bookmarkDao.getBookmarkByUrl(url)?.toDomainModel()
    }

    override suspend fun insertBookmark(bookmark: Bookmark): Long {
        return bookmarkDao.insertBookmark(bookmark.toEntity())
    }

    override suspend fun updateBookmark(bookmark: Bookmark) {
        bookmarkDao.updateBookmark(bookmark.toEntity())
    }

    override suspend fun deleteBookmark(bookmark: Bookmark) {
        bookmarkDao.deleteBookmark(bookmark.toEntity())
    }

    override suspend fun deleteBookmarkById(id: Long) {
        bookmarkDao.deleteBookmarkById(id)
    }

    override suspend fun isBookmarked(url: String): Boolean {
        return bookmarkDao.getBookmarkByUrl(url) != null
    }

    private fun BookmarkEntity.toDomainModel(): Bookmark {
        return Bookmark(
            id = this.id.toString(),
            title = this.title,
            url = this.url,
            favicon = this.favicon,
            folderId = this.folderId?.toString(),
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
    }

    private fun Bookmark.toEntity(): BookmarkEntity {
        return BookmarkEntity(
            id = this.id.toLongOrNull() ?: 0L,
            title = this.title,
            url = this.url,
            favicon = this.favicon,
            folderId = this.folderId?.toLongOrNull(),
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
    }
}