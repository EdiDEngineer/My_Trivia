package edu.utap.mytrivia.data.local.converter

import android.text.SpannableString
import androidx.room.TypeConverter
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time

    @TypeConverter
    fun stringToSpannableString(value: String?): SpannableString? =
        value?.let { SpannableString.valueOf(it) }

    @TypeConverter
    fun spannableStringToString(value: SpannableString?): String? = value?.toString()

}