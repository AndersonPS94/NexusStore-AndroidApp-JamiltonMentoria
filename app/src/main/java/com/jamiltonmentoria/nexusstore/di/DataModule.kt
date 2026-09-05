package com.jamiltonmentoria.nexusstore.di

import android.content.Context
import androidx.room.Room
import com.jamiltonmentoria.nexusstore.data.local.dao.PostDao
import com.jamiltonmentoria.nexusstore.data.local.dao.ProductDao
import com.jamiltonmentoria.nexusstore.data.local.database.NexusDatabase
import com.jamiltonmentoria.nexusstore.data.remote.DummyStoreApi
import com.jamiltonmentoria.nexusstore.data.repository.ProductRepository
import com.jamiltonmentoria.nexusstore.data.repository.PostRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideNexusDatabase(@ApplicationContext context: Context): NexusDatabase {
        return Room.databaseBuilder(
            context,
            NexusDatabase::class.java,
            "nexus_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun providePostDao(db: NexusDatabase): PostDao {
        return db.postDao()
    }

    @Provides
    @Singleton
    fun provideProductDao(db: NexusDatabase): ProductDao {
        return db.productDao()
    }

    @Provides
    @Singleton
    fun provideProductRepository(api: DummyStoreApi, productDao: ProductDao): ProductRepository {
        return ProductRepository(api, productDao)
    }

    @Provides
    @Singleton
    fun providePostRepository(api: DummyStoreApi, postDao: PostDao): PostRepository {
        return PostRepository(api, postDao)
    }
}
