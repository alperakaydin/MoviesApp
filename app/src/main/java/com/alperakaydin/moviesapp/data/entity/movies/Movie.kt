package com.alperakaydin.moviesapp.data.entity.movies

import com.alperakaydin.moviesapp.data.entity.cart.InsertMovieRequest

data class Movie(
    val id: Int,
    val name: String,
    val image : String,
    val price: Int,
    val category: String,
    val rating: Double,
    val year : Int,
    val director: String,
    val description: String,
    val isFavorite: Boolean = false,
    val trailerUrl: String = "https://www.youtube.com/watch?v=mqqft2x_Aa4"
    ) {

}

fun Movie.toInsertMovieRequest(userName: String, orderAmount: Int): InsertMovieRequest {
    return InsertMovieRequest(
        name = this.name,
        image = this.image,
        price = this.price,
        category = this.category,
        rating = this.rating,
        year = this.year,
        director = this.director,
        description = this.description,
        orderAmount = orderAmount,
        userName = userName
    )
}

