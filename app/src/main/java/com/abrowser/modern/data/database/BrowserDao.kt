package com.abrowser.modern.data.database

import androidx.room.*
import com.abrowser.modern.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BrowserDao {
    
    // Tabs
    @Query("SELECT * FROM browser_tabs ORDER BY updatedAt DESC")
    fun getAllTabs(): Flow<List<BrowserTab>>
    
    @Query("SELECT * FROM browser_tabs WHERE isActive = 1 LIMIT 1")
    suspend fun getActiveTab(): BrowserTab?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTab(tab: BrowserTab)
    
    @Update
    suspend fun updateTab(tab: BrowserTab)
    
    @Delete
    suspend fun deleteTab(tab: BrowserTab)
    
    @Query("UPDATE browser_tabs SET isActive = 0")
    suspend fun deactivateAllTabs()
    
    // Bookmarks
    @Query("SELECT * FROM bookmarks ORDER BY createdAt DESC")
    fun getAllBookmarks(): Flow<List<Bookmark>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: Bookmark)
    
    @Delete
    suspend fun deleteBookmark(bookmark: Bookmark)
    
    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE url = :url)")
    suspend fun isBookmarked(url: String): Boolean
    
    // History
    @Query("SELECT * FROM history ORDER BY lastVisited DESC LIMIT 100")
    fun getRecentHistory(): Flow<List<HistoryItem>>
    
    @Query("SELECT * FROM history WHERE title LIKE '%' || :query || '%' OR url LIKE '%' || :query || '%' ORDER BY lastVisited DESC")
    fun searchHistory(query: String): Flow<List<HistoryItem>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistoryItem(item: HistoryItem)
    
    @Query("DELETE FROM history")
    suspend fun clearHistory()
    
    // Downloads
    @Query("SELECT * FROM downloads ORDER BY createdAt DESC")
    fun getAllDownloads(): Flow<List<DownloadItem>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDownload(download: DownloadItem)
    
    @Update
    suspend fun updateDownload(download: DownloadItem)
    
    @Delete
    suspend fun deleteDownload(download: DownloadItem)
}