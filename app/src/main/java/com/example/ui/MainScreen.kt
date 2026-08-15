package com.example.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.R
import com.example.ui.theme.AccentError
import com.example.ui.theme.AccentSuccess
import com.example.ui.theme.AccentWarning
import com.example.ui.theme.VibrantBlue
import com.example.ui.theme.VibrantBlueContainer
import com.example.ui.theme.VibrantNavyDeep
import com.example.ui.theme.VibrantOnBlueContainer
import com.example.ui.theme.VibrantSecondary
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: WebViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Handle back button inside WebView
    BackHandler(enabled = uiState.canGoBack) {
        viewModel.goBack()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                TopAppBar(
                    title = {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (uiState.mode == WebMode.LOCAL_HTML) "Local HTML" else uiState.pageTitle,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            if (uiState.mode == WebMode.LOCAL_HTML)
                                                AccentSuccess.copy(alpha = 0.15f)
                                            else
                                                VibrantBlueContainer
                                        )
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = if (uiState.mode == WebMode.LOCAL_HTML) "Offline" else "Online",
                                        color = if (uiState.mode == WebMode.LOCAL_HTML) AccentSuccess else VibrantOnBlueContainer,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Text(
                                text = uiState.currentUrl,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    navigationIcon = {
                        Row {
                            IconButton(
                                onClick = { viewModel.goBack() },
                                enabled = uiState.canGoBack,
                                modifier = Modifier.testTag("nav_back_button")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = if (uiState.canGoBack) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                )
                            }
                            IconButton(
                                onClick = { viewModel.goForward() },
                                enabled = uiState.canGoForward,
                                modifier = Modifier.testTag("nav_forward_button")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Forward",
                                    tint = if (uiState.canGoForward) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                )
                            }
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { viewModel.reload() },
                            modifier = Modifier.testTag("nav_refresh_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh"
                            )
                        }
                        IconButton(
                            onClick = { viewModel.setInfoDialogOpen(true) },
                            modifier = Modifier.testTag("nav_info_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.HelpOutline,
                                contentDescription = "APK Build Guide"
                            )
                        }
                        IconButton(
                            onClick = { viewModel.setSettingsOpen(true) },
                            modifier = Modifier.testTag("nav_settings_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )

                // Progress Bar
                AnimatedVisibility(
                    visible = uiState.isLoading,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    LinearProgressIndicator(
                        progress = { uiState.progress / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp),
                        color = VibrantBlue,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // WebView Container
            AndroidWebViewContainer(
                viewModel = viewModel,
                modifier = Modifier.fillMaxSize()
            )

            // Error Overlay Screen if loading failed
            if (uiState.hasError) {
                ErrorStateOverlay(
                    errorMessage = uiState.errorMessage ?: "Unable to load the requested web page.",
                    onRetry = { viewModel.reload() },
                    onSwitchToLocal = { viewModel.switchMode(WebMode.LOCAL_HTML) }
                )
            }
        }
    }

    // Settings Bottom Sheet
    if (uiState.isSettingsOpen) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.setSettingsOpen(false) },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier.testTag("settings_bottom_sheet")
        ) {
            SettingsSheetContent(
                uiState = uiState,
                onModeSelected = { viewModel.switchMode(it) },
                onLoadUrl = { viewModel.loadCustomRemoteUrl(it) },
                onToggleJavaScript = { viewModel.toggleJavaScript(it) },
                onClearCache = { viewModel.clearCacheAndCookies() },
                onOpenInBrowser = {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uiState.currentUrl))
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        // ignore
                    }
                },
                onShowGuide = {
                    viewModel.setSettingsOpen(false)
                    viewModel.setInfoDialogOpen(true)
                }
            )
        }
    }

    // GitHub & Project Build Guide Dialog
    if (uiState.isInfoDialogOpen) {
        ApkBuildGuideDialog(
            onDismiss = { viewModel.setInfoDialogOpen(false) }
        )
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun AndroidWebViewContainer(
    viewModel: WebViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    AndroidView(
        modifier = modifier.testTag("web_view_element"),
        factory = { ctx ->
            WebView(ctx).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    databaseEnabled = true
                    allowFileAccess = true
                    allowContentAccess = true
                    useWideViewPort = true
                    loadWithOverviewMode = true
                    builtInZoomControls = true
                    displayZoomControls = false
                    setSupportZoom(true)
                    mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                    cacheMode = WebSettings.LOAD_DEFAULT
                }

                webChromeClient = object : WebChromeClient() {
                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                        viewModel.setProgress(newProgress)
                    }

                    override fun onReceivedTitle(view: WebView?, title: String?) {
                        viewModel.setPageTitle(title ?: "Web to APK")
                    }
                }

                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        viewModel.onPageStarted(url)
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        viewModel.onPageFinished(url)
                    }

                    override fun onReceivedError(
                        view: WebView?,
                        request: WebResourceRequest?,
                        error: WebResourceError?
                    ) {
                        // Only show full error if it's the main frame
                        if (request?.isForMainFrame == true) {
                            val description = error?.description?.toString() ?: "Network Connection Error"
                            viewModel.onReceivedError(description)
                        }
                    }

                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        val uri = request?.url ?: return false
                        val scheme = uri.scheme ?: ""

                        // Handle standard web schemes inside WebView
                        if (scheme == "http" || scheme == "https" || scheme == "file") {
                            return false
                        }

                        // Handle external schemes (tel:, mailto:, intent:, etc.)
                        return try {
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            context.startActivity(intent)
                            true
                        } catch (e: Exception) {
                            false
                        }
                    }
                }

                viewModel.registerWebView(this)
                loadUrl(uiState.currentUrl)
            }
        },
        update = { webView ->
            // If current URL changed programmatically and is different, load it
            if (webView.url != uiState.currentUrl && !uiState.isLoading) {
                webView.loadUrl(uiState.currentUrl)
            }
        }
    )
}

@Composable
fun ErrorStateOverlay(
    errorMessage: String,
    onRetry: () -> Unit,
    onSwitchToLocal: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.95f))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(AccentError.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Error",
                        tint = AccentError,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "មិនអាចភ្ជាប់ទៅគេហទំព័របានទេ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = errorMessage,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onSwitchToLocal,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("switch_to_local_error_button")
                    ) {
                        Text(text = "Local HTML")
                    }

                    Button(
                        onClick = onRetry,
                        colors = ButtonDefaults.buttonColors(containerColor = VibrantBlue),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("retry_error_button")
                    ) {
                        Text(text = "ព្យាយាមម្តងទៀត", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsSheetContent(
    uiState: WebUiState,
    onModeSelected: (WebMode) -> Unit,
    onLoadUrl: (String) -> Unit,
    onToggleJavaScript: (Boolean) -> Unit,
    onClearCache: () -> Unit,
    onOpenInBrowser: () -> Unit,
    onShowGuide: () -> Unit,
    modifier: Modifier = Modifier
) {
    var urlInput by remember { mutableStateOf(if (uiState.mode == WebMode.REMOTE_URL) uiState.currentUrl else uiState.customRemoteUrl) }

    val presetUrls = listOf(
        "https://google.com" to "Google",
        "https://wikipedia.org" to "Wikipedia",
        "https://github.com" to "GitHub",
        "https://developer.mozilla.org" to "MDN Web",
        "https://news.ycombinator.com" to "HackerNews"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "⚙️ ការកំណត់ WebView & URL",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = VibrantNavyDeep
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(VibrantBlueContainer)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Auto-Repo v1.0",
                    color = VibrantOnBlueContainer,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mode Selector Chips
        Text(
            text = "ជ្រើសរើសប្រភពមាតិកា (Content Source)",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FilterChip(
                selected = uiState.mode == WebMode.LOCAL_HTML,
                onClick = { onModeSelected(WebMode.LOCAL_HTML) },
                label = { Text("📱 Local HTML (Offline)") },
                leadingIcon = if (uiState.mode == WebMode.LOCAL_HTML) {
                    { Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp)) }
                } else null,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = AccentSuccess.copy(alpha = 0.2f),
                    selectedLabelColor = AccentSuccess
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .weight(1f)
                    .testTag("mode_chip_local")
            )

            FilterChip(
                selected = uiState.mode == WebMode.REMOTE_URL,
                onClick = { onModeSelected(WebMode.REMOTE_URL) },
                label = { Text("🌐 Website URL") },
                leadingIcon = if (uiState.mode == WebMode.REMOTE_URL) {
                    { Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp)) }
                } else null,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = VibrantBlueContainer,
                    selectedLabelColor = VibrantOnBlueContainer
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .weight(1f)
                    .testTag("mode_chip_remote")
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Custom URL Input
        Text(
            text = "បញ្ចូល Website URL ផ្ទាល់ខ្លួន",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = urlInput,
            onValueChange = { urlInput = it },
            placeholder = { Text("https://yourwebsite.com") },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Go
            ),
            keyboardActions = KeyboardActions(onGo = { onLoadUrl(urlInput) }),
            trailingIcon = {
                Button(
                    onClick = { onLoadUrl(urlInput) },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VibrantBlue),
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .testTag("url_go_button")
                ) {
                    Text("Go")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("custom_url_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Preset Quick Links
        Text(
            text = "គេហទំព័រគំរូ (Quick Presets):",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(6.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(presetUrls) { (url, label) ->
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.clickable {
                        urlInput = url
                        onLoadUrl(url)
                    }
                ) {
                    Text(
                        text = label,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))

        // Toggle JavaScript
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "JavaScript Engine",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "បើកដំណើរការ JS សម្រាប់គេហទំព័រទំនើប",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Switch(
                checked = uiState.isJavaScriptEnabled,
                onCheckedChange = onToggleJavaScript,
                modifier = Modifier.testTag("js_toggle_switch")
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action Buttons Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onClearCache,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .weight(1f)
                    .testTag("clear_cache_button")
            ) {
                Icon(Icons.Default.DeleteSweep, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Clear Cache", fontSize = 12.sp)
            }

            OutlinedButton(
                onClick = onOpenInBrowser,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .weight(1f)
                    .testTag("open_browser_button")
            ) {
                Icon(Icons.Default.OpenInBrowser, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Browser", fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onShowGuide,
            colors = ButtonDefaults.buttonColors(containerColor = VibrantBlue),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("show_guide_button")
        ) {
            Icon(Icons.Default.Code, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("របៀប Push ទៅ GitHub & Build APK", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}

@Composable
fun ApkBuildGuideDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = VibrantBlue,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "📦 ការបង្កើត APK លើ GitHub",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = VibrantNavyDeep
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "គម្រោងនេះត្រូវបានរៀបចំរួចរាល់ជាមួយ GitHub Actions Workflow សម្រាប់ Build APK ដោយស្វ័យប្រវត្តិ។",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

                GuideStepItem(
                    step = "1",
                    title = "ដាក់ File HTML ក្នុង assets:",
                    desc = "ដាក់ឯកសារ web ទាំងអស់ក្នុង `app/src/main/assets/www/`"
                )

                GuideStepItem(
                    step = "2",
                    title = "កំណត់ Package Name & App Name:",
                    desc = "Package Name: `com.example.webapp` (ក្នុង build.gradle.kts)\nឈ្មោះ App: `Web to APK` (ក្នុង strings.xml)"
                )

                GuideStepItem(
                    step = "3",
                    title = "Push ទៅកាន់ GitHub:",
                    desc = "បង្កើត Repository នៅលើ GitHub រួច git push កូដនេះទៅកាន់ main branch។"
                )

                GuideStepItem(
                    step = "4",
                    title = "Download APK ពី GitHub Actions:",
                    desc = "ចូលទៅកាន់ Actions Tab ក្នុង GitHub -> ចុចលើ Build Workflow -> Download file `app-debug-apk.zip`"
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VibrantBlue)
            ) {
                Text("យល់ព្រម (Got it)", fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
fun GuideStepItem(
    step: String,
    title: String,
    desc: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(VibrantBlue),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = step,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = desc,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 16.sp
            )
        }
    }
}

