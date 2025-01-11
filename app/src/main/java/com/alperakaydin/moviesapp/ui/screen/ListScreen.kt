package com.alperakaydin.moviesapp.ui.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.alperakaydin.moviesapp.data.entity.movies.Movie
import com.alperakaydin.moviesapp.ui.components.MovieDetailCard
import com.alperakaydin.moviesapp.ui.theme.CustomBackground
import com.alperakaydin.moviesapp.ui.theme.CustomTextColor
import com.alperakaydin.moviesapp.ui.theme.robotoregular
import com.alperakaydin.moviesapp.ui.viewmodel.ListScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    title: String,
    movies: List<Movie>,
    listScreenViewModel: ListScreenViewModel,
    navController: NavController,
    padding: PaddingValues
) {
    val loading by listScreenViewModel.loading.collectAsState()
    val cartItems by listScreenViewModel.cartItems.collectAsState()
    val favoriteMovies by listScreenViewModel.favoriteMovies.observeAsState(emptyList())


    LaunchedEffect(key1 = true) {
        listScreenViewModel.updateCartItems()
        listScreenViewModel.updateFavoriteMovies()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .background(CustomBackground),
        contentAlignment = if (loading) Alignment.Center else Alignment.TopStart
    ) {
        Column {
            Text(
                text = title,
                fontFamily = robotoregular,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = CustomTextColor,
                modifier = Modifier
                    .padding(start = 18.dp, bottom = 18.dp, top = 10.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(6.dp))

            if (movies.isEmpty()) {
                Text(
                    text = "Not Found.",
                    fontFamily = robotoregular,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                    //modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {

                    items(movies) { movie ->
                        val isFavorite = favoriteMovies.any { it.id == movie.id }
                        val cartItemCount =
                            cartItems.find { it.name == movie.name }?.orderAmount ?: 0


                        MovieDetailCard(
                            movie = movie,
                            isFavorite = isFavorite,
                            onMovieClick = { movie ->
                                val movieJson = com.google.gson.Gson().toJson(movie)
                                navController.navigate("movieDetailScreen/${movieJson}")
                            },
                            cartItemCount = cartItemCount,
                            onFavoriteClick = {
                                CoroutineScope(Dispatchers.Main).launch {
                                    listScreenViewModel.toggleFavorite(movie)

                                }
                            },
                            onAddToCartClick = {
                                CoroutineScope(Dispatchers.Main).launch {
                                    listScreenViewModel.addToCart(movie)

                                }
                            },
                            onDecreaseFromCartClick = {
                                CoroutineScope(Dispatchers.Main).launch {
                                    listScreenViewModel.decreaseFromCart(movie)

                                }
                            }
                        )
                    }
                }
            }
        }
    }

}


@Composable
fun AnimatedIconButton(
    icon: ImageVector,
    tint: Color,
    onClick: () -> Unit
) {
    val scale = remember { Animatable(1f) }
    val coroutineScope = rememberCoroutineScope() // Use Compose's coroutine scope

    IconButton(
        onClick = {
            onClick()
            coroutineScope.launch {
                scale.animateTo(1.2f) // Scale up
                scale.animateTo(1f)   // Scale down
            }
        },
        modifier = Modifier.scale(scale.value) // Apply scale to the button
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint
        )
    }
}