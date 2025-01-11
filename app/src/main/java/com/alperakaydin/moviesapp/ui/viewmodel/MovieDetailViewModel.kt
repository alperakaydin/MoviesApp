package com.alperakaydin.moviesapp.ui.viewmodel

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alperakaydin.moviesapp.data.entity.movies.Movie
import com.alperakaydin.moviesapp.data.repo.CartRepository
import com.alperakaydin.moviesapp.data.repo.MovieRepository
import com.alperakaydin.moviesapp.shared.viewmodel.SharedCartViewModel
import com.alperakaydin.moviesapp.shared.viewmodel.SharedMovieViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    var sharedCartViewModel: SharedCartViewModel,
    var sharedMovieViewModel: SharedMovieViewModel, var cartRepository: CartRepository,
    var movieRepository: MovieRepository
) : ViewModel() {


    val isUpdating = sharedCartViewModel.isUpdatingCart

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    private val _userCount = MutableStateFlow(0)
    val userCount: StateFlow<Int> = _userCount.asStateFlow()

    private val _trailerUrl = MutableLiveData<String?>()
    val trailerUrl: LiveData<String?> get() = _trailerUrl

    fun loadTrailer(movie: Movie) {
        _trailerUrl.value = movieRepository.getTrailerUrl(movie.name)
    }

    fun extractVideoId(trailerUrl: String?): String? {
        if (trailerUrl.isNullOrEmpty()) return null

        return try {
            val uri = Uri.parse(trailerUrl)
            uri.getQueryParameter("v")
        } catch (e: Exception) {
            Log.e("extractVideoId", "Hata: ${e.message}")
            null
        }
    }


    fun fetchUserCountForMovie(movieName: String) {
        viewModelScope.launch {
            _userCount.value = cartRepository.getUserCountForMovie(movieName)
        }
    }

    fun toggleFavorite(movie: Movie) {
        CoroutineScope(Dispatchers.Main).launch {
            sharedMovieViewModel.toggleFavorite(movie)
            _isFavorite.value = sharedMovieViewModel.isFavorite(movie)
        }
    }

    suspend fun isFavoriteUpdate(movie: Movie) {
        _isFavorite.value = sharedMovieViewModel.isFavorite(movie)

    }

    fun addToCart(movie: Movie) {
        sharedCartViewModel.addToCart(movie)
    }

    fun getShareIntent(movie: Movie): Intent {
        return Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Movie Share")
            putExtra(
                Intent.EXTRA_TEXT,
                "I like this movie: ${movie.name}\n Details: ${movie.description}"
            )
        }
    }
}