package com.alperakaydin.moviesapp.data.entity.cart

import com.alperakaydin.moviesapp.data.entity.movies.Movie

data class CartItem(
    val cartId: Int,
    val name: String,
    val image: String,
    val price: Int,
    val category: String,
    val rating: Double,
    val year: Int,
    val director: String,
    val description: String,
    val orderAmount: Int,
    val userName: String
)

