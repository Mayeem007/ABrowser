package com.abrowser.modern.di

import android.content.Context
import androidx.room.Room
import com.abrowser.modern.data.database.ABrowserDatabase
import com.abrowser.modern.data.database.dao.BookmarkDao
import com.abrowser.modern.data.database.dao.HistoryDao
import com.abrowser.modern.data.database.dao.DownloadDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Database