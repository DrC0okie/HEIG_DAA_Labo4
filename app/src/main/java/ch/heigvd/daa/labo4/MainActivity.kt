package ch.heigvd.daa.labo4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import ch.heigvd.daa.labo4.databinding.ActivityMainBinding
import ch.heigvd.daa.labo4.viewmodels.ViewModelNotes
import ch.heigvd.daa.labo4.viewmodels.ViewModelNotesFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: ViewModelNotes by viewModels {
        ViewModelNotesFactory((application as MyApp).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

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