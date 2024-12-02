package com.blankcat.teva.models

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [
        Index("email")
    ]
)
@Immutable
data class User(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val email: String,

    val password: String?,
)