package com.abrowser.modern.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.abrowser.modern.ui.browser.BrowserWebView
import com.abrowser.modern.ui.browser.BrowserViewModel
import com.abrowser.modern.ui.components.AddressBar
import com.abrowser.modern.ui.components.TabBar
import com.abrowser.modern.ui.components.VideoDownloadDialog
import com.abrowser.modern.ui.viewmodel.BrowserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowserScreen(
    viewModel: BrowserViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top App Bar with tabs
        TopAppBar(
            title = {
                Text(
                    text = uiState.currentTab?.title ?: "ABrowser",
                    maxLines = 1
                )
            },
            actions = {
                IconButton(onClick = { viewModel.addNewTab() }) {
                    Icon(Icons.Default.Add, contentDescription = "New Tab")
                }
                IconButton(onClick = { viewModel.showMenu() }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = Color.White,
                actionIconContentColor = Color.White
            )
        )

        // Tab Bar
        if (uiState.tabs.size > 1) {
            TabBar(
                tabs = uiState.tabs,
                activeTabId = uiState.currentTab?.id,
                onTabSelected = { viewModel.switchToTab(it) },
                onTabClosed = { viewModel.closeTab(it) }
            )
        }

        // Address Bar
        AddressBar(
            url = uiState.currentUrl,
            isLoading = uiState.isLoading,
            onUrlChanged = { viewModel.navigateToUrl(it) },
            onRefresh = { viewModel.refresh() },
            onBack = { viewModel.goBack() },
            onForward = { viewModel.goForward() },
            canGoBack = uiState.canGoBack,
            canGoForward = uiState.canGoForward
        )

        // WebView
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            if (uiState.currentTab != null) {
                BrowserWebView(
                    url = tab.url,
                    onUrlChanged = { viewModel.updateCurrentUrl(it) },
                    onTitleChanged = { viewModel.updateCurrentTitle(it) },
                    onLoadingChanged = { viewModel.updateLoadingState(it) },
                    onVideoDetected = { videoInfo ->
                        viewModel.onVideoDetected(videoInfo)
                    }
                )
            } else {
                // Add a placeholder or auto-create tab
                Text(
                    text = "No tabs open. Create a new one!",
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // Video Download FAB
            if (uiState.detectedVideos.isNotEmpty()) {
                FloatingActionButton(
                    onClick = { viewModel.showVideoDownloadDialog() },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    containerColor = MaterialTheme.colorScheme.secondary
                ) {
                    Icon(
                        Icons.Default.Download,
                        contentDescription = "Download Video",
                        tint = Color.White
                    )
                }
            }
        }
    }

    // Video Download Dialog
    if (uiState.showVideoDownloadDialog) {
        VideoDownloadDialog(
            videos = uiState.detectedVideos,
            onDownload = { videoInfo ->
                viewModel.downloadVideo(videoInfo)
                viewModel.hideVideoDownloadDialog()
            },
            onDismiss = { viewModel.hideVideoDownloadDialog() }
        )
    }
}

@Composable
fun VideoDownloadDialog(
    videos: detectedVideos,
    onDownload: (ERROR) -> hideVideoDownloadDialog,
    onDismiss: () -> hideVideoDownloadDialog
) {
    TODO("Not yet implemented")
}

@Composable
fun BrowserWebView(
    url: url,
    onUrlChanged: () -> updateCurrentUrl,
    onTitleChanged: () -> updateCurrentTitle,
    onLoadingChanged: () -> updateLoadingState,
    onVideoDetected: (ERROR) -> onVideoDetected
) {
    TODO("Not yet implemented")
}

@Composable
fun AddressBar(
    url: currentUrl,
    isLoading: Boolean,
    onUrlChanged: () -> Unit,
    onRefresh: () -> Unit,
    onBack: () -> Unit,
    onForward: () -> Unit,
    canGoBack: canGoBack,
    canGoForward: canGoForward
) {
    TODO("Not yet implemented")
}