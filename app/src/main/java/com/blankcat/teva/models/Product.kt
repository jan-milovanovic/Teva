package com.blankcat.teva.models

import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Duration
import java.time.ZonedDateTime

@Entity(
    tableName = "products",
    indices = [
        Index("title"),
        Index("store"),
        Index("is_checked"),
        Index("repeat"),
        Index("last_purchase")
    ]
)
@Immutable
data class Product(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val title: String = "",

    val store: String = "",

    @ColumnInfo("is_checked")
    val isChecked: Boolean = false,

    val repeat: Repeat,

    @ColumnInfo("last_purchase")
    val lastPurchase: ZonedDateTime,
) {
    val remainingTime: Duration
        get() {
            val timeSeconds =
                ZonedDateTime.now().minusSeconds(lastPurchase.toEpochSecond()).toEpochSecond()
            return repeat.toDuration().minusSeconds(timeSeconds)
        }

    val progressValue: Float
        get() {
            val timeSeconds =
                ZonedDateTime.now().minusSeconds(lastPurchase.toEpochSecond()).toEpochSecond()
            return (timeSeconds.toFloat().div(repeat.toDuration().seconds)).coerceIn(0f, 1f)
        }
}