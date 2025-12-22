package com.tanveer.focusflow.UserInterface.screens.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.tanveer.focusflow.viewModel.ProfileViewModel
import com.tanveer.focusflow.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navToLogin: () -> Unit,
    uid: String = "current_uid",
    vm: ProfileViewModel = hiltViewModel()
) {
    val user by vm.user.collectAsState()
    var showEditDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

    val imagePicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { vm.uploadProfileImage(context, it, uid) }
    }

    LaunchedEffect(uid) { vm.loadUser(uid) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Profile") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(20.dp))

            Image(
                painter = if (!user?.photoUrl.isNullOrEmpty())
                    rememberAsyncImagePainter(user!!.photoUrl)
                else
                    painterResource(R.drawable.baseline_person_24),
                contentDescription = "Profile Avatar",
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .clickable { imagePicker.launch("image/*") } // click to pick
            )


            Spacer(Modifier.height(12.dp))

            Text(
                text = user?.name ?: "Your Name",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = user?.email ?: "email@example.com",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(Modifier.height(20.dp))

            // Edit Button
            Button(
                onClick = { showEditDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Edit, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Edit Profile")
            }

            Spacer(Modifier.height(12.dp))

            // Logout
            OutlinedButton(
                onClick = { vm.logout { navToLogin() } },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Logout", color = Color.Red)
            }
        }

        // Edit Dialog
        if (showEditDialog && user != null) {
            EditProfileDialog(
                initialName = user!!.name,
                initialBio = user!!.bio ?: "",
                onClose = { showEditDialog = false },
                onSave = { name, bio ->
                    vm.updateProfile(uid, name, bio)
                    showEditDialog = false
                }
            )
        }
    }
}
@Composable
fun EditProfileDialog(
    initialName: String,
    initialBio: String,
    onClose: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var bio by remember { mutableStateOf(initialBio) }

    AlertDialog(
        onDismissRequest = onClose,
        title = { Text("Edit Profile") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text("Bio") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(name, bio) }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onClose) {
                Text("Cancel")
            }
        }
    )
}
