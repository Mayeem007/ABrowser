package com.abrowser.modern.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
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
    var showVideoDialog by remember { mutableStateOf(false) }
    var detectedVideo by remember { mutableStateOf<VideoInfo?>(null) }
    var urlText by remember { mutableStateOf("") }

    // Get the active tab
    val activeTab = uiState.tabs.find { it.id == uiState.activeTabId }

    // Update URL text when active tab changes
    LaunchedEffect(activeTab?.url) {
        urlText = activeTab?.url ?: ""
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = activeTab?.title ?: "ABrowser",
                    maxLines = 1
                )
            },
            actions = {
                IconButton(onClick = { viewModel.createNewTab() }) {
                    Icon(Icons.Default.Add, contentDescription = "New Tab")
                }
                IconButton(onClick = { viewModel.toggleBookmark() }) {
                    Icon(
                        if (uiState.isCurrentUrlBookmarked) Icons.Filled.Star else Icons.Outlined.Star,
                        contentDescription = "Bookmark"
                    )
                }
                IconButton(onClick = { /* TODO: Show menu */ }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = Color.White,
                actionIconContentColor = Color.White
            )
        )

        // Navigation Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.goBack() },
                enabled = activeTab?.canGoBack == true
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            
            IconButton(
                onClick = { viewModel.goForward() },
                enabled = activeTab?.canGoForward == true
            ) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Forward")
            }
            
            IconButton(onClick = { viewModel.refresh() }) {
                Icon(Icons.Default.Refresh, contentDescription = "Refresh")
            }
            
            OutlinedTextField(
                value = urlText,
                onValueChange = { urlText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Enter URL") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Go
                ),
                keyboardActions = KeyboardActions(
                    onGo = {
                        viewModel.navigateToUrl(urlText)
                    }
                )
            )
        }

        // WebView
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            if (activeTab != null) {
                AndroidView(
                    factory = { context ->
                        WebView(context).apply {
                            settings.javaScriptEnabled = true
                            settings.domStorageEnabled = true
                            settings.loadWithOverviewMode = true
                            settings.useWideViewPort = true
                            settings.builtInZoomControls = true
                            settings.displayZoomControls = false
                            
                            webViewClient = viewModel.createWebViewClient { videoInfo ->
                                detectedVideo = videoInfo
                                showVideoDialog = true
                            }
                            webChromeClient = viewModel.createWebChromeClient()
                            
                            // Set this WebView in the ViewModel
                            viewModel.setWebView(this)
                        }
                    },
                    update = { webView ->
                        if (webView.url != activeTab.url && activeTab.url.isNotEmpty()) {
                            webView.loadUrl(activeTab.url)
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

            // Loading indicator
            if (uiState.isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                )
            }

            // Video Download FAB
            if (detectedVideo != null) {
                FloatingActionButton(
                    onClick = { showVideoDialog = true },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    containerColor = MaterialTheme.colorScheme.secondary
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Download Video",
                        tint = Color.White
                    )
                }
            }
        }
    }

    // Video Download Dialog
    if (showVideoDialog && detectedVideo != null) {
        VideoDownloadDialog(
            video = detectedVideo!!,
            onDownload = { videoInfo ->
                viewModel.downloadVideo(videoInfo)
                showVideoDialog = false
            },
            onDismiss = { showVideoDialog = false }
        )
    }
}

@Composable
fun VideoDownloadDialog(
    video: VideoInfo,
    onDownload: (VideoInfo) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Download Video") },
        text = { 
            Column {
                Text("Title: ${video.title}")
                Text("Quality: ${video.quality}")
                Text("Format: ${video.format}")
            }
        },
        confirmButton = {
            TextButton(onClick = { onDownload(video) }) {
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