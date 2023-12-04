package ch.heigvd.daa.labo4.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ch.heigvd.daa.labo4.App
import ch.heigvd.daa.labo4.databinding.FragmentNoteEditBinding
import ch.heigvd.daa.labo4.models.NoteAndSchedule
import ch.heigvd.daa.labo4.utils.DateUtils.formatDate
import ch.heigvd.daa.labo4.viewmodels.ViewModelNotes
import ch.heigvd.daa.labo4.viewmodels.ViewModelNotesFactory

/**
 * Fragment for editing a selected note.
 * @author Timothée Van Hove, Léo Zmoos
 */
class EditNoteFragment : Fragment() {
    private var _binding: FragmentNoteEditBinding? = null
    private val binding get() = _binding!!

    // ViewModel instance obtained from Activity's ViewModelProvider.
    private val viewModel: ViewModelNotes by activityViewModels {
        ViewModelNotesFactory((requireActivity().application as App).repository, requireActivity().application)
    }

    /**
     * Inflates the layout for this fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Sets up the UI and observes LiveData from the ViewModel.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fetching the note ID passed as an argument.
        val noteId = arguments?.getLong("noteId", -1L) ?: -1L
        var noteAndSchedule: NoteAndSchedule? = null

        // Observing note details from the ViewModel.
        viewModel.getNoteById(noteId).observe(viewLifecycleOwner) { note ->
            noteAndSchedule = note
            note?.let {
                with(binding) {
                    val scheduleDate = it.schedule?.date
                    editTextTitle.setText(it.note.title)
                    editTextContent.setText(it.note.text)
                    editTextDate.textColors

                    // Handling the visibility of date based on the presence of a schedule.
                    if (scheduleDate != null) {
                        editTextDate.setText(scheduleDate?.let { date -> formatDate(date) })
                    } else {
                        editTextDate.visibility = View.GONE
                        textViewDate.visibility = View.GONE
                    }
                }
            }
        }

        // Handling the Edit button click event.
        binding.buttonEdit.setOnClickListener {
            if (noteAndSchedule != null) {
                viewModel.updateNoteAndSchedule(
                    noteId,
                    binding.editTextTitle.text.toString(),
                    binding.editTextContent.text.toString()
                )
            } else {
                Toast.makeText(view.context, "Error editing the note", Toast.LENGTH_LONG).show()
            }
            activity?.supportFragmentManager?.popBackStack()
        }

        // Handling the Cancel button click event.
        binding.buttonCancel.setOnClickListener { activity?.supportFragmentManager?.popBackStack() }
    }

    /**
     * Cleans up resources when the view is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}