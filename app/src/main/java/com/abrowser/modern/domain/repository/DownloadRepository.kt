package com.abrowser.modern.domain.repository

import kotlinx.coroutines.flow.Flow
import com.abrowser.modern.data.database.entities.DownloadEntity
import com.abrowser.modern.data.database.entities.DownloadStatus

interface DownloadRepository {
    fun getAllDownloads(): Flow<List<DownloadEntity>>
    fun getDownloadsByStatus(status: DownloadStatus): Flow<List<DownloadEntity>>
    suspend fun getDownloadById(id: Long): DownloadEntity?
    suspend fun insertDownload(download: DownloadEntity): Long
    suspend fun updateDownload(download: DownloadEntity)
    suspend fun deleteDownload(download: DownloadEntity)
    suspend fun updateDownloadProgress(id: Long, status: DownloadStatus, progress: Float, downloadedSize: Long)
    suspend fun startDownload(url: String, fileName: String, mimeType: String): Long
    suspend fun pauseDownload(id: Long)
    suspend fun resumeDownload(id: Long)
    suspend fun cancelDownload(id: Long)
}