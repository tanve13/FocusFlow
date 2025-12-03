package com.tanveer.focusflow.UserInterface.screens.notes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tanveer.focusflow.model.Note
import com.tanveer.focusflow.viewModel.NotesViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun NotesScreen(vm: NotesViewModel = viewModel(), uid: String = "current_uid") {
    val notes by vm.notes.collectAsState()
    LaunchedEffect(Unit) { vm.loadNotes(uid) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Notes", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(12.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(notes) { note ->
                Card(modifier = Modifier.fillMaxWidth().clickable { /* open edit */ }) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(note.title, style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(6.dp))
                        Text(note.content, maxLines = 3, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        Button(onClick = {
            val n = Note(title = "Quick note ${System.currentTimeMillis()}", content = "Write here")
            vm.createNote(uid, n)
        }) { Text("Add Quick Note") }
    }
}
