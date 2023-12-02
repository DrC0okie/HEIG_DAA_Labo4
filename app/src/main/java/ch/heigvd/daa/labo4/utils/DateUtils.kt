package ch.heigvd.daa.labo4.utils

import android.content.Context
import ch.heigvd.daa.labo4.R
import java.util.Calendar
import java.util.concurrent.TimeUnit

object DateUtils {
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

    private fun getMonthDifference(startCal: Calendar, endCal: Calendar): Long {
        val yearDiff = endCal.get(Calendar.YEAR) - startCal.get(Calendar.YEAR)
        val monthDiff = endCal.get(Calendar.MONTH) - startCal.get(Calendar.MONTH)
        return (yearDiff * 12 + monthDiff).toLong()
    }
}
