package com.blankcat.teva.models

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "cart_note",
    indices = [
        Index("text")
    ]
)

@Immutable
data class CartNote(

    @PrimaryKey
    val id: Int = 0,

    val text: String? = null
)