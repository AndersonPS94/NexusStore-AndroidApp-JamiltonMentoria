package com.jamiltonmentoria.nexusstore.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideDummyStoreApi(retrofit: Retrofit): com.jamiltonmentoria.nexusstore.data.remote.DummyStoreApi {
        return retrofit.create(com.jamiltonmentoria.nexusstore.data.remote.DummyStoreApi::class.java)
    }
}
