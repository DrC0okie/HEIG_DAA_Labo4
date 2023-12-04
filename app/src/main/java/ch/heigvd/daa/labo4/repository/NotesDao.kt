package ch.heigvd.daa.labo4.repository

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ch.heigvd.daa.labo4.models.Note
import ch.heigvd.daa.labo4.models.NoteAndSchedule
import ch.heigvd.daa.labo4.models.Schedule

/**
 * Data Access Object (DAO) for managing notes and schedules.
 * Provides methods to interact with the database.
 * @author Timothée Van Hove, Léo Zmoos
 */
@Dao
interface NotesDao {
    /**
     * Retrieves all notes and their associated schedules.
     *
     * @return LiveData containing a list of notes and schedules.
     */
    @Transaction
    @Query("SELECT * FROM note")
    fun getAllNotes(): LiveData<List<NoteAndSchedule>>

    /**
     * Deletes all notes from the database.
     */
    @Query("DELETE FROM note")
    fun deleteAllNotes()

    /**
     * Counts the total number of notes.
     *
     * @return LiveData containing the count of notes.
     */
    @Query("SELECT COUNT(*) FROM note")
    fun getCountNotes(): LiveData<Int>

    /**
     * Counts the total number of notes.
     *
     * @return The count of notes as a Long value.
     */
    @Query("SELECT COUNT(*) FROM note")
    fun getCountDirect() : Long

    /**
     * Inserts a new note into the database.
     *
     * @param note The note to be inserted.
     * @return The row ID of the newly inserted note.
     */
    @Insert
    fun insert(note: Note): Long

    /**
     * Inserts a new schedule into the database.
     *
     * @param schedule The schedule to be inserted.
     * @return The row ID of the newly inserted schedule.
     */
    @Insert
    fun insert(schedule: Schedule): Long

    /**
     * Updates an existing note in the database.
     *
     * @param noteId The ID of the note to update.
     * @param title The new title for the note.
     * @param text The new text content of the note.
     */
    @Query("UPDATE note SET title = :title, text = :text WHERE noteId = :noteId")
    fun updateNote(noteId: Long, title: String, text: String)
}
