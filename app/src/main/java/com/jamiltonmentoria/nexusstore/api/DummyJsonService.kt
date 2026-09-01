package com.jamiltonmentoria.nexusstore.api

import com.jamiltonmentoria.nexusstore.data.model.ProductResponse
import retrofit2.Response
import retrofit2.http.GET

public interface DummyJsonService {

    @GET("products")
    suspend fun showProducts() : Response<ProductResponse>
}