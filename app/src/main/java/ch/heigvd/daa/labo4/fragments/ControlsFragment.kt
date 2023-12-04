package ch.heigvd.daa.labo4.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ch.heigvd.daa.labo4.App
import ch.heigvd.daa.labo4.R
import ch.heigvd.daa.labo4.viewmodels.ViewModelNotes
import ch.heigvd.daa.labo4.viewmodels.ViewModelNotesFactory

/**
 * Fragment for providing control actions like generating or deleting notes.
 * @author Timothée Van Hove, Léo Zmoos
 */
class ControlsFragment : Fragment() {

    // ViewModel instance obtained from Activity's ViewModelProvider.
    private val viewModel: ViewModelNotes by activityViewModels {
        ViewModelNotesFactory((requireActivity().application as App).repository, requireActivity().application)
    }

    /**
     * Inflates the layout for this fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_controls, container, false)
    }

    /**
     * Sets up UI components and event listeners.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fetching UI components by their ID.
        val notesCounter = view.findViewById<TextView>(R.id.text_view_notes_counter)
        val buttonGenerate = view.findViewById<TextView>(R.id.button_generate)
        val buttonDelete = view.findViewById<TextView>(R.id.button_delete)

        // Observing the count of notes and updating the UI.
        viewModel.countNotes.observe(viewLifecycleOwner) { count ->
            notesCounter.text = count.toString()
        }

        // Handling the click event for generating a new note.
        buttonGenerate.setOnClickListener {
            viewModel.generateNote()
        }

        // Handling the click event for deleting all notes.
        buttonDelete.setOnClickListener {
            viewModel.deleteNotes()
        }
    }
}