package com.alperakaydin.moviesapp.data.datasource

import com.alperakaydin.moviesapp.data.entity.cart.DeleteMovieRequest
import com.alperakaydin.moviesapp.data.entity.cart.DeleteMovieResponse
import com.alperakaydin.moviesapp.data.entity.cart.GetMovieCartRequest
import com.alperakaydin.moviesapp.data.entity.cart.GetMovieCartResponse
import com.alperakaydin.moviesapp.data.entity.cart.InsertMovieRequest
import com.alperakaydin.moviesapp.data.entity.cart.InsertMovieResponse
import com.alperakaydin.moviesapp.data.entity.movies.GetAllMoviesResponse
import com.alperakaydin.moviesapp.retrofit.MoviesDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class MovieDataSource(var moviesDAO: MoviesDAO) {

    suspend fun getAllMovies(): GetAllMoviesResponse = withContext(Dispatchers.IO) {

        return@withContext moviesDAO.getAllMovies()
    }

    suspend fun getMovieCart(getMovieCartRequest: GetMovieCartRequest): Response<GetMovieCartResponse> =
        withContext(Dispatchers.IO) {

            return@withContext moviesDAO.getMovieCart(getMovieCartRequest.userName)
        }

    suspend fun getAllMovieCart(): Response<GetMovieCartResponse> {

        return moviesDAO.getAllMovieCart()
    }

    suspend fun insertMovie(insertMovieRequest: InsertMovieRequest): Response<InsertMovieResponse> {

        return moviesDAO.insertMovie(
            insertMovieRequest.name,
            insertMovieRequest.image,
            insertMovieRequest.price,
            insertMovieRequest.category,
            insertMovieRequest.rating,
            insertMovieRequest.year,
            insertMovieRequest.director,
            insertMovieRequest.description,
            insertMovieRequest.orderAmount,
            insertMovieRequest.userName
        )
    }

    suspend fun deleteMovie(deleteMovieRequest: DeleteMovieRequest): Response<DeleteMovieResponse> {

        return moviesDAO.deleteMovie(
            deleteMovieRequest.cartId,
            deleteMovieRequest.userName
        )
    }


}

