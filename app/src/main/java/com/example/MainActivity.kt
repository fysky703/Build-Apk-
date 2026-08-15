package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.ui.MainScreen
import com.example.ui.SplashScreen
import com.example.ui.WebViewModel
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

  private val webViewModel: WebViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        var showSplash by remember { mutableStateOf(true) }

        Crossfade(
          targetState = showSplash,
          label = "screen_transition",
          modifier = Modifier.fillMaxSize()
        ) { isSplash ->
          if (isSplash) {
            SplashScreen(
              onSplashFinished = { showSplash = false }
            )
          } else {
            MainScreen(
              viewModel = webViewModel
            )
          }
        }
      }
    }
  }
}

