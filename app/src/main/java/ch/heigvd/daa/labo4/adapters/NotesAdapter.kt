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
import ch.heigvd.daa.labo4.utils.DateUtils
import ch.heigvd.daa.labo4.utils.NoteUtils

class NotesAdapter(listItems: List<NoteAndSchedule> = listOf()) :
    RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    private val differ = AsyncListDiffer(this, NotesDiffCallback())

    // Holds the current list of items displayed by the adapter.
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position].schedule != null) NOTE_SCHEDULE else NOTE
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(private val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        /**
         * Binds a NoteAndSchedule object to the ViewHolder.
         *
         * @param noteAndSchedule The NoteAndSchedule object to bind to the ViewHolder.
         */
        fun bind(noteAndSchedule: NoteAndSchedule) {
            when (binding) {
                is N_ViewBinding -> bindNote(binding, noteAndSchedule)
                is NS_ViewBinding -> bindNoteSchedule(binding, noteAndSchedule)
            }
        }

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

        override fun areContentsTheSame(oldItem: NoteAndSchedule, newItem: NoteAndSchedule): Boolean {
            // Since Note and Schedule are data classes, a simple equality check is sufficient.
            // This will compare all properties of the Note and Schedule objects.
            return oldItem == newItem
        }
    }
}
