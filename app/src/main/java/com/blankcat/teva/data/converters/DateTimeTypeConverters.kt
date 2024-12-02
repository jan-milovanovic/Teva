package com.blankcat.teva.data.converters

import androidx.room.TypeConverter
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

object DateTimeTypeConverters {

    @TypeConverter
    fun fromTimestamp(value: Long): ZonedDateTime {
        return value.let { Instant.ofEpochSecond(value).atZone(ZoneId.systemDefault()) }
    }

    @TypeConverter
    fun dateToTimestamp(zonedDateTime: ZonedDateTime): Long {
        return zonedDateTime.toEpochSecond()
    }
}