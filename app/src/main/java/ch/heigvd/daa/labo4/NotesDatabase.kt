package ch.heigvd.daa.labo4

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import ch.heigvd.daa.labo4.models.Note
import ch.heigvd.daa.labo4.models.Schedule
import ch.heigvd.daa.labo4.repository.NotesDao
import kotlin.concurrent.thread

@Database(entities = [Note::class, Schedule::class], version = 1, exportSchema = true)
@TypeConverters(DateConverter::class)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun dao(): NotesDao

    companion object {
        private var DB: NotesDatabase? = null
        fun getDatabase(context: Context): NotesDatabase {
            return DB ?: synchronized(this) {
                DB = Room.databaseBuilder(
                    context.applicationContext,
                    NotesDatabase::class.java, "mydatabase.db"
                ).fallbackToDestructiveMigration().addCallback(this.callback).build()

                DB!!
            }
        }

        private val callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                DB?.let { database ->
//                    val isEmpty = database.dao().getCountDirect() == 0L
//                    if (isEmpty) {
                        thread {
                            for (i in 0..10) {
                                val note = Note.generateRandomNote()
                                val id = database.dao().insert(note)

                                val schedule = Note.generateRandomSchedule()
                                if (schedule != null) {
                                    schedule.ownerId = id
                                    database.dao().insert(schedule)
                                }
                            }
                        }
                    //}
                }
            }
        }
    }
}