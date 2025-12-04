package com.tanveer.focusflow.UserInterface.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navBack: () -> Unit) {

    // ------------------------ STATES ------------------------
    var focusMinutes by remember { mutableStateOf(25f) }
    var breakMinutes by remember { mutableStateOf(5f) }

    var soundEnabled by remember { mutableStateOf(true) }
    var selectedSound by remember { mutableStateOf("Rain") }

    var themeSelected by remember { mutableStateOf("System Default") }

    var showSoundDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    val soundOptions = listOf("Rain", "Wind", "Fireplace", "Forest", "White Noise")
    val themeOptions = listOf("Light", "Dark", "System Default")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = navBack) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            // ---------------- HEADER GRADIENT ----------------
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Black, Color(0xFF202020))
                        )
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Text(
                        "App Settings",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        "Customize your focus experience",
                        color = Color.White.copy(0.8f),
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(Modifier.height(18.dp))

            // ---------------- TIMER CARD ----------------
            SettingsCard(title = "Timer Duration") {

                Text("Focus Duration: ${focusMinutes.toInt()} min")
                Slider(
                    value = focusMinutes,
                    onValueChange = { focusMinutes = it },
                    valueRange = 10f..60f
                )

                Spacer(Modifier.height(12.dp))

                Text("Break Duration: ${breakMinutes.toInt()} min")
                Slider(
                    value = breakMinutes,
                    onValueChange = { breakMinutes = it },
                    valueRange = 3f..30f
                )
            }

            Spacer(Modifier.height(16.dp))

            // ---------------- SOUND CARD ----------------
            SettingsCard(title = "Focus Sound") {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Enable Focus Sound")
                    Switch(checked = soundEnabled, onCheckedChange = { soundEnabled = it })
                }

                if (soundEnabled) {
                    Divider(Modifier.padding(vertical = 12.dp))
                    SettingRow(
                        title = "Select Sound",
                        value = selectedSound,
                        icon = Icons.Default.MusicNote
                    ) { showSoundDialog = true }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ---------------- THEME CARD ----------------
            SettingsCard(title = "Appearance") {
                SettingRow(
                    title = "Theme",
                    value = themeSelected,
                    icon = Icons.Default.DarkMode
                ) { showThemeDialog = true }
            }

            Spacer(Modifier.height(16.dp))

            // ---------------- Logout ----------------
            SettingsCard(title = "Account") {
                SettingRow(
                    title = "Logout",
                    value = "Tap to logout",
                    color = Color.Red
                ) { showLogoutDialog = true }
            }

            Spacer(Modifier.height(30.dp))
        }
    }

    // ---------------- SOUND DIALOG ----------------
    if (showSoundDialog) {
        SelectDialog(
            title = "Choose Focus Sound",
            options = soundOptions,
            selected = selectedSound,
            onSelect = {
                selectedSound = it
                showSoundDialog = false
            },
            onDismiss = { showSoundDialog = false }
        )
    }

    // ---------------- THEME DIALOG ----------------
    if (showThemeDialog) {
        SelectDialog(
            title = "Choose Theme",
            options = themeOptions,
            selected = themeSelected,
            onSelect = {
                themeSelected = it
                showThemeDialog = false
            },
            onDismiss = { showThemeDialog = false }
        )
    }

    // ---------------- LOGOUT CONFIRMATION ----------------
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout") },
            text = { Text("Are you sure you want to logout?") },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    navBack()
                }) { Text("Logout", color = Color.Red) }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun SettingsCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(Modifier.height(10.dp))
            content()
        }
    }
}

@Composable
fun SettingRow(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    color: Color = Color.Black,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            icon?.let {
                Icon(it, contentDescription = null, tint = Color.Gray)
                Spacer(Modifier.width(10.dp))
            }
            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = color)
        }

        Text(value, color = Color.Gray)
    }
}

@Composable
fun SelectDialog(
    title: String,
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                options.forEach { option ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(option) }
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(option, Modifier.weight(1f), fontSize = 16.sp)
                        if (selected == option) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = Color.Black)
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        }
    )
}
