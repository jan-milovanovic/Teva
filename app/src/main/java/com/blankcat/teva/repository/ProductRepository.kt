package com.blankcat.teva.repository

import com.blankcat.teva.data.dao.ProductDao
import com.blankcat.teva.models.Product
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val productDao: ProductDao) {
    fun getProducts(): Flow<List<Product>> = productDao.getProducts()

    fun deleteProduct(product: Product) = productDao.deleteProduct(product)

    fun insertProduct(product: Product) = productDao.insertProduct(product)

    fun updateProduct(product: Product) = productDao.updateProduct(product)
}