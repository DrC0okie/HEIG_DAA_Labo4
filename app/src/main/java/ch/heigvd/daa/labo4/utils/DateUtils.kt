package ch.heigvd.daa.labo4.utils

import android.content.Context
import ch.heigvd.daa.labo4.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * Utility object for handling date-related operations.
 * @author Timothée Van Hove, Léo Zmoos
 */
object DateUtils {

    /**
     * Calculates the difference between the current date and a due date.
     *
     * @param context The application context for accessing resources.
     * @param dueDate The due date to compare with the current date.
     * @return A Pair of formatted string representing the time difference and a boolean indicating if it is late.
     */
    fun getDateDiff(context: Context, dueDate: Calendar): Pair<String, Boolean> {
        val today = Calendar.getInstance()
        val diffMillis = dueDate.timeInMillis - today.timeInMillis
        val isLate = diffMillis <= 0

        if (isLate) return Pair(context.getString(R.string.schedule_late), true)

        // Calculate differences in various units
        val diffMinutes = TimeUnit.MINUTES.convert(diffMillis, TimeUnit.MILLISECONDS)
        val diffHours = TimeUnit.HOURS.convert(diffMillis, TimeUnit.MILLISECONDS)
        val diffDays = TimeUnit.DAYS.convert(diffMillis, TimeUnit.MILLISECONDS)

        val diffWeeks = diffDays / 7
        val diffMonths = getMonthDifference(today, dueDate)

        // Return the formatted string based on the largest time unit difference
        with(context.resources) {
            return when {
                diffMonths > 0 -> Pair(
                    getQuantityString(
                        R.plurals.schedule_month,
                        diffMonths.toInt(),
                        diffMonths
                    ), false
                )

                diffWeeks > 0 -> Pair(
                    getQuantityString(
                        R.plurals.schedule_week,
                        diffWeeks.toInt(),
                        diffWeeks
                    ), false
                )

                diffDays > 0 -> Pair(
                    getQuantityString(
                        R.plurals.schedule_day,
                        diffDays.toInt(),
                        diffDays
                    ), false
                )

                diffHours > 0 -> Pair(
                    getQuantityString(
                        R.plurals.schedule_hour,
                        diffHours.toInt(),
                        diffHours
                    ), false
                )

                diffMinutes > 0 -> Pair(
                    getQuantityString(
                        R.plurals.schedule_minute,
                        diffMinutes.toInt(),
                        diffMinutes
                    ), false
                )

                else -> Pair(context.getString(R.string.schedule_late), true)
            }
        }
    }

    /**
     * Calculates the difference in months between two Calendar dates.
     *
     * @param startCal The start date.
     * @param endCal The end date.
     * @return The difference in months as a Long.
     */
    private fun getMonthDifference(startCal: Calendar, endCal: Calendar): Long {
        val yearDiff = endCal.get(Calendar.YEAR) - startCal.get(Calendar.YEAR)
        val monthDiff = endCal.get(Calendar.MONTH) - startCal.get(Calendar.MONTH)
        return (yearDiff * 12 + monthDiff).toLong()
    }

    /**
     * Formats a Calendar date into a readable string format.
     *
     * @param calendar The Calendar object to format.
     * @return A string representing the formatted date.
     */
    fun formatDate(calendar: Calendar): String {
        val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return formatter.format(calendar.time)
    }
}
