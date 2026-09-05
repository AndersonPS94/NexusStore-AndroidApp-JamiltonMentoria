package com.jamiltonmentoria.nexusstore.data.repository

import com.jamiltonmentoria.nexusstore.data.local.dao.ProductDao
import com.jamiltonmentoria.nexusstore.data.model.ProductDto
import com.jamiltonmentoria.nexusstore.data.model.ProductResponse
import com.jamiltonmentoria.nexusstore.data.remote.DummyStoreApi
import com.jamiltonmentoria.nexusstore.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val api: DummyStoreApi,
    private val productDao: ProductDao
) {
    suspend fun getProducts(): Flow<Resource<ProductResponse>> = flow {
        emit(Resource.Loading())
        
        val localData = productDao.getAllProducts().first()
        if (localData.isNotEmpty()) {
            emit(Resource.Success(ProductResponse(localData)))
        }

        try {
            val response = api.getProducts()
            if (response.isSuccessful) {
                val remoteProducts = response.body()?.products ?: emptyList()
                
                val currentProducts = localData.associateBy { it.id }
                val productsToInsert = remoteProducts.map { remote ->
                    val local = currentProducts[remote.id]
                    if (local != null) {
                        remote.copy(isInCart = local.isInCart, quantityInCart = local.quantityInCart)
                    } else {
                        remote
                    }
                }
                
                productDao.deleteAllProducts()
                productDao.insertProducts(productsToInsert)
                
                emitAll(productDao.getAllProducts().map { Resource.Success(ProductResponse(it)) })
            } else {
                emit(Resource.Error("Erro ao buscar produtos: ${response.message()}"))
            }
        } catch (e: Exception) {
            if (localData.isEmpty()) {
                emit(Resource.Error("Falha na conexão: ${e.message}"))
            }
        }
    }

    fun getProductById(id: Int): Flow<Resource<ProductDto>> = productDao.getProductById(id).map {
        if (it != null) Resource.Success(it) else Resource.Error("Produto não encontrado")
    }

    suspend fun updateCartStatus(productId: Int, isInCart: Boolean, qty: Int) {
        productDao.updateCartStatus(productId, isInCart, qty)
    }
}
