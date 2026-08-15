package com.example.ui

import android.webkit.CookieManager
import android.webkit.WebStorage
import android.webkit.WebView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class WebMode(val title: String) {
    LOCAL_HTML("Local HTML (Offline)"),
    REMOTE_URL("Live Website URL")
}

data class WebUiState(
    val mode: WebMode = WebMode.LOCAL_HTML,
    val currentUrl: String = "file:///android_asset/www/index.html",
    val defaultLocalUrl: String = "file:///android_asset/www/index.html",
    val customRemoteUrl: String = "https://example.com",
    val pageTitle: String = "Web to APK",
    val progress: Int = 0,
    val isLoading: Boolean = false,
    val canGoBack: Boolean = false,
    val canGoForward: Boolean = false,
    val hasError: Boolean = false,
    val errorMessage: String? = null,
    val isSettingsOpen: Boolean = false,
    val isInfoDialogOpen: Boolean = false,
    val isJavaScriptEnabled: Boolean = true
)

class WebViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(WebUiState())
    val uiState: StateFlow<WebUiState> = _uiState.asStateFlow()

    private var webViewRef: WebView? = null

    fun registerWebView(webView: WebView) {
        this.webViewRef = webView
    }

    fun setProgress(progress: Int) {
        _uiState.update {
            it.copy(
                progress = progress,
                isLoading = progress < 100
            )
        }
    }

    fun setPageTitle(title: String) {
        _uiState.update { it.copy(pageTitle = if (title.isNotBlank()) title else "Web to APK") }
    }

    fun onPageStarted(url: String?) {
        _uiState.update {
            it.copy(
                isLoading = true,
                hasError = false,
                errorMessage = null,
                currentUrl = url ?: it.currentUrl
            )
        }
    }

    fun onPageFinished(url: String?) {
        _uiState.update {
            it.copy(
                isLoading = false,
                progress = 100,
                currentUrl = url ?: it.currentUrl,
                canGoBack = webViewRef?.canGoBack() ?: false,
                canGoForward = webViewRef?.canGoForward() ?: false
            )
        }
    }

    fun onReceivedError(description: String) {
        _uiState.update {
            it.copy(
                isLoading = false,
                hasError = true,
                errorMessage = description
            )
        }
    }

    fun switchMode(newMode: WebMode) {
        if (_uiState.value.mode == newMode) return

        _uiState.update {
            val targetUrl = if (newMode == WebMode.LOCAL_HTML) {
                it.defaultLocalUrl
            } else {
                it.customRemoteUrl
            }
            it.copy(
                mode = newMode,
                currentUrl = targetUrl,
                hasError = false,
                errorMessage = null
            )
        }
        loadUrl(_uiState.value.currentUrl)
    }

    fun loadCustomRemoteUrl(url: String) {
        var formatted = url.trim()
        if (formatted.isNotBlank()) {
            if (!formatted.startsWith("http://") && !formatted.startsWith("https://") && !formatted.startsWith("file://")) {
                formatted = "https://$formatted"
            }
            _uiState.update {
                it.copy(
                    mode = WebMode.REMOTE_URL,
                    customRemoteUrl = formatted,
                    currentUrl = formatted,
                    hasError = false,
                    errorMessage = null,
                    isSettingsOpen = false
                )
            }
            loadUrl(formatted)
        }
    }

    fun reload() {
        _uiState.update { it.copy(hasError = false, errorMessage = null) }
        webViewRef?.reload()
    }

    fun goBack(): Boolean {
        return if (webViewRef?.canGoBack() == true) {
            webViewRef?.goBack()
            true
        } else {
            false
        }
    }

    fun goForward() {
        if (webViewRef?.canGoForward() == true) {
            webViewRef?.goForward()
        }
    }

    fun loadUrl(url: String) {
        _uiState.update { it.copy(hasError = false, errorMessage = null) }
        webViewRef?.loadUrl(url)
    }

    fun clearCacheAndCookies() {
        webViewRef?.clearCache(true)
        webViewRef?.clearHistory()
        WebStorage.getInstance().deleteAllData()
        CookieManager.getInstance().removeAllCookies(null)
        CookieManager.getInstance().flush()
        reload()
    }

    fun setSettingsOpen(isOpen: Boolean) {
        _uiState.update { it.copy(isSettingsOpen = isOpen) }
    }

    fun setInfoDialogOpen(isOpen: Boolean) {
        _uiState.update { it.copy(isInfoDialogOpen = isOpen) }
    }

    fun toggleJavaScript(enabled: Boolean) {
        _uiState.update { it.copy(isJavaScriptEnabled = enabled) }
        webViewRef?.settings?.javaScriptEnabled = enabled
        reload()
    }
}
