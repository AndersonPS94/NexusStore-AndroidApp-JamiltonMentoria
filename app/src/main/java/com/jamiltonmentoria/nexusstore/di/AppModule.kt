package com.jamiltonmentoria.nexusstore.di

import com.jamiltonmentoria.nexusstore.data.network.DummyApiService
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Koin module for registering network clients and dependencies.
 */
val appModule = module {
    // Retrofit Instance
    single {
        Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // API Service
    single {
        get<Retrofit>().create(DummyApiService::class.java)
    }

    // Ready for UseCases and ViewModels registration
    // single { MyUseCase(get()) }
    // viewModel { MyViewModel(get()) }
}
