package com.blankcat.teva.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.blankcat.teva.data.converters.DateTimeTypeConverters
import com.blankcat.teva.data.converters.TevaCollectionTypeConverter
import com.blankcat.teva.data.dao.AppDao
import com.blankcat.teva.data.dao.CardDao
import com.blankcat.teva.data.dao.CartNoteDao
import com.blankcat.teva.data.dao.CouponDao
import com.blankcat.teva.data.dao.ProductDao
import com.blankcat.teva.data.dao.TransactionRunnerDao
import com.blankcat.teva.data.dao.UserDao
import com.blankcat.teva.models.AppData
import com.blankcat.teva.models.TevaBarcode
import com.blankcat.teva.models.CartNote
import com.blankcat.teva.models.Product
import com.blankcat.teva.models.User

@Database(
    entities = [
        Product::class,
        CartNote::class,
        User::class,
        TevaBarcode::class,
        AppData::class,
    ],
    version = 1,
    exportSchema = false
)

@TypeConverters(DateTimeTypeConverters::class, TevaCollectionTypeConverter::class)
abstract class TevaDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun cartNoteDao(): CartNoteDao
    abstract fun transactionRunnerDao(): TransactionRunnerDao
    abstract fun userDao(): UserDao
    abstract fun cardDao(): CardDao
    abstract fun couponDao(): CouponDao
    abstract fun appDao(): AppDao
}