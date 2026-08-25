package com.jamiltonmentoria.nexusstore.data.network

import retrofit2.http.GET

/**
 * Retrofit interface for DummyJSON API endpoints.
 */
interface DummyApiService {

    @GET("products")
    suspend fun getProducts(): Any // Replace Any with actual Response Models later

    @GET("carts")
    suspend fun getCarts(): Any

    @GET("users")
    suspend fun getUsers(): Any

    @GET("posts")
    suspend fun getPosts(): Any
}
