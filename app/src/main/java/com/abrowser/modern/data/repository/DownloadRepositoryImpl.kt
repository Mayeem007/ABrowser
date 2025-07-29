package com.abrowser.modern.data.repository

import kotlinx.coroutines.flow.Flow
import com.abrowser.modern.data.database.dao.DownloadDao
import com.abrowser.modern.data.database.entities.DownloadEntity
import com.abrowser.modern.data.database.entities.DownloadStatus
import com.abrowser.modern.domain.repository.DownloadRepository
import java.util.Date
import javax.inject.Inject

class DownloadRepositoryImpl @Inject constructor(
    private val downloadDao: DownloadDao
) : DownloadRepository {

    override fun getAllDownloads(): Flow<List<DownloadEntity>> {
        return downloadDao.getAllDownloads()
    }

    override fun getDownloadsByStatus(status: DownloadStatus): Flow<List<DownloadEntity>> {
        return downloadDao.getDownloadsByStatus(status)
    }

    override suspend fun getDownloadById(id: Long): DownloadEntity? {
        return downloadDao.getDownloadById(id)
    }

    override suspend fun insertDownload(download: DownloadEntity): Long {
        return downloadDao.insertDownload(download)
    }

    override suspend fun updateDownload(download: DownloadEntity) {
        downloadDao.updateDownload(download)
    }

    override suspend fun deleteDownload(download: DownloadEntity) {
        downloadDao.deleteDownload(download)
    }

    override suspend fun updateDownloadProgress(id: Long, status: DownloadStatus, progress: Float, downloadedSize: Long) {
        downloadDao.updateDownloadProgress(id, status, progress, downloadedSize)
    }

    override suspend fun startDownload(url: String, fileName: String, mimeType: String): Long {
        val download = DownloadEntity(
            fileName = fileName,
            url = url,
            filePath = "/storage/emulated/0/Download/$fileName",
            fileSize = 0L,
            downloadedSize = 0L,
            mimeType = mimeType,
            status = DownloadStatus.PENDING,
            progress = 0f,
            createdAt = Date(),
            completedAt = null
        )
        return insertDownload(download)
    }

    override suspend fun pauseDownload(id: Long) {
        val download = getDownloadById(id)
        download?.let {
            updateDownload(it.copy(status = DownloadStatus.PAUSED))
        }
    }

    override suspend fun resumeDownload(id: Long) {
        val download = getDownloadById(id)
        download?.let {
            updateDownload(it.copy(status = DownloadStatus.DOWNLOADING))
        }
    }

    override suspend fun cancelDownload(id: Long) {
        val download = getDownloadById(id)
        download?.let {
            updateDownload(it.copy(status = DownloadStatus.CANCELLED))
        }
    }
}