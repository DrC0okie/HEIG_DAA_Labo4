package ch.heigvd.daa.labo4

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import ch.heigvd.daa.labo4.models.Note
import ch.heigvd.daa.labo4.models.Schedule
import ch.heigvd.daa.labo4.repository.Dao
import kotlin.concurrent.thread


@Database(entities = [Note::class, Schedule::class], version = 1, exportSchema = true)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): Dao

    companion object {
        private var DB: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return DB ?: synchronized(this) {
                DB = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "mydatabase.db"
                ).fallbackToDestructiveMigration().addCallback(this.callback).build()

                DB!!
            }
        }

        private val callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                DB?.let { database ->
                    val isEmpty = database.dao().getCountDirect() == 0L
                    if (isEmpty) {
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
                    }
                }
            }
        }
    }
}