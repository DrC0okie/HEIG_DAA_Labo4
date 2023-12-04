package ch.heigvd.daa.labo4.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ch.heigvd.daa.labo4.App
import ch.heigvd.daa.labo4.R
import ch.heigvd.daa.labo4.adapters.NotesAdapter
import ch.heigvd.daa.labo4.databinding.FragmentNotesBinding
import ch.heigvd.daa.labo4.models.NoteAndSchedule
import ch.heigvd.daa.labo4.models.OnNoteClickListener
import ch.heigvd.daa.labo4.viewmodels.ViewModelNotes
import ch.heigvd.daa.labo4.viewmodels.ViewModelNotesFactory

/**
 * Fragment for displaying a list of notes and their schedules.
 * @author Timothée Van Hove, Léo Zmoos
 */
class NotesFragment : Fragment(), OnNoteClickListener {

    // ViewModel instance obtained from Activity's ViewModelProvider.
    private val viewModel: ViewModelNotes by activityViewModels {
        ViewModelNotesFactory((requireActivity().application as App).repository, requireActivity().application)
    }

    // Binding variable to access the layout's views.
    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    // Adapter for the RecyclerView.
    private lateinit var notesAdapter: NotesAdapter

    //Inflates the layout for this fragment.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    //Sets up the RecyclerView and observes LiveData from the ViewModel.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the adapter and set it to the RecyclerView.
        notesAdapter = NotesAdapter(this)
        binding.recyclerViewNotes.apply {
            adapter = notesAdapter
            layoutManager = LinearLayoutManager(context)
        }

        // Observes changes in sorted notes and updates the adapter.
        viewModel.sortedNotes.observe(viewLifecycleOwner) { notes ->
            notesAdapter.items = notes
        }
    }

    /**
     * Handles note item click events.
     *
     * @param noteAndSchedule The selected note and its schedule.
     */
    override fun onNoteClicked(noteAndSchedule: NoteAndSchedule) {

        // Prepare the fragment for editing the selected note.
        val editNoteFragment = EditNoteFragment().apply {
            arguments = Bundle().apply {
                noteAndSchedule.note.noteId?.let { putLong("noteId", it) }
            }
        }

        // Determine the container to replace based on the current orientation.
        val isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        val containerId = if (isLandscape) {
            R.id.fragment_container_controls
        } else {
            R.id.fragment_container_notes
        }

        // Perform the fragment transaction.
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(containerId, editNoteFragment)
            ?.addToBackStack(null)
            ?.commit()
    }

    /**
     * Cleans up resources when the view is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
