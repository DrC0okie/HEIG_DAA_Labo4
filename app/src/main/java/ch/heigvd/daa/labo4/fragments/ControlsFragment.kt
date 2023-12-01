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


class ControlsFragment : Fragment() {

    private val viewModel: ViewModelNotes by activityViewModels {
        ViewModelNotesFactory((requireActivity().application as App).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_controls, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val notesCounter = view.findViewById<TextView>(R.id.text_view_notes_counter)
        val buttonGenerate = view.findViewById<TextView>(R.id.button_generate)
        val buttonDelete = view.findViewById<TextView>(R.id.button_delete)

        viewModel.countNotes.observe(viewLifecycleOwner) { count ->
            notesCounter.text = count.toString()
        }

        buttonGenerate.setOnClickListener {
            viewModel.generateNote()
        }

        buttonDelete.setOnClickListener {
            viewModel.deleteNotes()
        }
    }
}