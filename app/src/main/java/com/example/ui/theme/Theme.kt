package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = VibrantBlue,
    onPrimary = Color.White,
    primaryContainer = VibrantBlueDark,
    onPrimaryContainer = VibrantBlueContainer,
    secondary = VibrantSecondary,
    onSecondary = Color.White,
    secondaryContainer = VibrantDarkSurfaceVariant,
    onSecondaryContainer = VibrantDarkTextPrimary,
    tertiary = VibrantTertiary,
    background = VibrantDarkBackground,
    surface = VibrantDarkSurface,
    surfaceVariant = VibrantDarkSurfaceVariant,
    onBackground = VibrantDarkTextPrimary,
    onSurface = VibrantDarkTextPrimary,
    onSurfaceVariant = VibrantDarkTextSecondary,
    outline = VibrantLightOutline
  )

private val LightColorScheme =
  lightColorScheme(
    primary = VibrantBlue,
    onPrimary = Color.White,
    primaryContainer = VibrantBlueContainer,
    onPrimaryContainer = VibrantOnBlueContainer,
    secondary = VibrantSecondary,
    onSecondary = Color.White,
    secondaryContainer = VibrantSecondaryContainer,
    onSecondaryContainer = VibrantNavyDeep,
    tertiary = VibrantTertiary,
    background = VibrantLightBackground,
    surface = VibrantLightSurface,
    surfaceVariant = VibrantLightSurfaceVariant,
    onBackground = VibrantLightTextPrimary,
    onSurface = VibrantLightTextPrimary,
    onSurfaceVariant = VibrantLightTextSecondary,
    outline = VibrantLightOutline
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Keep Vibrant Palette theme consistent
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}


