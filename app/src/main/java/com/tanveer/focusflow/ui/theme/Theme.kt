package com.tanveer.focusflow.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController

val LightColors = lightColorScheme(
    primary = BW_Black,
    secondary = BW_Grey,

    background = BW_OffWhite,
    surface = BW_White,

    onPrimary = BW_White,
    onSecondary = BW_White,

    onBackground = BW_Black,
    onSurface = BW_Black,

    outline = BW_LightGrey
)

val DarkColors = darkColorScheme(
    primary = BW_White,
    secondary = BW_GreyDark,

    background = BW_Dark,
    surface = BW_SurfaceDark,

    onPrimary = BW_Black,
    onSecondary = BW_Black,

    onBackground = BW_White,
    onSurface = BW_White,

    outline = BW_GreyDark
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
