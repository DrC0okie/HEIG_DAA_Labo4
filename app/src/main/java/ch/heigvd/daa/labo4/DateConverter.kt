package ch.heigvd.daa.labo4

import androidx.room.TypeConverter
import java.util.*

/**
 * TypeConverter class for Room to handle Calendar type.
 * Provides methods to convert Calendar objects to long for storing in the Room database.
 * @author Timothée Van Hove, Léo Zmoos
 */
class DateConverter {

    /**
     * Converts a long value to a Calendar object.
     *
     * @param dateLong The date in milliseconds.
     * @return The corresponding Calendar object.
     */
    @TypeConverter
    fun toCalendar(dateLong: Long): Calendar =
        Calendar.getInstance().apply {
            time = Date(dateLong)
        }

    /**
     * Converts a Calendar object to a long value.
     *
     * @param date The Calendar object to convert.
     * @return The date in milliseconds.
     */
    @TypeConverter
    fun fromCalendar(date: Calendar): Long = date.time.time
}