package com.alperakaydin.moviesapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alperakaydin.moviesapp.data.entity.cart.CartItem
import com.alperakaydin.moviesapp.data.entity.movies.Movie
import com.alperakaydin.moviesapp.data.repo.MovieRepository
import com.alperakaydin.moviesapp.shared.viewmodel.SharedCartViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    val sharedCartViewModel: SharedCartViewModel,
    val movieRepository: MovieRepository
) : ViewModel() {

    val cart: StateFlow<List<CartItem>> = sharedCartViewModel.cartItems
    val totalCartPrice = sharedCartViewModel.totalCartPrice
    val recomandations = MutableStateFlow<List<Movie>>(emptyList())

    init {
        sharedCartViewModel.loadCartItems()
        getRecommendations()
    }

    fun getRecommendations() {
        viewModelScope.launch {
            val recommendations = movieRepository.getCartRecommendations()
            recomandations.value = recommendations
        }
    }

    fun deleteMovie(cartId: Int) {
        sharedCartViewModel.deleteMovie(cartId)
        //sharedCartViewModel.loadCartItems()

    }

    fun increaseToMovieCountInCart(movieName: String) {
        viewModelScope.launch {
            val movie = movieRepository.getMovieByName(movieName)
            sharedCartViewModel.addToCart(movie!!)
        }
    }

    fun decreaseToMovieCountInCart(movieName: String) {
        viewModelScope.launch {
            val movie = movieRepository.getMovieByName(movieName)
            sharedCartViewModel.decreaseFromCart(movie!!)
        }
    }

    fun clearCart() {
        sharedCartViewModel.clearCart()
    }


    fun loadCartItems() {
        sharedCartViewModel.loadCartItems()
    }


}