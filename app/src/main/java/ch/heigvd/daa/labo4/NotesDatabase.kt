package ch.heigvd.daa.labo4

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import ch.heigvd.daa.labo4.models.Note
import ch.heigvd.daa.labo4.models.Schedule
import ch.heigvd.daa.labo4.repository.NotesDao
import kotlin.concurrent.thread

/**
 * Room database for storing notes and their schedules.
 * @author Timothée Van Hove, Léo Zmoos
 * @Database Annotation to denote a Room database.
 * @TypeConverters Annotation to specify custom type converters.
 */
@Database(entities = [Note::class, Schedule::class], version = 1, exportSchema = true)
@TypeConverters(DateConverter::class)
abstract class NotesDatabase : RoomDatabase() {
    // Abstract method to get the DAO for interacting with the database.
    abstract fun dao(): NotesDao

    companion object {
        // Singleton instance of the database.
        private var INSTANCE: NotesDatabase? = null

        /**
         * Gets the singleton instance of the database.
         *
         * @param context The application context.
         * @return The singleton instance of the NotesDatabase.
         */
        fun getDatabase(context: Context): NotesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotesDatabase::class.java, "mydatabase.db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(databaseCallback)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // Callback for populating the database when it is first created.
        private val databaseCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Populate the database in the background thread.
                INSTANCE?.let { database ->
                    thread {
                        populateDatabase(database.dao())
                    }
                }
            }
        }

        /**
         * Populates the database with initial data if it's empty.
         *
         * @param dao The Data Access Object (DAO) used for database operations.
         */
        private fun populateDatabase(dao: NotesDao) {
            val isEmpty = dao.getCountDirect() == 0L
            if (isEmpty) {
                // Generate sample notes and schedules.
                repeat(10) {
                    val note = Note.generateRandomNote()
                    val id = dao.insert(note)

                    Note.generateRandomSchedule()?.let { schedule ->
                        schedule.ownerId = id
                        dao.insert(schedule)
                    }
                }
            }
        }
    }
}
