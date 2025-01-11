package com.alperakaydin.moviesapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alperakaydin.moviesapp.data.entity.cart.CartItem
import com.alperakaydin.moviesapp.data.entity.movies.Movie
import com.alperakaydin.moviesapp.data.repo.MovieRepository
import com.alperakaydin.moviesapp.shared.viewmodel.SharedCartViewModel
import com.alperakaydin.moviesapp.shared.viewmodel.SharedMovieViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val sharedMovieViewModel: SharedMovieViewModel,
    private val sharedCartViewModel: SharedCartViewModel
) : ViewModel() {

    val recommendations = MutableLiveData<List<Movie>>()
    val cart: StateFlow<List<CartItem>> = sharedCartViewModel.cartItems

    val favoriteMovies: LiveData<List<Movie>> = sharedMovieViewModel.favoriteMovies
    val movieList = MutableLiveData<List<Movie>>()

    val filteredMovies = MutableLiveData<List<Movie>>()
    val sortOptions = listOf("Rating", "Yıl", "Fiyat")

    val categories = MediatorLiveData<List<String>>().apply {
        addSource(movieList) { movies ->
            value = movies?.map { it.category }?.distinct()?.sorted()
        }
    }

    val _query = MutableLiveData<String>("")
    val _sortBy = MutableLiveData<String>("Rating")
    val _category = MutableLiveData<String?>()


    init {
        moviesLoad()
    }

    fun getRecommendations() {
        CoroutineScope(Dispatchers.Main).launch {
            recommendations.value = movieRepository.getAllRecommendations()
        }
    }


    fun updateFavoriteMovies() {
        CoroutineScope(Dispatchers.Main).launch {
            sharedMovieViewModel.updateFavoriteMovies()
        }
    }


    fun moviesLoad() {
        CoroutineScope(Dispatchers.Main).launch {
            val movies = movieRepository.moviesLoad()
            movieList.value = movies
            applyFiltersAndSorting(movies)
        }
    }


    private fun applyFiltersAndSorting(movies: List<Movie>) {
        val filtered = movies.filter { movie ->
            (_query.value.isNullOrEmpty() || movie.name.contains(
                _query.value!!,
                ignoreCase = true
            )) &&
                    (_category.value == null || movie.category == _category.value)
        }

        val sorted = when (_sortBy.value) {
            "Fiyat" -> filtered.sortedBy { it.price }
            "Yıl" -> filtered.sortedByDescending { it.year }
            "Rating" -> filtered.sortedByDescending { it.rating }
            else -> filtered
        }

        filteredMovies.value = sorted
    }


}
