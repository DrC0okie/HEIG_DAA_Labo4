package ch.heigvd.daa.labo4.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView as IV
import android.widget.TextView as TV
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ch.heigvd.daa.labo4.R
import ch.heigvd.daa.labo4.models.Type
import ch.heigvd.daa.labo4.models.NoteAndSchedule
import ch.heigvd.daa.labo4.models.State
import java.util.Calendar as Cal
import java.util.concurrent.TimeUnit as Tu

class NotesAdapter(_items: List<NoteAndSchedule> = listOf()) :
    RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    var items = listOf<NoteAndSchedule>()
        set(value) {
            val diffCallback = NotesDiffCallback(items, value)
            val diffItems = DiffUtil.calculateDiff(diffCallback)
            field = value
            diffItems.dispatchUpdatesTo(this)
        }

    init {
        items = _items
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        return if (items[position].schedule != null) NOTE_SCHEDULE else NOTE
    }

    companion object {
        private const val NOTE = 0
        private const val NOTE_SCHEDULE = 1
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val iconNote = view.findViewById<IV>(R.id.image_view_icon)
        private val titleNote = view.findViewById<TV>(R.id.text_view_title)
        private val textNote = view.findViewById<TV>(R.id.text_view_text)

        private val iconSchedule = view.findViewById<IV>(R.id.image_view_clock)
        private val textSchedule = view.findViewById<TV>(R.id.text_view_schedule)

        fun bind(noteAndSchedule: NoteAndSchedule) {
            iconNote.setImageResource(
                when (noteAndSchedule.note.type) {
                    Type.TODO -> R.drawable.todo
                    Type.SHOPPING -> R.drawable.shopping
                    Type.WORK -> R.drawable.work
                    Type.FAMILY -> R.drawable.family
                    Type.NONE -> R.drawable.note
                }
            )
            iconNote.imageTintList = when (noteAndSchedule.note.state) {
                State.IN_PROGRESS -> ContextCompat.getColorStateList(iconNote.context, R.color.grey)
                State.DONE -> ContextCompat.getColorStateList(iconNote.context, R.color.green)
            }
            titleNote.text = noteAndSchedule.note.title
            textNote.text = noteAndSchedule.note.text

            if (noteAndSchedule.schedule != null) {
                val (dateText, isLate) = displayDateDifference(
                    itemView.context,
                    noteAndSchedule.schedule.date
                )

                textSchedule.text = dateText
                iconSchedule.imageTintList = when (isLate) {
                    true -> ContextCompat.getColorStateList(iconSchedule.context, R.color.red)
                    false -> ContextCompat.getColorStateList(iconSchedule.context, R.color.grey)
                }
            }
        }

        private fun displayDateDifference(context: Context, dueDate: Cal): Pair<String, Boolean> {
            val today = Cal.getInstance()
            val diff = dueDate.timeInMillis.minus(today.timeInMillis)

            if (diff <= 0) return Pair(context.getString(R.string.schedule_late), true)

            val diffMinutes = Tu.MINUTES.convert(diff, Tu.MILLISECONDS)
            val diffHours = Tu.HOURS.convert(diff, Tu.MILLISECONDS)
            val diffDays = Tu.DAYS.convert(diff, Tu.MILLISECONDS)
            val diffWeeks = diffDays.div(today.getActualMaximum(Cal.DAY_OF_WEEK))
            val diffMonths = diffDays.div(today.getActualMaximum(Cal.DAY_OF_MONTH))

            when {
                diffMonths > 0 ->
                    return Pair(
                        context.resources.getQuantityString(
                            R.plurals.schedule_month,
                            diffMonths.toInt(),
                            diffMonths
                        ), false
                    )
                diffWeeks > 0 ->
                    return Pair(
                        context.resources.getQuantityString(
                            R.plurals.schedule_week,
                            diffWeeks.toInt(),
                            diffWeeks
                        ), false
                    )
                diffDays > 0 ->
                    return Pair(
                        context.resources.getQuantityString(
                            R.plurals.schedule_day,
                            diffDays.toInt(),
                            diffDays
                        ), false
                    )
                diffHours > 0 ->
                    return Pair(
                        context.resources.getQuantityString(
                            R.plurals.schedule_hour,
                            diffHours.toInt(),
                            diffHours
                        ), false
                    )
                diffMinutes > 0 ->
                    return Pair(
                        context.resources.getQuantityString(
                            R.plurals.schedule_minute,
                            diffMinutes.toInt(),
                            diffMinutes
                        ), false
                    )
                else -> return Pair(
                    context.getString(R.string.schedule_late),
                    true
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            NOTE -> ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.note_item_list_view, parent, false)
            )
            else -> ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.note_schedule_item_list_view, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
}
