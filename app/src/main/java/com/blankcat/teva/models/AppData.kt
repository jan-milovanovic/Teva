package com.blankcat.teva.models

import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "app_data",
    indices = [
        Index("is_dark_theme")
    ]
)

@Immutable
data class AppData(

    @PrimaryKey
    val id: Int = 0,

    @ColumnInfo("is_dark_theme")
    val isDarkTheme: Boolean = false
)