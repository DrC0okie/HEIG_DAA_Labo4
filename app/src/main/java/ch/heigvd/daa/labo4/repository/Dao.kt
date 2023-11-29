package ch.heigvd.daa.labo4.repository

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.Query
import ch.heigvd.iict.daa.labo4.models.Note
import ch.heigvd.iict.daa.labo4.models.NoteAndSchedule
import ch.heigvd.iict.daa.labo4.models.Schedule

interface Dao {
    @Query("SELECT * FROM note")
    fun getAllNotes(): LiveData<List<NoteAndSchedule>>

    @Query("DELETE FROM note")
    fun deleteAllNotes()

    @Query("SELECT COUNT(*) FROM note")
    fun getCountNotes(): LiveData<Int>

    @Insert
    fun insert(note: Note): Long

    @Insert
    fun insert(schedule: Schedule): Long
}