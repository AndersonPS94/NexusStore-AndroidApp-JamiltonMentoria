package com.jamiltonmentoria.nexusstore.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jamiltonmentoria.nexusstore.data.model.PostDto
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
    @Query("SELECT * FROM posts")
    fun getAllPosts(): Flow<List<PostDto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<PostDto>)

    @Query("DELETE FROM posts")
    suspend fun deleteAllPosts()

    @Query("UPDATE posts SET isLiked = :liked, userLikesCount = :likesCount WHERE id = :postId")
    suspend fun updateLikeStatus(postId: Int, liked: Boolean, likesCount: Int)

    @Query("UPDATE posts SET isSaved = :saved WHERE id = :postId")
    suspend fun updateSaveStatus(postId: Int, saved: Boolean)
}
