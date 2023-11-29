package ch.heigvd.daa.labo4.repository

import androidx.lifecycle.LiveData
import ch.heigvd.iict.daa.labo4.models.Note
import ch.heigvd.iict.daa.labo4.models.NoteAndSchedule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class Repository(private val dao: Dao, private val applicationScope: CoroutineScope) {
    var allNotes: LiveData<List<NoteAndSchedule>> = dao.getAllNotes()
    val countNotes: LiveData<Int> = dao.getCountNotes()

    fun deleteAllNotes() {
        applicationScope.launch {
            dao.deleteAllNotes()
        }
    }

    fun generateANote() {
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

}