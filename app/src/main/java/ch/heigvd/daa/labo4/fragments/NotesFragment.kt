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

class NotesFragment : Fragment(), OnNoteClickListener {

    private val viewModel: ViewModelNotes by activityViewModels {
        ViewModelNotesFactory((requireActivity().application as App).repository)
    }

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    private lateinit var notesAdapter: NotesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notesAdapter = NotesAdapter(this)
        binding.recyclerViewNotes.apply {
            adapter = notesAdapter
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.sortedNotes.observe(viewLifecycleOwner) { notes ->
            notesAdapter.items = notes
        }
    }

    override fun onNoteClicked(noteAndSchedule: NoteAndSchedule) {
        val editNoteFragment = EditNoteFragment().apply {
            arguments = Bundle().apply {
                noteAndSchedule.note.noteId?.let { putLong("noteId", it) }
            }
        }

        val isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        val containerId = if (isLandscape) {
            R.id.fragment_container_controls
        } else {
            R.id.fragment_container_notes
        }

        // Replace the current fragment with the new one
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(containerId, editNoteFragment)
            ?.addToBackStack(null)
            ?.commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
