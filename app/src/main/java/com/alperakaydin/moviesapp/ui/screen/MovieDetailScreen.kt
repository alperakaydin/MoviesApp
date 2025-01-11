package com.alperakaydin.moviesapp.ui.screen

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.webkit.WebView
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.traceEventEnd
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.alperakaydin.moviesapp.R
import com.alperakaydin.moviesapp.data.entity.movies.Movie
import com.alperakaydin.moviesapp.ui.components.CustomBottomNavigationBar
import com.alperakaydin.moviesapp.ui.components.CustomTopBar
import com.alperakaydin.moviesapp.ui.theme.CustomBackground
import com.alperakaydin.moviesapp.ui.theme.CustomButtonColor
import com.alperakaydin.moviesapp.ui.theme.CustomTextColor
import com.alperakaydin.moviesapp.ui.theme.robotoregular
import com.alperakaydin.moviesapp.ui.viewmodel.MovieDetailViewModel
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.runBlocking

@Composable
fun MovieDetailScreen(
    movie: Movie,
    navController: NavController,
    movieDetailViewModel: MovieDetailViewModel,
    padding: PaddingValues
) {
    val isFavorite by movieDetailViewModel.isFavorite.observeAsState(false)
    val userCount by movieDetailViewModel.userCount.collectAsState()
    val context = LocalContext.current
    val shareIntent = movieDetailViewModel.getShareIntent(movie)
    val trailerUrl by movieDetailViewModel.trailerUrl.observeAsState()

    LaunchedEffect(true) {
        //movieDetailViewModel.loadCartItems()
        movieDetailViewModel.isFavoriteUpdate(movie)
        movieDetailViewModel.fetchUserCountForMovie(movie.name)
        movieDetailViewModel.loadTrailer(movie)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding) // Scaffold'dan gelen padding uygulanıyor
            .background(CustomBackground)
    )
    {
            Column(
                modifier = Modifier
                    .fillMaxSize()

            ) {
                // Üstteki görsel ve gradyan
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    GlideImage(
                        imageModel = "http://kasimadalan.pe.hu/movies/images/${movie.image ?: "default.png"}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, CustomBackground),
                                    startY = 150f,
                                    endY = 750f
                                )
                            )
                    )
                }
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = movie.name,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = CustomTextColor
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Kullanıcı sayısı bilgisi
                Text(
                    text = if (userCount > 0) {
                        "This movie is in the cart of $userCount users."
                    } else {
                        "This movie is not in anyone's cart."
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    color = CustomTextColor,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // İşlevsel butonlar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    MovieActionButton(
                        icon = if(isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        label = if(!isFavorite) "Add Favorite" else "Remove Favorite",
                        onClick = { movieDetailViewModel.toggleFavorite(movie) }
                    )
                    MovieActionButton(
                        icon = Icons.Default.Share,
                        label = "Share",
                        onClick = {
                            context.startActivity(
                                Intent.createChooser(shareIntent, "Trailer")
                            )
                        }
                    )
                    MovieActionButton(
                        icon = ImageVector.vectorResource(id = R.drawable.ic_play_),
                        label = "Watch the Trailer",
                        onClick = {
                            val videoId = movieDetailViewModel.extractVideoId(trailerUrl)
                            if (!videoId.isNullOrEmpty()) {
                                navController.navigate("trailerScreen/$videoId")
                            } else {
                                Toast.makeText(context, "Trailer not found.", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        movieDetailViewModel.addToCart(movie)
                        Toast.makeText(context, "Added to cart.", Toast.LENGTH_SHORT).show()
                        navController.navigate("cartScreen")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CustomButtonColor
                    ),
                    //contentPadding = PaddingValues(12.dp),
                    modifier = Modifier.padding(16.dp)
                        .border(
                            width = 1.dp,
                            color = CustomTextColor,
                            shape = RoundedCornerShape(6.dp)
                        ).fillMaxWidth()
                        .height(40.dp)
                    ,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Buy Now - ${movie.price}₺", style = MaterialTheme.typography.bodyLarge)
                }


                // Açıklama ve diğer bilgiler
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Director: ${movie.director}",
                        fontFamily = robotoregular,
                        style = MaterialTheme.typography.bodyMedium,
                        color = CustomTextColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "IMDb Rating: ${movie.rating}",
                        fontFamily = robotoregular,
                        style = MaterialTheme.typography.bodyMedium,
                        color = CustomTextColor
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = movie.description,
                        fontFamily = robotoregular,
                        style = MaterialTheme.typography.bodySmall,
                        color = CustomTextColor
                    )
                }




            }
        }

}

@Composable
fun MovieActionButton(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
    ) {
        Icon(imageVector = icon, contentDescription = label, tint = Color.White, modifier = Modifier.size(24.dp))
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = Color.White)
    }
}

