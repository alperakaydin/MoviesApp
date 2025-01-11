package com.alperakaydin.moviesapp.data.repo

import android.util.Log
import com.alperakaydin.moviesapp.data.datasource.AppPref
import com.alperakaydin.moviesapp.data.datasource.MovieDataSource
import com.alperakaydin.moviesapp.data.datasource.UserSessionManager
import com.alperakaydin.moviesapp.data.entity.cart.CartItem
import com.alperakaydin.moviesapp.data.entity.cart.DeleteMovieRequest
import com.alperakaydin.moviesapp.data.entity.cart.DeleteMovieResponse
import com.alperakaydin.moviesapp.data.entity.cart.GetMovieCartRequest
import com.alperakaydin.moviesapp.data.entity.cart.InsertMovieResponse
import com.alperakaydin.moviesapp.data.entity.movies.Movie
import com.alperakaydin.moviesapp.data.entity.movies.toInsertMovieRequest

class CartRepository(
    var movieDataSource: MovieDataSource, var userSessionManager: UserSessionManager,
    var appPref: AppPref
) {


    suspend fun getMovieCart(getMovieCartRequest: GetMovieCartRequest): List<CartItem> {
        return try {
            val userName = userSessionManager.getUsername()
            getMovieCartRequest.userName = userName

            val response = movieDataSource.getMovieCart(getMovieCartRequest)

            if (response.isSuccessful) {
                val responseBody = response.body()

                if (responseBody != null) {
                    responseBody.movie_cart
                } else {
                    emptyList<CartItem>()
                }
            } else {
                // Hata gövdesini logla
                val errorBody = response.errorBody()?.string()
                Log.e("CartTest", "Error: ${errorBody ?: "Unknown error"}")
                emptyList<CartItem>()
            }
        } catch (e: Exception) {
            Log.e("CartTest", "Exception - getMovieCart: ${e.message}")
            emptyList<CartItem>()
        }
    }


    suspend fun insertMovie(
        movie: Movie,
        orderAmount: Int
    ): InsertMovieResponse {
        val userName = userSessionManager.getUsername()
        val insertMovieRequest = movie.toInsertMovieRequest(userName, orderAmount)
        val insertMovieResponse = movieDataSource.insertMovie(insertMovieRequest)

        if (insertMovieResponse.isSuccessful) {
            Log.e("MovieRepository", "Response body: ${insertMovieResponse.body().toString()}")
            return insertMovieResponse.body() ?: throw Exception("Response body is null")
        } else {
            throw Exception("Error: ${insertMovieResponse.errorBody()?.string()}")
        }


    }

    suspend fun addOrUpdateMovieInCart(movie: Movie, newOrderAmount: Int) {

        try {
            val userName = userSessionManager.getUsername()
            val getMovieCartRequest = GetMovieCartRequest(userName)
            val cartItems = getMovieCart(getMovieCartRequest)

            // Sepette mevcut öğeyi bul
            val existingItem = cartItems.find { it.name == movie.name }

            if (existingItem != null) {
                // Mevcut öğeyi sil
                deleteMovie(DeleteMovieRequest(existingItem.cartId, userName))
            }

            // Yeni miktar ile öğeyi ekle
            if (newOrderAmount > 0) {
                insertMovie(movie, newOrderAmount)
            }

        } catch (e: Exception) {
            Log.e("MovieRepository", "Error adding or updating movie in cart: ${e.message}")
        }
    }

    suspend fun deleteMovie(deleteMovieRequest: DeleteMovieRequest): DeleteMovieResponse {
        val userName = userSessionManager.getUsername()
        deleteMovieRequest.userName = userName

        val deleteMovieResponse = movieDataSource.deleteMovie(deleteMovieRequest)

        if (deleteMovieResponse.isSuccessful && deleteMovieResponse.body()?.success == 1) {
            return DeleteMovieResponse(
                1,
                "Successfully deleted movie CartID: ${deleteMovieRequest.cartId}"
            )
        } else {
            return DeleteMovieResponse(
                0,
                "Failed to delete movie  CartID: ${deleteMovieRequest.cartId}"
            )
        }
    }

    suspend fun getAllCartItems(): List<CartItem> {
        return try {
            val response = movieDataSource.getAllMovieCart()
            if (response.isSuccessful) {
                response.body()?.movie_cart ?: emptyList()
            } else {
                Log.e("CartRepository", "Error: ${response.errorBody()?.string()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("CartRepository", "Exception: ${e.message}")
            emptyList()
        }
    }

    suspend fun getUserCountForMovie(movieName: String): Int {
        val allCartItems = getAllCartItems()
        return allCartItems.count { it.name == movieName }
    }
}