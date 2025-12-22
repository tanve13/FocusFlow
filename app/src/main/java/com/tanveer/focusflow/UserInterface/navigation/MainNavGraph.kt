package com.tanveer.focusflow.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tanveer.focusflow.R
import com.tanveer.focusflow.UserInterface.navigation.Screen
import com.tanveer.focusflow.UserInterface.screens.achievements.AchievementScreen
import com.tanveer.focusflow.UserInterface.screens.home.HomeScreen
import com.tanveer.focusflow.UserInterface.screens.tasks.TaskScreen
import com.tanveer.focusflow.UserInterface.screens.insights.InsightsScreen
import com.tanveer.focusflow.UserInterface.screens.music.AmbientSoundScreen
import com.tanveer.focusflow.UserInterface.screens.notifications.NotificationScreen
import com.tanveer.focusflow.UserInterface.screens.profile.EditProfileScreen
import com.tanveer.focusflow.UserInterface.screens.profile.ProfileScreen
import com.tanveer.focusflow.UserInterface.screens.settings.SettingsScreen
import com.tanveer.focusflow.data.model.Badge
import com.tanveer.focusflow.ui.components.BottomNavigationBar

@Composable
fun MainNavGraph(navController: NavHostController) {

    val innerNavController = rememberNavController()
    Scaffold(bottomBar = { BottomNavigationBar(innerNavController) }) { padding ->
        NavHost(
            navController = innerNavController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    navController = innerNavController,
                    onOpenMusic = { innerNavController.navigate(Screen.Music.route) },
                )
            }
            composable("profile") { ProfileScreen(
                navToLogin = { navController.navigate("login") }
            )}

            composable("editProfile") { EditProfileScreen(
                navBack = { innerNavController.popBackStack() }
            )}
            composable("settings") {
                SettingsScreen(navBack = { navController.popBackStack() })     // ← Ye tum banane wali ho
            }
            composable("achievements") {
                // Dummy badges and stats
                val badges = listOf(
                    Badge("1", "Consistency Starter", "Maintain a 7-day streak", R.drawable.img, unlocked = true, progress = 1f),
                    Badge("2", "Focus Warrior", "Complete a 30-day streak", R.drawable.img, unlocked = false, progress = 0.5f)
                )
                val streak = 10
                val todaySessions = 5
                val totalSessions = 120
                val totalMinutes = 600
                AchievementScreen(
                    badges = badges,
                    streak = streak,
                    todaySessions = todaySessions,
                    totalSessions = totalSessions,
                    totalMinutes = totalMinutes,
                    navBack = { navController.popBackStack() },
                    onShare = {}
                )
            }
            composable(Screen.Tasks.route) { TaskScreen(uid = "current_uid") }
            composable(Screen.Notification.route) { NotificationScreen() }
            composable(Screen.Insights.route) { InsightsScreen() }
            composable(Screen.Music.route) { AmbientSoundScreen(onBack = { innerNavController.popBackStack() }) }
        }


    }
}
