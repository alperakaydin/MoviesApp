package com.alperakaydin.moviesapp.data.entity.cart

data class GetMovieCartResponse(
    val movie_cart : List<CartItem> = emptyList()
) {
}