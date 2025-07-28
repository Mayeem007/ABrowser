package com.abrowser.modern.data.database

import androidx.room.TypeConverter
import com.abrowser.modern.data.model.DownloadStatus

class Converters {
    
    @TypeConverter
    fun fromDownloadStatus(status: DownloadStatus): String {
        return status.name
    }
    
    @TypeConverter
    fun toDownloadStatus(status: String): DownloadStatus {
        return DownloadStatus.valueOf(status)
    }
}