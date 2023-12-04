package ch.heigvd.daa.labo4

import android.app.Application
import ch.heigvd.daa.labo4.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

/**
 * Application class for the notes app.
 * Initializes and provides the application-wide dependencies.
 * @author Timothée Van Hove, Léo Zmoos
 */
class App : Application() {
    // Define a CoroutineScope for the whole application.
    private val applicationScope = CoroutineScope(SupervisorJob())

    // Lazy initialization of the repository.
    // Ensures that the repository is created only when needed and only once.
    val repository by lazy {
        // Create an instance of the database.
        val database = NotesDatabase.getDatabase(this)

        // Initialize the repository with the DAO and application CoroutineScope.
        Repository(database.dao(), applicationScope)
    }
}