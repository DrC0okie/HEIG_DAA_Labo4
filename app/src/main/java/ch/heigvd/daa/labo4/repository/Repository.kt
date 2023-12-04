package ch.heigvd.daa.labo4.repository

import androidx.lifecycle.LiveData
import ch.heigvd.daa.labo4.models.Note
import ch.heigvd.daa.labo4.models.NoteAndSchedule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Repository for managing notes and their schedules.
 * Provides a clean API for data access to the rest of the application.
 * @author Timothée Van Hove, Léo Zmoos
 * @param dao The Data Access Object for interacting with the database.
 * @param applicationScope The CoroutineScope for performing database operations.
 */
class Repository(private val dao: NotesDao, private val applicationScope: CoroutineScope) {
    // LiveData list of all notes and schedules.
    var allNotes: LiveData<List<NoteAndSchedule>> = dao.getAllNotes()

    // LiveData for the count of notes in the database.
    val countNotes: LiveData<Int> = dao.getCountNotes()

    /**
     * Deletes all notes from the database.
     * Performs the operation within a coroutine scope.
     */
    fun deleteNotes() {
        applicationScope.launch {
            dao.deleteAllNotes()
        }
    }

    /**
     * Generates and inserts a new random note into the database.
     * If a schedule is also generated, it is associated with the note.
     */
    fun generateNote() {
        applicationScope.launch {
            val note = Note.generateRandomNote()
            val schedule = Note.generateRandomSchedule()

            val id = dao.insert(note)
            schedule?.let {
                it.ownerId = id
                dao.insert(it)
            }
        }
    }

    /**
     * Updates an existing note's title and text in the database.
     *
     * @param noteId The ID of the note to be updated.
     * @param title The new title for the note.
     * @param text The new text content of the note.
     */
    fun editNote(noteId: Long, title: String, text: String) {
        applicationScope.launch {
            dao.updateNote(noteId, title, text)
        }
    }
}
