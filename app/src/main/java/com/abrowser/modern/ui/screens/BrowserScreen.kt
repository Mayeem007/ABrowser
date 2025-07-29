package com.abrowser.modern.ui.screens

import android.webkit.WebView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.abrowser.modern.domain.model.BrowserTab
import com.abrowser.modern.data.model.VideoInfo
import com.abrowser.modern.ui.components.BrowserTopBar
import com.abrowser.modern.ui.components.TabBar
import com.abrowser.modern.ui.components.VideoPlayerOverlay
import com.abrowser.modern.ui.viewmodel.BrowserViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowserScreen(
    viewModel: BrowserViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    
    var showVideoOverlay by remember { mutableStateOf(false) }
    var currentVideoInfo by remember { mutableStateOf<VideoInfo?>(null) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Tab bar
        TabBar(
            tabs = uiState.tabs,
            activeTabId = uiState.activeTabId,
            onTabClick = viewModel::switchTab,
            onTabClose = viewModel::closeTab,
            onNewTab = viewModel::createNewTab
        )

        // Browser top bar
        val activeTab = uiState.tabs.find { it.id == uiState.activeTabId }
        if (activeTab != null) {
            BrowserTopBar(
                url = activeTab.url,
                isLoading = activeTab.isLoading,
                canGoBack = activeTab.canGoBack,
                canGoForward = activeTab.canGoForward,
                isBookmarked = uiState.isCurrentUrlBookmarked,
                onUrlChange = { /* Handle URL change */ },
                onNavigate = { url -> viewModel.navigateToUrl(url) },
                onBack = viewModel::goBack,
                onForward = viewModel::goForward,
                onRefresh = viewModel::refresh,
                onBookmarkToggle = viewModel::toggleBookmark,
                onMenuClick = { /* Handle menu click */ }
            )
        }

        // WebView content
        Box(modifier = Modifier.weight(1f)) {
            if (activeTab != null) {
                AndroidView(
                    factory = { context ->
                        WebView(context).apply {
                            settings.apply {
                                javaScriptEnabled = true
                                domStorageEnabled = true
                                loadWithOverviewMode = true
                                useWideViewPort = true
                                builtInZoomControls = true
                                displayZoomControls = false
                            }
                            
                            webViewClient = viewModel.createWebViewClient { videoInfo ->
                                currentVideoInfo = videoInfo
                                showVideoOverlay = true
                            }
                            
                            webChromeClient = viewModel.createWebChromeClient()
                        }
                    },
                    update = { webView ->
                        if (activeTab.url.isNotEmpty() && webView.url != activeTab.url) {
                            webView.loadUrl(activeTab.url)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Video player overlay
            currentVideoInfo?.let { videoInfo ->
                VideoPlayerOverlay(
                    videoInfo = videoInfo,
                    isVisible = showVideoOverlay,
                    onDownloadClick = {
                        viewModel.downloadVideo(videoInfo)
                        showVideoOverlay = false
                    },
                    onQualityClick = {
                        // Handle quality selection
                    },
                    onFullscreenClick = {
                        // Handle fullscreen
                    },
                    onDismiss = {
                        showVideoOverlay = false
                    }
                )
            }
        }
    }
}