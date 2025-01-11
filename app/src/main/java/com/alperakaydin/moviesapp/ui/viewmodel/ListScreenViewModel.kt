package com.alperakaydin.moviesapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alperakaydin.moviesapp.data.entity.movies.Movie
import com.alperakaydin.moviesapp.data.repo.MovieRepository
import com.alperakaydin.moviesapp.shared.viewmodel.SharedCartViewModel
import com.alperakaydin.moviesapp.shared.viewmodel.SharedMovieViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListScreenViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    var sharedCartViewModel: SharedCartViewModel,
    var sharedMovieViewModel: SharedMovieViewModel
) : ViewModel() {


    val cartItems = sharedCartViewModel.cartItems
    val favoriteMovies = sharedMovieViewModel.favoriteMovies


    val loading  = sharedCartViewModel.isUpdatingCart

    suspend fun updateFavoriteMovies() {
        sharedMovieViewModel.updateFavoriteMovies()
    }

    fun updateCartItems() {
        //sharedCartViewModel.loadCartItems()
        CoroutineScope(Dispatchers.Main).launch {
            sharedCartViewModel.loadCartItems()
        }
    }
    suspend fun toggleFavorite(movie: Movie) {
        sharedMovieViewModel.toggleFavorite(movie)
    }

    fun addToCart(movie: Movie) {
        sharedCartViewModel.addToCart(movie)
    }

    fun decreaseFromCart(movie: Movie) {
        sharedCartViewModel.decreaseFromCart(movie)

    }

}