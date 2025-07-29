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
import androidx.compose.ui.viewinterop.AndroidView
import android.webkit.WebView
import androidx.hilt.navigation.compose.hiltViewModel
import com.abrowser.modern.data.model.VideoInfo
import com.abrowser.modern.ui.viewmodel.BrowserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowserScreen(
    viewModel: BrowserViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Fix for potential null currentTab
    if (uiState.currentTab == null && uiState.tabs.isEmpty()) {
        viewModel.addNewTab()  // Auto-create a tab if none exist
    }

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

        // Tab Bar (simplified for now)
        if (uiState.tabs.size > 1) {
            // TODO: Implement TabBar component
        }

        // Address Bar (simplified for now)
        // TODO: Implement AddressBar component

        // WebView
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            if (uiState.currentTab != null) {
                AndroidView(
                    factory = { context ->
                        WebView(context).apply {
                            settings.javaScriptEnabled = true
                            settings.domStorageEnabled = true
                        }
                    },
                    update = { webView ->
                        if (webView.url != uiState.currentTab.url) {
                            webView.loadUrl(uiState.currentTab.url)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            } else {
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
    videos: List<VideoInfo>,
    onDownload: (VideoInfo) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Detected Videos") },
        text = { 
            Text("${videos.size} video(s) detected")
        },
        confirmButton = {
            TextButton(onClick = { 
                if (videos.isNotEmpty()) {
                    onDownload(videos.first())
                }
            }) {
                Text("Download")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}