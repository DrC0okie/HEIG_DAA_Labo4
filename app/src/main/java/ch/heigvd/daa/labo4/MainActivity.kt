package ch.heigvd.daa.labo4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import ch.heigvd.daa.labo4.databinding.ActivityMainBinding
import ch.heigvd.daa.labo4.viewmodels.ViewModelNotes
import ch.heigvd.daa.labo4.viewmodels.ViewModelNotesFactory

/**
 * Main activity for the notes application.
 * This activity is the entry point of the application and handles the user interface.
 * @author Timothée Van Hove, Léo Zmoos
 */
class MainActivity : AppCompatActivity() {

    // Binding object instance with access to the views in the activity_main layout.
    private lateinit var binding: ActivityMainBinding

    // ViewModel instance obtained from Activity's ViewModelProvider.
    private val viewModel: ViewModelNotes by viewModels {
        ViewModelNotesFactory((application as App).repository, applicationContext)
    }

    /**
     * Initializes the activity.
     *
     * @param savedInstanceState contains the data supplied in onSaveInstanceState.Otherwise, null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout XML file and return a binding object instance.
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    /**
     * Initializes the contents of the Activity's standard options menu.
     *
     * @param menu The options menu in which items are placed.
     * @return True for the menu to be displayed; if return false, it will not be shown.
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    /**
     * Handles action bar item clicks.
     *
     * @param item The menu item that was selected.
     * @return false to allow normal menu processing to proceed, true to consume it here.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var ret = true
        when (item.itemId) {
            R.id.creation_date_filter -> viewModel.setSortOrder(Sort.BY_DATE)
            R.id.eta_filter -> viewModel.setSortOrder(Sort.BY_ETA)
            R.id.menu_actions_generate -> viewModel.generateNote()
            R.id.menu_actions_delete_all -> viewModel.deleteNotes()
            else -> ret = super.onOptionsItemSelected(item)
        }
        return ret
    }
}