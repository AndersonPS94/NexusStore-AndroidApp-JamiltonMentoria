package com.jamiltonmentoria.nexusstore.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jamiltonmentoria.nexusstore.data.model.ProductDto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<ProductDto>>

    @Query("SELECT * FROM products WHERE id = :id")
    fun getProductById(id: Int): Flow<ProductDto?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductDto>)

    @Query("UPDATE products SET isInCart = :isInCart, quantityInCart = :qty WHERE id = :productId")
    suspend fun updateCartStatus(productId: Int, isInCart: Boolean, qty: Int)

    @Query("DELETE FROM products")
    suspend fun deleteAllProducts()
}
