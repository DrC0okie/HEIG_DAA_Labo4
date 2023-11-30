package ch.heigvd.daa.labo4

import androidx.room.TypeConverter
import java.util.*

class DateConverter {
    @TypeConverter
    fun toCalendar(dateLong: Long): Calendar =
        Calendar.getInstance().apply {
            time = Date(dateLong)
        }

    @TypeConverter
    fun fromCalendar(date: Calendar): Long = date.time.time
}