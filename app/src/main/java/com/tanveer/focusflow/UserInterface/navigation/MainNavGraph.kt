package com.tanveer.focusflow.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.tanveer.focusflow.UserInterface.navigation.Screen
import com.tanveer.focusflow.UserInterface.screens.home.HomeScreen
import com.tanveer.focusflow.UserInterface.screens.tasks.TaskScreen
import com.tanveer.focusflow.UserInterface.screens.notes.NotesScreen
import com.tanveer.focusflow.UserInterface.screens.insights.InsightsScreen
import com.tanveer.focusflow.UserInterface.screens.music.AmbientSoundScreen
import com.tanveer.focusflow.UserInterface.screens.profile.EditProfileScreen
import com.tanveer.focusflow.UserInterface.screens.profile.ProfileScreen
import com.tanveer.focusflow.UserInterface.screens.settings.SettingsScreen
import com.tanveer.focusflow.ui.components.BottomNavigationBar
import com.tanveer.focusflow.ui.components.SideSlider

@Composable
fun MainNavGraph(navController: NavHostController) {

    val innerNavController = rememberNavController()
    var sliderVisible by remember { mutableStateOf(false) }

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
                navToEdit = { innerNavController.navigate("editProfile") },
                navToLogin = { navController.navigate("login") }
            )}

            composable("editProfile") { EditProfileScreen(
                navBack = { innerNavController.popBackStack() }
            )}
            composable("settings") {
                SettingsScreen()     // ← Ye tum banane wali ho
            }
            composable(Screen.Tasks.route) { TaskScreen(uid = "current_uid") }
            composable(Screen.Notes.route) { NotesScreen(uid = "current_uid") }
            composable(Screen.Insights.route) { InsightsScreen() }
            composable(Screen.Music.route) { AmbientSoundScreen(onBack = { innerNavController.popBackStack() }) }
        }
        var sliderVisible by remember { mutableStateOf(false) }

        SideSlider(
            visible = sliderVisible,
            onClose = { sliderVisible = false },
            openProfile = {
                sliderVisible = false
                navController.navigate("profile")
            },
            openSettings = {
                sliderVisible = false
                navController.navigate("settings")
            }
        )
    }
}
