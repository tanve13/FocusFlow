package com.tanveer.focusflow.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tanveer.focusflow.data.firestore.NotesRepository
import com.tanveer.focusflow.data.model.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val repo: NotesRepository
) : ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    fun loadNotes(uid: String) {
        viewModelScope.launch {
            _notes.value = repo.getNotes(uid)
        }
    }

    fun createNote(uid: String, note: Note) {
        viewModelScope.launch {
            repo.createNote(uid, note)
            loadNotes(uid)
        }
    }

    fun updateNote(uid: String, note: Note) {
        viewModelScope.launch {
            repo.updateNote(uid, note)
            loadNotes(uid)
        }
    }

    fun deleteNote(uid: String, noteId: String) {
        viewModelScope.launch {
            repo.deleteNote(uid, noteId)
            loadNotes(uid)
        }
    }
}
