package com.jamiltonmentoria.nexusstore.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jamiltonmentoria.nexusstore.data.local.dao.PostDao
import com.jamiltonmentoria.nexusstore.data.local.dao.ProductDao
import com.jamiltonmentoria.nexusstore.data.model.PostDto
import com.jamiltonmentoria.nexusstore.data.model.ProductDto

@Database(entities = [PostDto::class, ProductDto::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class NexusDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun productDao(): ProductDao
}
