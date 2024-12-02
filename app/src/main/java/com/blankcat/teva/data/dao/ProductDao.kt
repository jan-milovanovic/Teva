package com.blankcat.teva.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.blankcat.teva.models.Product
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ProductDao : BaseDao<Product> {
    @Query("SELECT * FROM products")
    abstract fun getProducts(): Flow<List<Product>>

    @Insert
    abstract fun insertProduct(product: Product)

    @Delete
    abstract fun deleteProduct(product: Product)

    @Update
    abstract fun updateProduct(product: Product)
}