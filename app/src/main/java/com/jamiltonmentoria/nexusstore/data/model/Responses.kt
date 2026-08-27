package com.jamiltonmentoria.nexusstore.data.model

data class ProductResponse(val products: List<ProductDto>)
data class ProductDto(val id: Int, val title: String, val price: Double, val thumbnail: String)

data class CartResponse(val carts: List<CartDto>)
data class CartDto(val id: Int, val total: Double, val totalProducts: Int)

data class UserResponse(val users: List<UserDto>)
data class UserDto(val id: Int, val firstName: String, val lastName: String, val image: String)

data class PostResponse(val posts: List<PostDto>)
data class PostDto(val id: Int, val title: String, val body: String)
