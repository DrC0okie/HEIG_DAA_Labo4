package ch.heigvd.daa.labo4

import android.app.Application
import ch.heigvd.daa.labo4.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class App:Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    val repository by lazy {
        val database = NotesDatabase.getDatabase(this)
        Repository(database.dao(), applicationScope)
    }
}