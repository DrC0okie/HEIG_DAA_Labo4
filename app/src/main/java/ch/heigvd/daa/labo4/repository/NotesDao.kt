package ch.heigvd.daa.labo4.repository

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ch.heigvd.daa.labo4.models.Note
import ch.heigvd.daa.labo4.models.NoteAndSchedule
import ch.heigvd.daa.labo4.models.Schedule

@Dao
interface NotesDao {
    @Transaction
    @Query("SELECT * FROM note")
    fun getAllNotes(): LiveData<List<NoteAndSchedule>>

    @Query("DELETE FROM note")
    fun deleteAllNotes()

    @Query("SELECT COUNT(*) FROM note")
    fun getCountNotes(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM note")
    fun getCountDirect() : Long

    @Insert
    fun insert(note: Note): Long

    @Insert
    fun insert(schedule: Schedule): Long
}