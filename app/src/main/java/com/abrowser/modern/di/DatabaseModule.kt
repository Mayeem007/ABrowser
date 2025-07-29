package com.abrowser.modern.di

import android.content.Context
import androidx.room.Room
import com.abrowser.modern.data.database.ABrowserDatabase
import com.abrowser.modern.data.database.dao.BookmarkDao
import com.abrowser.modern.data.database.dao.HistoryDao
import com.abrowser.modern.data.database.dao.DownloadDao
import com.abrowser.modern.domain.repository.BookmarkRepository
import com.abrowser.modern.domain.repository.DownloadRepository
import com.abrowser.modern.data.repository.BookmarkRepositoryImpl
import com.abrowser.modern.data.repository.DownloadRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideABrowserDatabase(@ApplicationContext context: Context): ABrowserDatabase {
        return Room.databaseBuilder(
            context,
            ABrowserDatabase::class.java,
            "abrowser_database"
        ).build()
    }

    @Provides
    fun provideBookmarkDao(database: ABrowserDatabase): BookmarkDao {
        return database.bookmarkDao()
    }

    @Provides
    fun provideHistoryDao(database: ABrowserDatabase): HistoryDao {
        return database.historyDao()
    }

    @Provides
    fun provideDownloadDao(database: ABrowserDatabase): DownloadDao {
        return database.downloadDao()
    }

    @Provides
    @Singleton
    fun provideBookmarkRepository(bookmarkDao: BookmarkDao): BookmarkRepository {
        return BookmarkRepositoryImpl(bookmarkDao)
    }

    @Provides
    @Singleton
    fun provideDownloadRepository(downloadDao: DownloadDao): DownloadRepository {
        return DownloadRepositoryImpl(downloadDao)
    }
}