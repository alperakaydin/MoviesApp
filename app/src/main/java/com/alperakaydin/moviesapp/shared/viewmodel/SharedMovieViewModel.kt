package com.alperakaydin.moviesapp.shared.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alperakaydin.moviesapp.data.entity.movies.Movie
import com.alperakaydin.moviesapp.data.repo.MovieRepository
import kotlinx.coroutines.launch
import javax.inject.Inject


class SharedMovieViewModel @Inject constructor(
    var movieRepository: MovieRepository
) : ViewModel() {

    private val _favoriteMovies = MutableLiveData<List<Movie>>()
    val favoriteMovies: LiveData<List<Movie>> get() = _favoriteMovies

    init {
        viewModelScope.launch {
            updateFavoriteMovies()
        }
    }

    suspend fun toggleFavorite(movie: Movie) {

        if (movieRepository.isFavorite(movie.id)) {
            movieRepository.removeFavoriteId(movie.id)
        } else {
            movieRepository.addFavoriteId(movie.id)
        }
        updateFavoriteMovies()

    }

    suspend fun updateFavoriteMovies() {

        val favoriteIds = movieRepository.getFavorites()
        val allMovies = movieRepository.moviesLoad()
        _favoriteMovies.value = allMovies.filter { favoriteIds.contains(it.id.toString()) }

        Log.e("FavTest", "SharedViewModel - Favori Filmler: ${_favoriteMovies.value}")
    }


    suspend fun isFavorite(movie: Movie): Boolean {

        updateFavoriteMovies()
        return _favoriteMovies.value?.any { it.id == movie.id } ?: false


    }


}
