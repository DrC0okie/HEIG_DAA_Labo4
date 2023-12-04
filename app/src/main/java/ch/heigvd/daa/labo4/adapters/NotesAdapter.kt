package ch.heigvd.daa.labo4.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import ch.heigvd.daa.labo4.databinding.NoteItemListViewBinding as N_ViewBinding
import ch.heigvd.daa.labo4.databinding.NoteScheduleItemListViewBinding as NS_ViewBinding
import ch.heigvd.daa.labo4.models.NoteAndSchedule
import ch.heigvd.daa.labo4.models.OnNoteClickListener
import ch.heigvd.daa.labo4.utils.DateUtils
import ch.heigvd.daa.labo4.utils.NoteUtils

/**
 * Adapter for RecyclerView to display a list of notes and their schedules.
 * @author Timothée Van Hove, Léo Zmoos
 * @property clickListener Listener for handling click events on items.
 * @property listItems Initial list of notes and schedules to display.
 */
class NotesAdapter(
    private val clickListener: OnNoteClickListener,
    listItems: List<NoteAndSchedule> = listOf()
) :
    RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    // Utilizes AsyncListDiffer for efficient updates of RecyclerView items.
    private val differ = AsyncListDiffer(this, NotesDiffCallback())

    // Public accessor to get current list and update list items.
    var items = listOf<NoteAndSchedule>()
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
            field = value
        }

    init {
        items = listItems
    }

    companion object {
        private const val NOTE = 0
        private const val NOTE_SCHEDULE = 1
    }

    // Inflates appropriate view layout based on the item type.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        return if (viewType == NOTE) {
            val binding = N_ViewBinding.inflate(LayoutInflater.from(context), parent, false)
            ViewHolder(binding)
        } else {
            val binding = NS_ViewBinding.inflate(LayoutInflater.from(context), parent, false)
            ViewHolder(binding)
        }
    }

    // Binds data to the ViewHolder.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    // Determines the type of view to display based on whether a schedule exists.
    override fun getItemViewType(position: Int): Int {
        return if (items[position].schedule != null) NOTE_SCHEDULE else NOTE
    }

    override fun getItemCount() = items.size

    /**
     * ViewHolder class for binding note or note and schedule data to a view.
     *
     * @param binding The binding for the inflated view.
     */
    inner class ViewHolder(private val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    clickListener.onNoteClicked(items[position])
                }
            }
        }

        // Binds the data to the view depending on its type.
        fun bind(noteAndSchedule: NoteAndSchedule) {
            when (binding) {
                is N_ViewBinding -> bindNote(binding, noteAndSchedule)
                is NS_ViewBinding -> bindNoteSchedule(binding, noteAndSchedule)
            }
        }

        // Binds note data to the view.
        private fun bindNote(binding: N_ViewBinding, noteAndSchedule: NoteAndSchedule) {
            val context = itemView.context
            binding.apply {
                with(noteAndSchedule) {
                    textViewTitle.text = note.title
                    textViewText.text = note.text
                    imageViewIcon.setImageResource(NoteUtils.getIconResource(note.type))
                    imageViewIcon.imageTintList = NoteUtils.getStateTint(context, note.state)
                }
            }
        }

        // Binds note and schedule data to the view.
        private fun bindNoteSchedule(binding: NS_ViewBinding, noteAndSchedule: NoteAndSchedule) {
            val context = itemView.context
            binding.apply {
                with(noteAndSchedule) {
                    val (dateText, isLate) = DateUtils.getDateDiff(context, schedule!!.date)
                    textViewTitle.text = note.title
                    textViewText.text = note.text
                    imageViewIcon.setImageResource(NoteUtils.getIconResource(note.type))
                    imageViewIcon.imageTintList = NoteUtils.getStateTint(context, note.state)
                    textViewSchedule.text = dateText
                    imageViewClock.imageTintList = NoteUtils.getScheduleTint(context, isLate)
                }
            }
        }
    }

    /**
     * DiffUtil Callback for calculating the difference between two lists of NoteAndSchedule objects.
     */
    private class NotesDiffCallback : DiffUtil.ItemCallback<NoteAndSchedule>() {

        override fun areItemsTheSame(oldItem: NoteAndSchedule, newItem: NoteAndSchedule): Boolean {
            // Comparing the unique IDs of the notes to check if they are the same item.
            return oldItem.note.noteId == newItem.note.noteId
        }

        override fun areContentsTheSame(
            oldItem: NoteAndSchedule,
            newItem: NoteAndSchedule
        ): Boolean {
            // Since Note and Schedule are data classes, a simple equality check is sufficient.
            // This will compare all properties of the Note and Schedule objects.
            return oldItem == newItem
        }
    }
}
