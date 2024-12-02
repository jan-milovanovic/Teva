package com.blankcat.teva.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.blankcat.teva.models.CartNote
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CartNoteDao : BaseDao<CartNote> {

    @Query("SELECT * FROM cart_note WHERE id = 0 LIMIT 1")
    abstract fun getNote(): Flow<CartNote?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertNote(cartNote: CartNote)
}