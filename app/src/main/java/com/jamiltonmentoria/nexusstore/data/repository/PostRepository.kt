package com.jamiltonmentoria.nexusstore.data.repository

import androidx.room.withTransaction
import com.jamiltonmentoria.nexusstore.data.local.dao.PostDao
import com.jamiltonmentoria.nexusstore.data.model.PostDto
import com.jamiltonmentoria.nexusstore.data.model.PostResponse
import com.jamiltonmentoria.nexusstore.data.model.UserResponse
import com.jamiltonmentoria.nexusstore.data.remote.DummyStoreApi
import com.jamiltonmentoria.nexusstore.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val api: DummyStoreApi,
    private val postDao: PostDao
) {
    fun getPosts(): Flow<Resource<PostResponse>> = flow {
        emit(Resource.Loading())
        
        val localData = postDao.getAllPosts().first()
        if (localData.isNotEmpty()) {
            emit(Resource.Success(PostResponse(localData)))
        }

        try {
            val response = api.getPosts()
            if (response.isSuccessful) {
                val remotePosts = response.body()?.posts ?: emptyList()
                
                postDao.deleteAllPosts()
                postDao.insertPosts(remotePosts)
                
                emitAll(postDao.getAllPosts().map { Resource.Success(PostResponse(it)) })
            } else {
                emit(Resource.Error("Erro ao buscar postagens: ${response.message()}"))
            }
        } catch (e: Exception) {
            if (localData.isEmpty()) {
                emit(Resource.Error("Falha na conexão: ${e.message}"))
            }
        }
    }

    fun getUsers(): Flow<Resource<UserResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.getUsers()
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Resource.Success(it))
                } ?: emit(Resource.Error("Usuários não encontrados"))
            } else {
                emit(Resource.Error("Erro ao buscar usuários: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Falha na conexão: ${e.message}"))
        }
    }

    suspend fun updateLikeStatus(postId: Int, liked: Boolean, likesCount: Int) {
        postDao.updateLikeStatus(postId, liked, likesCount)
    }

    suspend fun updateSaveStatus(postId: Int, saved: Boolean) {
        postDao.updateSaveStatus(postId, saved)
    }
}
