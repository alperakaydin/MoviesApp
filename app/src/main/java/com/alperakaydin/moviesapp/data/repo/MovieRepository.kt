package com.alperakaydin.moviesapp.data.repo

import com.alperakaydin.moviesapp.data.datasource.AppPref
import com.alperakaydin.moviesapp.data.datasource.MovieDataSource
import com.alperakaydin.moviesapp.data.datasource.UserSessionManager
import com.alperakaydin.moviesapp.data.entity.cart.GetMovieCartRequest
import com.alperakaydin.moviesapp.data.entity.movies.Movie

class MovieRepository(
    var movieDataSource: MovieDataSource,
    var cartRepository: CartRepository,
    var userSessionManager: UserSessionManager,
    var appPref: AppPref,
) {


    suspend fun moviesLoad(): List<Movie> = movieDataSource.getAllMovies().movies

    private val trailerUrls = mapOf(
        "Batman" to "https://www.youtube.com/watch?v=mqqft2x_Aa4",
        "Interstellar" to "https://www.youtube.com/watch?v=zSWdZVtXT7E",
        "Catsby" to "https://www.youtube.com/watch?v=CatsbyTrailer",
        "12 Monkeys" to "https://www.youtube.com/watch?v=12MonkeysTrailer"
    )

    fun getTrailerUrl(movieName: String): String? {
        return trailerUrls[movieName]
    }

    suspend fun getMovieByName(movieName: String): Movie? {
        return movieDataSource.getAllMovies().movies.find { it.name == movieName }

    }

    suspend fun getMovieById(movieId: Int): Movie? {
        return movieDataSource.getAllMovies().movies.find { it.id == movieId }

    }

    // Recommendation Functions
    suspend fun getAllRecommendations(): List<Movie> {
        val userName = userSessionManager.getUsername()
        val favorites = appPref.readFavorites().map { it.toInt() }
        val cartItems = cartRepository.getMovieCart(GetMovieCartRequest(userName))

        val favoriteCategories = favorites.mapNotNull { movieId ->
            getMovieById(movieId)?.category
        }

        val cartCategories = cartItems.map { it.category }
        val allCategories = (favoriteCategories + cartCategories).distinct()

        return moviesLoad()
            .filter { it.category in allCategories && it.id !in favorites }
            .sortedByDescending { it.rating }
            .take(10)
    }

    // Recommendation Functions
    suspend fun getCartRecommendations(): List<Movie> {

        val userName = userSessionManager.getUsername()
        val cartItems = cartRepository.getMovieCart(GetMovieCartRequest(userName))


        val cartCategories = cartItems.map { it.category }
        val allCategories = cartCategories.distinct()

        return moviesLoad()
            .filter { it.category in allCategories }
            .sortedByDescending { it.rating }
            .take(5)
    }


    // Favori Functions
    suspend fun getFavorites(): Set<String> {
        return appPref.readFavorites()
    }

    suspend fun addFavoriteId(id: Int) {
        val _id = id.toString()
        val favs = getFavorites().toHashSet()
        favs.add(_id)
        appPref.deleteFavorites()
        appPref.saveFavorites(favs)
    }

    suspend fun removeFavoriteId(id: Int) {
        val _id = id.toString()
        val favs = getFavorites().toHashSet()
        favs.remove(_id)
        appPref.deleteFavorites()
        appPref.saveFavorites(favs)
    }

    suspend fun isFavorite(id: Int): Boolean {
        val _id = id.toString()
        val favs = getFavorites().toHashSet()
        return favs.contains(_id)

    }

}