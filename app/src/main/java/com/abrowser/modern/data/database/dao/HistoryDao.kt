package com.abrowser.modern.data.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.abrowser.modern.data.database.entities.HistoryEntity
import java.util.Date

@Dao
interface HistoryDao {
    
    @Query("SELECT * FROM history ORDER BY lastVisited DESC LIMIT :limit")
    fun getRecentHistory(limit: Int = 100): Flow<List<HistoryEntity>>
    
    @Query("SELECT * FROM history WHERE title LIKE '%' || :query || '%' OR url LIKE '%' || :query || '%' ORDER BY lastVisited DESC")
    fun searchHistory(query: String): Flow<List<HistoryEntity>>
    
    @Query("SELECT * FROM history WHERE url = :url LIMIT 1")
    suspend fun getHistoryByUrl(url: String): HistoryEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: HistoryEntity): Long
    
    @Update
    suspend fun updateHistory(history: HistoryEntity)
    
    @Query("DELETE FROM history WHERE lastVisited < :date")
    suspend fun deleteOldHistory(date: Date)
    
    @Query("DELETE FROM history")
    suspend fun clearAllHistory()
    
    @Query("SELECT COUNT(*) FROM history")
    suspend fun getHistoryCount(): Int
}