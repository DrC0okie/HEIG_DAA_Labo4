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

class EditNoteFragment : Fragment() {
    private var _binding: FragmentNoteEditBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ViewModelNotes by activityViewModels {
        ViewModelNotesFactory((requireActivity().application as App).repository, requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val noteId = arguments?.getLong("noteId", -1L) ?: -1L
        var noteAndSchedule: NoteAndSchedule? = null

        // Use ViewModel to fetch data using noteId and scheduleId
        viewModel.getNoteById(noteId).observe(viewLifecycleOwner) { note ->
            noteAndSchedule = note
            note?.let {
                with(binding) {
                    val scheduleDate = it.schedule?.date
                    editTextTitle.setText(it.note.title)
                    editTextContent.setText(it.note.text)
                    editTextDate.textColors

                    if (scheduleDate != null) {
                        editTextDate.setText(scheduleDate?.let { date -> formatDate(date) })
                    } else {
                        editTextDate.visibility = View.GONE
                        textViewDate.visibility = View.GONE
                    }
                }
            }
        }

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

        binding.buttonCancel.setOnClickListener { activity?.supportFragmentManager?.popBackStack() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}