package com.alperakaydin.moviesapp.shared.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.alperakaydin.moviesapp.data.entity.cart.CartItem
import com.alperakaydin.moviesapp.data.entity.cart.DeleteMovieRequest
import com.alperakaydin.moviesapp.data.entity.cart.GetMovieCartRequest
import com.alperakaydin.moviesapp.data.entity.movies.Movie
import com.alperakaydin.moviesapp.data.repo.CartRepository
import com.alperakaydin.moviesapp.data.repo.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedCartViewModel @Inject constructor(
    var movieRepository: MovieRepository, var cartRepository: CartRepository
) : ViewModel() {

    private val _cartItems: MutableStateFlow<List<CartItem>> = MutableStateFlow(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _totalCartPrice = MutableStateFlow(0)
    val totalCartPrice: StateFlow<Int> = _totalCartPrice

    private val _isUpdatingCart = MutableStateFlow(false)
    val isUpdatingCart: StateFlow<Boolean> = _isUpdatingCart.asStateFlow()


    init {
        loadCartItems()
    }

    fun loadCartItems() {
        CoroutineScope(Dispatchers.Main).launch {
            _cartItems.value =
                cartRepository.getMovieCart(GetMovieCartRequest(userName = "")).sortedBy { it.name }

            calculateTotalCartPrice()
        }
    }

    private fun calculateTotalCartPrice() {
        val totalPrice = _cartItems.value.sumOf { it.price * it.orderAmount }
        _totalCartPrice.value = totalPrice
    }

    fun clearCart() {
        CoroutineScope(Dispatchers.Main).launch {
            _cartItems.value.forEach {
                cartRepository.deleteMovie(DeleteMovieRequest(it.cartId, ""))
                loadCartItems()
            }
            _cartItems.value = emptyList()
            calculateTotalCartPrice()

        }

    }


    fun addToCart(movie: Movie) {
        _isUpdatingCart.value = true
        Log.e("TTest",
            "SharedCartViewModel - 333333: ${_cartItems.value.size} - ${movie.name} - " + "${cartItems.value.find { it.name == movie.name }?.cartId ?: "boş"} ${cartItems.value.find { it.name == movie.name }?.name ?: "boş"} "
        )
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val currentItems = _cartItems.value.toMutableList()
                val existingItem = currentItems.find { it.name == movie.name }

                if (existingItem != null) {
                    // Mevcut öğenin miktarını artır
                    val updatedAmount = existingItem.orderAmount + 1
                    cartRepository.addOrUpdateMovieInCart(movie, updatedAmount)
                } else {
                    // Yeni öğeyi sepete ekle
                    cartRepository.insertMovie(movie, 1)
                }
                Log.e("TTest",
                    "SharedCartViewModel - 444444: ${_cartItems.value.size} - ${movie.name} - " + "${cartItems.value.find { it.name == movie.name }?.cartId ?: "boş"} ${cartItems.value.find { it.name == movie.name }?.name ?: "boş"} "
                )
                loadCartItems()
            } catch (e: Exception) {
                Log.e("SharedCartViewModel", "Error adding to cart: ${e.message}")
            } finally {
                //loadCartItems()
                _cartItems.value = cartRepository.getMovieCart(GetMovieCartRequest(userName = ""))
                    .sortedBy { it.name }

                calculateTotalCartPrice()
                _isUpdatingCart.value = false

            }
        }
    }

    // Sepetteki filmin miktarını azaltır
    fun decreaseFromCart(movie: Movie) {
        _isUpdatingCart.value = true
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val currentItems = _cartItems.value.toMutableList()
                val existingItem = currentItems.find { it.name == movie.name }

                if (existingItem != null && existingItem.orderAmount > 1) {
                    // Miktarı azalt
                    val updatedAmount = existingItem.orderAmount - 1
                    cartRepository.addOrUpdateMovieInCart(movie, updatedAmount)
                } else if (existingItem != null) {
                    // Miktar 1 ise tamamen kaldır
                    cartRepository.deleteMovie(DeleteMovieRequest(existingItem.cartId, ""))
                }
                loadCartItems()
            } catch (e: Exception) {
                Log.e("SharedCartViewModel", "Error decreasing from cart: ${e.message}")
            } finally {
                _isUpdatingCart.value = false
            }
        }
    }

    fun deleteMovie(cartId: Int) {
        _isUpdatingCart.value = true
        CoroutineScope(Dispatchers.Main).launch {
            try {
                //loadCartItems()
                val response = cartRepository.deleteMovie(DeleteMovieRequest(cartId, ""))
                if (response.success == 1) {
                    _cartItems.value = _cartItems.value.filter { it.cartId != cartId }
                    loadCartItems()
                    Log.e("CartTest", response.message)
                } else {
                    Log.e("CartTest", response.message)
                }


            } catch (e: Exception) {
                Log.e("CartTest", "Error deleting movie: ${e.message}")
            } finally {
                _isUpdatingCart.value = false
            }
        }
    }
}