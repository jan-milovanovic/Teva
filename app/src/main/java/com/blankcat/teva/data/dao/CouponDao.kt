package com.blankcat.teva.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.blankcat.teva.models.TevaBarcode
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CouponDao {

    @Query("SELECT * FROM barcodes WHERE collection == 'Coupon'")
    abstract fun getCoupons(): Flow<List<TevaBarcode>>

    @Insert
    abstract fun insertCoupon(card: TevaBarcode)

    @Delete
    abstract fun deleteCoupon(card: TevaBarcode)

    @Update
    abstract fun updateCoupon(card: TevaBarcode)
}