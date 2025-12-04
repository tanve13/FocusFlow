package com.tanveer.focusflow.data.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.tanveer.focusflow.data.model.Note
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotesRepository @Inject constructor(
    private val db: FirebaseFirestore
) {
    private fun notesCollection(uid: String) = db.collection("notes").document(uid).collection("userNotes")

    suspend fun createNote(uid: String, note: Note) {
        val doc = notesCollection(uid).document()
        notesCollection(uid).document(doc.id).set(note.copy(id = doc.id)).await()
    }

    suspend fun updateNote(uid: String, note: Note) {
        notesCollection(uid).document(note.id).set(note).await()
    }

    suspend fun deleteNote(uid: String, noteId: String) {
        notesCollection(uid).document(noteId).delete().await()
    }

    suspend fun getNotes(uid: String): List<Note> =
        notesCollection(uid).get().await().documents.mapNotNull { it.toObject(Note::class.java) }
}
