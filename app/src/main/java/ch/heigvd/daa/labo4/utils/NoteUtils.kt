package ch.heigvd.daa.labo4.utils

import android.content.Context
import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import ch.heigvd.daa.labo4.R
import ch.heigvd.daa.labo4.models.State
import ch.heigvd.daa.labo4.models.Type

/**
 * Utility object for handling note-related UI operations.
 * @author Timothée Van Hove, Léo Zmoos
 */
object NoteUtils {

    /**
     * Returns the appropriate icon resource based on the note type.
     *
     * @param type The type of the note.
     * @return The drawable resource ID of the icon.
     */
    fun getIconResource(type: Type): Int {
        return when (type) {
            Type.TODO -> R.drawable.todo
            Type.SHOPPING -> R.drawable.shopping
            Type.WORK -> R.drawable.work
            Type.FAMILY -> R.drawable.family
            Type.NONE -> R.drawable.note
        }
    }

    /**
     * Determines the color tint for a note based on its state.
     *
     * @param context The application context.
     * @param state The state of the note.
     * @return A ColorStateList corresponding to the state of the note.
     */
    fun getStateTint( context: Context, state: State): ColorStateList? {
        return when (state) {
            State.IN_PROGRESS -> ContextCompat.getColorStateList(context, R.color.grey)
            State.DONE -> ContextCompat.getColorStateList(context, R.color.green)
        }
    }

    /**
     * Determines the color tint for the schedule icon based on its timeliness.
     *
     * @param context The application context.
     * @param isLate Boolean indicating whether the schedule is late.
     * @return A ColorStateList corresponding to the timeliness of the schedule.
     */
    fun getScheduleTint( context: Context, isLate: Boolean): ColorStateList? {
        return when (isLate) {
            true -> ContextCompat.getColorStateList(context, R.color.red)
            false -> ContextCompat.getColorStateList(context, R.color.grey)
        }
    }
}