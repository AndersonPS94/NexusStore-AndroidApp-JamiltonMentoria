package com.jamiltonmentoria.nexusstore.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

data class ProductResponse(val products: List<ProductDto>)

@Entity(tableName = "products")
data class ProductDto(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val price: Double,
    val discountPercentage: Double,
    val rating: Double,
    val stock: Int,
    val brand: String?,
    val thumbnail: String,
    val images: List<String>,
    val isInCart: Boolean = false,
    val quantityInCart: Int = 0
) : Serializable

data class CartResponse(val carts: List<CartDto>)
data class CartDto(val id: Int, val total: Double, val totalProducts: Int)

data class UserResponse(val users: List<UserDto>)
data class UserDto(val id: Int, val firstName: String, val lastName: String, val image: String) : Serializable

data class PostResponse(val posts: List<PostDto>)

@Entity(tableName = "posts")
data class PostDto(
    @PrimaryKey val id: Int,
    val title: String,
    val body: String,
    val userId: Int,
    val tags: List<String>,
    val reactions: PostReactions?,
    val views: Int,
    val isLiked: Boolean = false,
    val isSaved: Boolean = false,
    val userLikesCount: Int = 0 // Extra field to store local increment
) : Serializable

data class PostReactions(
    val likes: Int,
    val dislikes: Int
) : Serializable
