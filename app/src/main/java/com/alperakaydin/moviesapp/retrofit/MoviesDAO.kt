package com.alperakaydin.moviesapp.retrofit

import com.alperakaydin.moviesapp.data.entity.cart.DeleteMovieResponse
import com.alperakaydin.moviesapp.data.entity.cart.GetMovieCartResponse
import com.alperakaydin.moviesapp.data.entity.cart.InsertMovieResponse
import com.alperakaydin.moviesapp.data.entity.movies.GetAllMoviesResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface MoviesDAO {

    @GET("movies/getAllMovies.php")
    suspend fun getAllMovies(): GetAllMoviesResponse

    @POST("movies/getMovieCart.php")
    @FormUrlEncoded
    suspend fun getMovieCart(@Field("userName") userName: String): Response<GetMovieCartResponse>

    @POST("movies/insertMovie.php")
    @FormUrlEncoded
    suspend fun insertMovie(
        @Field("name") name: String,
        @Field("image") image: String,
        @Field("price") price: Int,
        @Field("category") category: String,
        @Field("rating") rating: Double,
        @Field("year") year: Int,
        @Field("director") director: String,
        @Field("description") description: String,
        @Field("orderAmount") orderAmount: Int,
        @Field("userName") userName: String
    ): Response<InsertMovieResponse>


    @POST("movies/deleteMovie.php")
    @FormUrlEncoded
    suspend fun deleteMovie(
        @Field("cartId") cartId: Int,
        @Field("userName") userName: String
    ): Response<DeleteMovieResponse>


    @GET("movies/getAllMovieCart.php")
    suspend fun getAllMovieCart(): Response<GetMovieCartResponse>


}