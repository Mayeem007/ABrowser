package com.abrowser.modern.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.abrowser.modern.data.database.dao.BookmarkDao
import com.abrowser.modern.data.database.dao.HistoryDao
import com.abrowser.modern.data.database.dao.DownloadDao
import com.abrowser.modern.data.database.entities.BookmarkEntity
import com.abrowser.modern.data.database.entities.HistoryEntity
import com.abrowser.modern.data.database.entities.DownloadEntity
import com.abrowser.modern.data.database.converters.DateConverter

@Database(
    entities = [
        BookmarkEntity::class,
        HistoryEntity::class,
        DownloadEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class ABrowserDatabase : RoomDatabase() {
    
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun historyDao(): HistoryDao
    abstract fun downloadDao(): DownloadDao
    
    companion object {
        const val DATABASE_NAME = "abrowser_database"
        
        fun create(context: Context): ABrowserDatabase {
            return Room.databaseBuilder(
                context,
                ABrowserDatabase::class.java,
                DATABASE_NAME
            ).build()
        }
    }
}