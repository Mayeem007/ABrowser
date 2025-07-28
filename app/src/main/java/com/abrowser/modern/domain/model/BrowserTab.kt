package com.abrowser.modern.domain.model

data class BrowserTab(
    val id: String,
    val title: String = "New Tab",
    val url: String = "",
    val favicon: String? = null,
    val isLoading: Boolean = false,
    val canGoBack: Boolean = false,
    val canGoForward: Boolean = false,
    val progress: Int = 0,
    val isIncognito: Boolean = false,
    val isActive: Boolean = false
)