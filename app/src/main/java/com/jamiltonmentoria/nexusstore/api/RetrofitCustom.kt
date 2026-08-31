package com.jamiltonmentoria.nexusstore.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitCustom {

    fun showDummyJson() : DummyJsonService {
        return Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DummyJsonService::class.java)
    }
}