package com.tanveer.focusflow.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.compose.material3.*

private val LightColors = lightColorScheme(
    primary = Purple700,
    onPrimary = White00,
    secondary = Teal400,
    surface = White00,
    background = White00,
    onBackground = Navy900,
    error = Pink300
)

private val DarkColors = darkColorScheme(
    primary = Purple700,
    onPrimary = White00,
    secondary = Teal400,
    surface = Surface00,
    background = Navy900,
    onBackground = White00,
    error = Pink300
)

@Composable
fun FocusFlowTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val scheme = if (useDarkTheme) DarkColors else LightColors
    val sysUi = rememberSystemUiController()
    sysUi.setStatusBarColor(scheme.background, darkIcons = !useDarkTheme)
    MaterialTheme(
        colorScheme = scheme,
        typography = FocusTypography,
        content = content
    )
}
