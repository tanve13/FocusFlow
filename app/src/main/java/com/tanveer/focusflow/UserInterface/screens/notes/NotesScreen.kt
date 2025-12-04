package com.tanveer.focusflow.UserInterface.screens.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tanveer.focusflow.data.model.Note
import com.tanveer.focusflow.viewModel.NotesViewModel

@Composable
fun NotesScreen(
    vm: NotesViewModel = hiltViewModel(),
    uid: String = "current_uid",
    onEditNote: (Note) -> Unit = {}
) {
    val notes by vm.notes.collectAsState()
    LaunchedEffect(Unit) { vm.loadNotes(uid) }

    var showDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(notes) { note ->
                NoteCard(
                    note = note,
                    onEdit = { onEditNote(note) },
                    onDelete = { vm.deleteNote(uid, note.id) }
                )
            }
        }

        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
        ) { Text("+") }

        if (showDialog) {
            InputDialog(
                title = "Add New Note",
                placeholder = "Enter note title",
                onConfirm = { text ->
                    if (text.isNotBlank()) {
                        val n = Note(title = text, content = "")
                        vm.createNote(uid, n)
                    }
                    showDialog = false
                },
                onDismiss = { showDialog = false }
            )
        }
    }
}

@Composable
fun InputDialog(
    title: String,
    placeholder: String,
    initialText: String = "",
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var text by remember { mutableStateOf(initialText) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(onClick = { onConfirm(text) }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) { Text("Cancel") }
        },
        title = { Text(title) },
        text = {
            TextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text(placeholder) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 6
            )
        }
    )
}


@Composable
fun NoteCard(note: Note, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEdit() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(note.title, style = MaterialTheme.typography.titleMedium)
                Row {
                    IconButton(onClick = { onEdit() }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Note")
                    }
                    IconButton(onClick = { onDelete() }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Note", tint = Color.Red)
                    }
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(note.content, style = MaterialTheme.typography.bodyMedium, maxLines = 4)
        }
    }
}
