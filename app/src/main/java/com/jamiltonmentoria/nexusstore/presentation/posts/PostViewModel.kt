package com.jamiltonmentoria.nexusstore.presentation.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jamiltonmentoria.nexusstore.data.model.PostResponse
import com.jamiltonmentoria.nexusstore.data.model.UserDto
import com.jamiltonmentoria.nexusstore.data.repository.PostRepository
import com.jamiltonmentoria.nexusstore.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: PostRepository
) : ViewModel() {

    private val _posts = MutableStateFlow<Resource<PostResponse>>(Resource.Loading())
    val posts: StateFlow<Resource<PostResponse>> = _posts

    private val _usersMap = MutableStateFlow<Map<Int, UserDto>>(emptyMap())
    val usersMap: StateFlow<Map<Int, UserDto>> = _usersMap

    init {
        getPosts()
        getUsers()
    }

    fun getPosts() {
        viewModelScope.launch {
            repository.getPosts().collect {
                _posts.value = it
            }
        }
    }

    private fun getUsers() {
        viewModelScope.launch {
            repository.getUsers().collect { resource ->
                if (resource is Resource.Success) {
                    val map = resource.data?.users?.associateBy { it.id } ?: emptyMap()
                    _usersMap.value = map
                }
            }
        }
    }

    fun updateLike(postId: Int, liked: Boolean, likesCount: Int) {
        viewModelScope.launch {
            repository.updateLikeStatus(postId, liked, likesCount)
        }
    }

    fun updateSave(postId: Int, saved: Boolean) {
        viewModelScope.launch {
            repository.updateSaveStatus(postId, saved)
        }
    }
}
