package ch.heigvd.daa.labo4.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import ch.heigvd.daa.labo4.Sort
import ch.heigvd.daa.labo4.models.NoteAndSchedule
import ch.heigvd.daa.labo4.repository.Repository


/**
 * ViewModel for managing note data and interactions.
 * @author Timothée Van Hove, Léo Zmoos
 * @param repository The repository for data operations.
 * @param context Application context used for shared preferences.
 */
class ViewModelNotes(private val repository: Repository, context: Context) : ViewModel() {
    // Shared Preferences for storing order.
    private val sharedPreferences = context.getSharedPreferences("NotePreferences", Context.MODE_PRIVATE)

    // LiveData for tracking sort order changes.
    private val _sortOrder = MutableLiveData<Sort>().apply {
        value = Sort.values()[sharedPreferences.getInt("sortOrder", Sort.NONE.ordinal)]
    }

    private val sortOrder: LiveData<Sort> = _sortOrder

    // LiveData for all notes.
    private var allNotes = repository.allNotes

    // LiveData for the count of notes.
    val countNotes = repository.countNotes

    // LiveData for notes sorted by creation date.
    private val sortedNotesByDate = allNotes.map { notes ->
        notes.sortedByDescending { note -> note.note.creationDate }
    }

    // LiveData for notes sorted by estimated time of arrival.
    private val sortedNotesByEta = allNotes.map { notes ->
        notes.sortedWith(compareBy(nullsLast()) { note -> note.schedule?.date })
    }

    // LiveData for notes sorted based on current sort order.
    val sortedNotes = sortOrder.switchMap {
        when (it) {
            Sort.BY_DATE -> sortedNotesByDate
            Sort.BY_ETA -> sortedNotesByEta
            else -> allNotes
        }
    }

    /**
     * Sets the current sort order and saves it to shared preferences.
     *
     * @param sortOrder The desired sort order.
     */
    fun setSortOrder(sortOrder: Sort) {
        _sortOrder.value = sortOrder
        with(sharedPreferences.edit()) {
            putInt("sortOrder", sortOrder.ordinal)
            apply()
        }
    }

    // Generates a new note.
    fun generateNote() {
        repository.generateNote()
    }

    // Deletes all notes.
    fun deleteNotes() {
        repository.deleteNotes()
    }

    /**
     * Retrieves a specific note by ID.
     *
     * @param noteId The ID of the note to retrieve.
     * @return LiveData of the requested NoteAndSchedule.
     */
    fun getNoteById(noteId: Long): LiveData<NoteAndSchedule?> {
        return allNotes.switchMap { notes ->
            val liveData = MutableLiveData<NoteAndSchedule?>()
            val noteAndSchedule = notes.find { it.note.noteId == noteId }
            liveData.value = noteAndSchedule
            liveData
        }
    }

    /**
     * Updates a note's title and text.
     *
     * @param noteId The ID of the note to update.
     * @param title The new title of the note.
     * @param text The new text content of the note.
     */
    fun updateNoteAndSchedule(noteId: Long, title: String, text: String) {
        repository.editNote(noteId, title, text)
    }
}

/**
 * Factory for creating ViewModelNotes instances.
 *
 * @param repository The repository instance to be used by the ViewModel.
 * @param context The application context, used for accessing shared preferences.
 */
class ViewModelNotesFactory(private val repository: Repository, private val context: Context) : ViewModelProvider.Factory {

    /**
     * Creates ViewModelNotes instances.
     *
     * @param modelClass The class of the ViewModel to be created.
     * @return An instance of ViewModelNotes.
     * @throws IllegalArgumentException if an unknown ViewModel class is requested.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelNotes::class.java)) {
            return ViewModelNotes(repository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}