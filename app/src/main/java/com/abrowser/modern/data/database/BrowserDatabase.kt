package com.abrowser.modern.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.abrowser.modern.data.model.*

@Database(
    entities = [
        BrowserTab::class,
        Bookmark::class,
        HistoryItem::class,
        DownloadItem::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class BrowserDatabase : RoomDatabase() {
    
    abstract fun browserDao(): BrowserDao
    
    companion object {
        @Volatile
        private var INSTANCE: BrowserDatabase? = null
        
        fun getDatabase(context: Context): BrowserDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BrowserDatabase::class.java,
                    "browser_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}