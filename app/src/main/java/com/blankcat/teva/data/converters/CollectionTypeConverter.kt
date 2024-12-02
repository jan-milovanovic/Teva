package com.blankcat.teva.data.converters

import androidx.room.TypeConverter
import com.blankcat.teva.models.TevaCollection

class TevaCollectionTypeConverter {

    @TypeConverter
    fun fromCollection(collection: TevaCollection) = collection.name

    @TypeConverter
    fun toCollection(value: String) = enumValueOf<TevaCollection>(value)
}