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

class ViewModelNotes(private val repository: Repository, context: Context) : ViewModel() {
    private val sharedPreferences = context.getSharedPreferences("NotePreferences", Context.MODE_PRIVATE)

    private val _sortOrder = MutableLiveData<Sort>().apply {
        value = Sort.values()[sharedPreferences.getInt("sortOrder", Sort.NONE.ordinal)]
    }

    private val sortOrder: LiveData<Sort> = _sortOrder

    private var allNotes = repository.allNotes

    val countNotes = repository.countNotes

    private val sortedNotesByDate = allNotes.map { notes ->
        notes.sortedByDescending { note -> note.note.creationDate }
    }

    private val sortedNotesByEta = allNotes.map { notes ->
        notes.sortedWith(compareBy(nullsLast()) { note -> note.schedule?.date })
    }

    val sortedNotes = sortOrder.switchMap {
        when (it) {
            Sort.BY_DATE -> sortedNotesByDate
            Sort.BY_ETA -> sortedNotesByEta
            else -> allNotes
        }
    }

    fun setSortOrder(sortOrder: Sort) {
        _sortOrder.value = sortOrder
        with(sharedPreferences.edit()) {
            putInt("sortOrder", sortOrder.ordinal)
            apply()
        }
    }

    fun generateNote() {
        repository.generateNote()
    }

    fun deleteNotes() {
        repository.deleteNotes()
    }

    fun getNoteById(noteId: Long): LiveData<NoteAndSchedule?> {
        return allNotes.switchMap { notes ->
            val liveData = MutableLiveData<NoteAndSchedule?>()
            val noteAndSchedule = notes.find { it.note.noteId == noteId }
            liveData.value = noteAndSchedule
            liveData
        }
    }

    fun updateNoteAndSchedule(noteId: Long, title: String, text: String) {
        repository.editNote(noteId, title, text)
    }
}

class ViewModelNotesFactory(private val repository: Repository, private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelNotes::class.java)) {
            return ViewModelNotes(repository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}