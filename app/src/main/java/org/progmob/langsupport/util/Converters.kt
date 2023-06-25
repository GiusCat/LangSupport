package org.progmob.langsupport.util

import androidx.room.TypeConverter
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromString(value: String?): List<String>? {
        return value?.split(",")
    }

    @TypeConverter
    fun listToString(list: List<String>?): String? {
        return list?.joinToString(",")
    }
}
