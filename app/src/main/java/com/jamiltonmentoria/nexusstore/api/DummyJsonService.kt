package com.jamiltonmentoria.nexusstore.api

import retrofit2.Response
import retrofit2.http.GET

public interface DummyJsonService {

    @GET("products")
    suspend fun showProducts() : Response<List<Products>>
}