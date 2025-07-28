package com.abrowser.modern.ui.viewmodel

import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.abrowser.modern.domain.model.BrowserTab
import com.abrowser.modern.data.model.VideoInfo  // Change from domain.model to data.model
import com.abrowser.modern.domain.repository.BookmarkRepository
import com.abrowser.modern.domain.repository.DownloadRepository
import java.util.UUID
import javax.inject.Inject

data class BrowserUiState(
    val tabs: List<BrowserTab> = listOf(
        BrowserTab(
            id = UUID.randomUUID().toString(),
            title = "New Tab",
            url = "",
            isActive = true
        )
    ),
    val activeTabId: String = "",
    val isCurrentUrlBookmarked: Boolean = false,
    val isLoading: Boolean = false
)

@HiltViewModel
class BrowserViewModel @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
    private val downloadRepository: DownloadRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BrowserUiState())
    val uiState: StateFlow<BrowserUiState> = _uiState.asStateFlow()

    init {
        _uiState.value = _uiState.value.copy(
            activeTabId = _uiState.value.tabs.first().id
        )
    }

    fun createNewTab() {
        val newTab = BrowserTab(
            id = UUID.randomUUID().toString(),
            title = "New Tab",
            url = "",
            isActive = true
        )
        
        val updatedTabs = _uiState.value.tabs.map { it.copy(isActive = false) } + newTab
        _uiState.value = _uiState.value.copy(
            tabs = updatedTabs,
            activeTabId = newTab.id
        )
    }

    fun switchTab(tabId: String) {
        val updatedTabs = _uiState.value.tabs.map { tab ->
            tab.copy(isActive = tab.id == tabId)
        }
        _uiState.value = _uiState.value.copy(
            tabs = updatedTabs,
            activeTabId = tabId
        )
        
        // Check bookmark status for the new active tab
        checkBookmarkStatus()
    }

    fun closeTab(tabId: String) {
        val currentTabs = _uiState.value.tabs
        if (currentTabs.size <= 1) return // Don't close the last tab
        
        val updatedTabs = currentTabs.filter { it.id != tabId }
        val newActiveTabId = if (_uiState.value.activeTabId == tabId) {
            updatedTabs.firstOrNull()?.id ?: ""
        } else {
            _uiState.value.activeTabId
        }
        
        _uiState.value = _uiState.value.copy(
            tabs = updatedTabs,
            activeTabId = newActiveTabId
        )
    }

    fun navigateToUrl(url: String) {
        val formattedUrl = if (!url.startsWith("http://") && !url.startsWith("https://")) {
            if (url.contains(".")) "https://$url" else "https://www.google.com/search?q=$url"
        } else url

        updateActiveTab { tab ->
            tab.copy(url = formattedUrl, isLoading = true)
        }
        
        checkBookmarkStatus()
    }

    fun goBack() {
        // This would be handled by WebView
    }

    fun goForward() {
        // This would be handled by WebView
    }

    fun refresh() {
        updateActiveTab { tab ->
            tab.copy(isLoading = true)
        }
    }

    fun toggleBookmark() {
        val activeTab = getActiveTab() ?: return
        
        viewModelScope.launch {
            if (_uiState.value.isCurrentUrlBookmarked) {
                // Remove bookmark
                bookmarkRepository.getBookmarkByUrl(activeTab.url)?.let { bookmark ->
                    bookmarkRepository.deleteBookmark(bookmark)
                }
            } else {
                // Add bookmark
                val bookmark = com.abrowser.modern.domain.model.Bookmark(
                    title = activeTab.title,
                    url = activeTab.url,
                    favicon = activeTab.favicon
                )
                bookmarkRepository.insertBookmark(bookmark)
            }
            checkBookmarkStatus()
        }
    }

    fun downloadVideo(videoInfo: VideoInfo) {
        viewModelScope.launch {
            downloadRepository.startDownload(
                url = videoInfo.url,
                fileName = "${videoInfo.title}.${videoInfo.format}",
                mimeType = "video/${videoInfo.format}"
            )
        }
    }

    fun createWebViewClient(onVideoDetected: (VideoInfo) -> Unit): WebViewClient {
        return object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                super.onPageStarted(view, url, favicon)
                updateActiveTab { tab ->
                    tab.copy(isLoading = true, url = url ?: "")
                }
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                updateActiveTab { tab ->
                    tab.copy(
                        isLoading = false,
                        title = view?.title ?: "New Tab",
                        url = url ?: "",
                        canGoBack = view?.canGoBack() ?: false,
                        canGoForward = view?.canGoForward() ?: false
                    )
                }
                
                // Inject JavaScript to detect videos
                view?.evaluateJavascript("""
                    (function() {
                        const videos = document.querySelectorAll('video');
                        if (videos.length > 0) {
                            const video = videos[0];
                            const videoInfo = {
                                title: document.title || 'Video',
                                url: video.src || video.currentSrc,
                                duration: video.duration || 0
                            };
                            return JSON.stringify(videoInfo);
                        }
                        return null;
                    })();
                """) { result ->
                    if (result != "null" && result != null) {
                        // Parse video info and show overlay
                        try {
                            val videoInfo = VideoInfo(
                                title = view?.title ?: "Video",
                                url = url ?: "",
                                quality = "720p",
                                format = "mp4"
                            )
                            onVideoDetected(videoInfo)
                        } catch (e: Exception) {
                            // Handle parsing error
                        }
                    }
                }
                
                checkBookmarkStatus()
            }
        }
    }

    fun createWebChromeClient(): WebChromeClient {
        return object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                updateActiveTab { tab ->
                    tab.copy(progress = newProgress)
                }
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                updateActiveTab { tab ->
                    tab.copy(title = title ?: "New Tab")
                }
            }
        }
    }

    private fun updateActiveTab(update: (BrowserTab) -> BrowserTab) {
        val updatedTabs = _uiState.value.tabs.map { tab ->
            if (tab.id == _uiState.value.activeTabId) {
                update(tab)
            } else {
                tab
            }
        }
        _uiState.value = _uiState.value.copy(tabs = updatedTabs)
    }

    private fun getActiveTab(): BrowserTab? {
        return _uiState.value.tabs.find { it.id == _uiState.value.activeTabId }
    }

    private fun checkBookmarkStatus() {
        val activeTab = getActiveTab() ?: return
        if (activeTab.url.isEmpty()) return
        
        viewModelScope.launch {
            val isBookmarked = bookmarkRepository.isBookmarked(activeTab.url)
            _uiState.value = _uiState.value.copy(isCurrentUrlBookmarked = isBookmarked)
        }
    }
}