package com.blankcat.teva.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.blankcat.teva.models.TevaBarcode
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CardDao {

    @Query("SELECT * FROM barcodes WHERE collection == 'Card'")
    abstract fun getCards(): Flow<List<TevaBarcode>>

    @Insert
    abstract fun insertCard(tevaBarcode: TevaBarcode)

    @Delete
    abstract fun deleteCard(tevaBarcode: TevaBarcode)

    @Update
    abstract fun updateCard(tevaBarcode: TevaBarcode)
}