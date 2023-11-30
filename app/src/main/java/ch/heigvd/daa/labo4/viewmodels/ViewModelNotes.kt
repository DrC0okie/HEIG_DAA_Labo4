package ch.heigvd.daa.labo4.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import ch.heigvd.daa.labo4.Sort
import ch.heigvd.daa.labo4.repository.Repository

class ViewModelNotes(private val repository: Repository) : ViewModel() {
    var allNotes = repository.allNotes

    val countNotes = repository.countNotes

    private val sortOrder: LiveData<Sort> = MutableLiveData(Sort.NONE)

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
        (this.sortOrder as MutableLiveData).value = sortOrder
    }

    fun generateNote() {
        repository.generateNote()
    }

    fun deleteNotes() {
        repository.deleteNotes()
    }
}

@Suppress("UNCHECKED_CAST")
class ViewModelNotesFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelNotes::class.java)) {
            return ViewModelNotes(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}