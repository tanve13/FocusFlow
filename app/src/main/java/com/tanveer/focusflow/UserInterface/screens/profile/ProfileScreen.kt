package com.tanveer.focusflow.UserInterface.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.tanveer.focusflow.viewModel.ProfileViewModel
import com.tanveer.focusflow.R

@Composable
fun ProfileScreen(
    navToEdit: () -> Unit,
    navToLogin: () -> Unit,
    uid: String = "current_uid",
    vm: ProfileViewModel = hiltViewModel()
) {
    val user by vm.user.collectAsState()

    LaunchedEffect(Unit) { vm.loadUser(uid) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(24.dp))

        // Avatar
        Image(
            painter = painterResource(id = R.drawable.baseline_person_pin_24), // placeholder
            contentDescription = "Profile Avatar",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.Gray)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(user?.name ?: "Your Name", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(6.dp))
        Text(user?.email ?: "email@example.com", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = navToEdit, modifier = Modifier.fillMaxWidth()) {
            Text("Edit Profile")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = { vm.logout { navToLogin() } },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout", color = Color.Red)
        }
    }
}
