package ch.heigvd.daa.labo4.utils

import android.content.Context
import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import ch.heigvd.daa.labo4.R
import ch.heigvd.daa.labo4.models.State
import ch.heigvd.daa.labo4.models.Type

object NoteUtils {
    fun getIconResource(type: Type): Int {
        return when (type) {
            Type.TODO -> R.drawable.todo
            Type.SHOPPING -> R.drawable.shopping
            Type.WORK -> R.drawable.work
            Type.FAMILY -> R.drawable.family
            Type.NONE -> R.drawable.note
        }
    }

    fun getStateTint( context: Context, state: State): ColorStateList? {
        return when (state) {
            State.IN_PROGRESS -> ContextCompat.getColorStateList(context, R.color.grey)
            State.DONE -> ContextCompat.getColorStateList(context, R.color.green)
        }
    }

    fun getScheduleTint( context: Context, isLate: Boolean): ColorStateList? {
        return when (isLate) {
            true -> ContextCompat.getColorStateList(context, R.color.red)
            false -> ContextCompat.getColorStateList(context, R.color.grey)
        }
    }
}