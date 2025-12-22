package com.tanveer.focusflow.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.tanveer.focusflow.UserInterface.navigation.Screen

data class BottomItem(val route: String, val icon: ImageVector, val title: String)

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomItem(Screen.Home.route, Icons.Default.Timer, "Home"),
        BottomItem(Screen.Tasks.route, Icons.Default.List, "Tasks"),
        BottomItem(Screen.Notification.route, Icons.Default.Notifications, "Notification"),
        BottomItem(Screen.Insights.route, Icons.Default.BarChart, "Insights")
    )

    val backStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry.value?.destination?.route

    NavigationBar(
        containerColor = Color.White,  // 👈 background black
        contentColor = Color.White     // 👈 default content white
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(Screen.Home.route) { saveState = true }
                        }
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,    // selected icon color
                    unselectedIconColor = Color.Black.copy(alpha = 0.6f), // unselected icon
                    selectedTextColor = Color.Black,    // selected label color
                    unselectedTextColor = Color.Black.copy(alpha = 0.6f) // unselected label
                )
            )
        }
    }
}
