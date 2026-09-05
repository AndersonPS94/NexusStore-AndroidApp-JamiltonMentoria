package com.jamiltonmentoria.nexusstore.data.remote

import com.jamiltonmentoria.nexusstore.data.model.ProductResponse
import com.jamiltonmentoria.nexusstore.data.model.ProductDto
import com.jamiltonmentoria.nexusstore.data.model.CartResponse
import com.jamiltonmentoria.nexusstore.data.model.UserResponse
import com.jamiltonmentoria.nexusstore.data.model.PostResponse
import retrofit2.Response
import retrofit2.http.GET

interface DummyStoreApi {

    @GET("products")
    suspend fun getProducts(): Response<ProductResponse>

    @GET("products/{id}")
    suspend fun getProductById(@retrofit2.http.Path("id") id: Int): Response<ProductDto>

    @GET("carts")
    suspend fun getCarts(): Response<CartResponse>

    @GET("users")
    suspend fun getUsers(): Response<UserResponse>

    @GET("posts")
    suspend fun getPosts(): Response<PostResponse>
}
