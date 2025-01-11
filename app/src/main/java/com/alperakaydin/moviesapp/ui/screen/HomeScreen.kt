package com.alperakaydin.moviesapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.alperakaydin.moviesapp.R
import com.alperakaydin.moviesapp.data.entity.movies.Movie
import com.alperakaydin.moviesapp.ui.theme.CustomBackground
import com.alperakaydin.moviesapp.ui.theme.CustomTextColor
import com.alperakaydin.moviesapp.ui.theme.robotoregular
import com.alperakaydin.moviesapp.ui.viewmodel.HomeViewModel
import com.skydoves.landscapist.glide.GlideImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel, navController: NavController, padding: PaddingValues

) {
    val movies by homeViewModel.filteredMovies.observeAsState(listOf())
    val categories = homeViewModel.categories.observeAsState().value ?: listOf("a", "b")
    val sortOptions = homeViewModel.sortOptions
    val favoriteMovies by homeViewModel.favoriteMovies.observeAsState(listOf())
    val cartItems by homeViewModel.cart.collectAsState()
    val recommendations by homeViewModel.recommendations.observeAsState(listOf())
    val highRatedMovies by homeViewModel.movieList.observeAsState(emptyList())


    LaunchedEffect(favoriteMovies) {
        homeViewModel.moviesLoad()
        homeViewModel.updateFavoriteMovies()
        homeViewModel.getRecommendations()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .background(CustomBackground)
    )
    {

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ) {
            item {
                HighRatedMoviesCarousel(
                    movies = highRatedMovies.sortedByDescending { it.rating }.take(10),
                    onMovieClick = { movie ->
                        val movieJson = com.google.gson.Gson().toJson(movie)
                        navController.navigate("movieDetailScreen/${movieJson}")
                    }
                )
            }
            item { // Önerilenler
                MoviesLazyRow(
                    title = "Recommended for you",
                    movies = recommendations,
                    onMovieClick = { movie ->
                        val movieJson = com.google.gson.Gson().toJson(movie)
                        navController.navigate("movieDetailScreen/${movieJson}")
                    },
                    navController = navController
                )
            }

            item { // Favoriler
                MoviesLazyRow(
                    title = "Your Favories",
                    movies = favoriteMovies,
                    onMovieClick = { movie ->
                        val movieJson = com.google.gson.Gson().toJson(movie)
                        navController.navigate("movieDetailScreen/${movieJson}")
                    },
                    navController = navController
                )
            }


            item { // Her Kategori için
                categories.forEach { category ->
                    val categoryMovies = movies.filter { it.category == category }
                    MoviesLazyRow(
                        title = category,
                        movies = categoryMovies,
                        onMovieClick = { movie ->
                            val movieJson = com.google.gson.Gson().toJson(movie)
                            navController.navigate("movieDetailScreen/${movieJson}")
                        },
                        navController = navController
                    )
                }

            }
        }

    }


}


@Composable
fun RecommendationCard(movie: Movie, onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
        ),
        modifier = Modifier
            .width(150.dp)
            .height(200.dp)

            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            var url = ""
            if (movie.image != null) {
                url = "http://kasimadalan.pe.hu/movies/images/${movie.image}" //
            } else {
                url = "http://kasimadalan.pe.hu/movies/images/dune.png" //
            }
            GlideImage(imageModel = url, modifier = Modifier.size(width = 150.dp, height = 180.dp))

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .height(20.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_shopping_bag),
                    contentDescription = "Star Icon",
                    tint = Color.Yellow,
                    modifier = Modifier
                        .size(16.dp)
                        .padding(top = 2.dp, start = 2.dp, end = 4.dp)

                )
                Text(text = "${movie.price} ₺", fontSize = 12.sp, color = CustomTextColor)
            }


        }
    }
}

@Composable
fun HighRatedMoviesCarousel(movies: List<Movie>, onMovieClick: (Movie) -> Unit) {

    Text(
        text = "Trending",
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = CustomTextColor,
        fontFamily = robotoregular,
        modifier = Modifier
            .padding(start = 16.dp, bottom = 2.dp)
            .fillMaxWidth(),
        textAlign = TextAlign.Start
    )
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        itemsIndexed(movies) { index, movie ->
            Box(
                modifier = Modifier
                    .size(200.dp, 300.dp)
                    .clickable { onMovieClick(movie) }
            ) {
                Card(
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        GlideImage(
                            imageModel = "http://kasimadalan.pe.hu/movies/images/${movie.image}",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Text(
                    text = "${index + 1}",
                    color = Color.White,
                    fontFamily = robotoregular,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                        .background(
                            color = Color.Black.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}


@Composable
fun MoviesLazyRow(
    title: String, movies: List<Movie>, onMovieClick: (Movie) -> Unit,
    navController: NavController
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontFamily = robotoregular,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 18.sp,
                color = CustomTextColor,
                //modifier = Modifier.padding(bottom = 8.dp)
            )
            Button(
                onClick = {
                    navigateToMovieList(navController, movies, title)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = CustomTextColor,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .height(20.dp)
            ) {
                Text(
                    text = "See all",
                    fontFamily = robotoregular,
                    fontSize = 10.sp,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }
    }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp)
    ) {
        items(movies) { movie ->
            RecommendationCard(movie = movie, onClick = { onMovieClick(movie) })
        }
    }
}

fun navigateToMovieDetails(navController: NavController, movie: Movie) {
    val movieJson = com.google.gson.Gson().toJson(movie)
    navController.navigate("movieDetailScreen/$movieJson")
}

fun navigateToMovieList(navController: NavController, movies: List<Movie>, title: String) {
    val moviesJson = com.google.gson.Gson().toJson(movies)
    navController.navigate("listScreen/$title/$moviesJson")
}