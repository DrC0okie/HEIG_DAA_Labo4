package ch.heigvd.daa.labo4.repository

import androidx.lifecycle.LiveData
import ch.heigvd.daa.labo4.models.Note
import ch.heigvd.daa.labo4.models.NoteAndSchedule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class Repository(private val dao: NotesDao, private val applicationScope: CoroutineScope) {
    var allNotes: LiveData<List<NoteAndSchedule>> = dao.getAllNotes()
    val countNotes: LiveData<Int> = dao.getCountNotes()

    fun deleteNotes() {
        applicationScope.launch {
            dao.deleteAllNotes()
        }
    }

    fun generateNote() {
        applicationScope.launch {
            val note = Note.generateRandomNote()
            val schedule = Note.generateRandomSchedule()

            val id = dao.insert(note)
            if (schedule != null) {
                schedule.ownerId = id
                dao.insert(schedule)
            }
        }
    }

    fun editNote(noteId: Long, title: String, text: String) {
        applicationScope.launch {
            dao.updateNote(noteId, title, text)
        }
    }
}