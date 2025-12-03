package com.tanveer.focusflow.ui.navigation

import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tanveer.focusflow.UserInterface.navigation.Screen
import com.tanveer.focusflow.UserInterface.screens.insights.InsightsScreen
import com.tanveer.focusflow.UserInterface.screens.music.AmbientSoundScreen
import com.tanveer.focusflow.UserInterface.screens.notes.NotesScreen
import com.tanveer.focusflow.UserInterface.screens.tasks.TaskScreen
import com.tanveer.focusflow.ui.components.BottomNavigationBar
import com.tanveer.focusflow.ui.screens.home.HomeScreen
import com.tanveer.focusflow.ui.components.SideSlider

@Composable
fun MainNavGraph() {
    val navController = rememberNavController()
    var showSlider by remember { mutableStateOf(false) }

    Scaffold(bottomBar = { BottomNavigationBar(navController) }) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            NavHost(navController = navController, startDestination = Screen.Home.route) {
                composable(Screen.Home.route) {
                    HomeScreen(onOpenMusic = { navController.navigate(Screen.Music.route) }, onOpenProfile = { showSlider = true })
                }
                composable(Screen.Tasks.route) {
                    TaskScreen(uid = "current_uid")
                }
                composable(Screen.Notes.route) {
                    NotesScreen(uid = "current_uid")
                }
                composable(Screen.Insights.route) {
                    InsightsScreen()
                }
                composable(Screen.Music.route) {
                    AmbientSoundScreen(onBack = { navController.popBackStack() })
                }
            }

            SideSlider(visible = showSlider, onClose = { showSlider = false })
        }
    }
}
