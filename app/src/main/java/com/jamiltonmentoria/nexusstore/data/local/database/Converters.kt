package com.jamiltonmentoria.nexusstore.data.local.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jamiltonmentoria.nexusstore.data.model.PostReactions

class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromPostReactions(value: PostReactions?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toPostReactions(value: String): PostReactions? {
        return Gson().fromJson(value, PostReactions::class.java)
    }
}
