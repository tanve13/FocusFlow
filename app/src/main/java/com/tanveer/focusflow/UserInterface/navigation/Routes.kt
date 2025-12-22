package com.tanveer.focusflow.UserInterface.navigation


sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Tasks : Screen("tasks")
    object Notification : Screen("notification")
    object Insights : Screen("insights")
    object Music : Screen("music")
}

