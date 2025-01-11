package com.alperakaydin.moviesapp.ui.components


import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alperakaydin.moviesapp.R
import com.alperakaydin.moviesapp.data.entity.movies.Movie
import com.alperakaydin.moviesapp.ui.screen.AnimatedIconButton
import com.alperakaydin.moviesapp.ui.theme.CustomTextColor
import com.alperakaydin.moviesapp.ui.theme.robotoregular
import com.skydoves.landscapist.glide.GlideImage

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MovieDetailCard(
    movie: Movie,
    isFavorite: Boolean,
    onMovieClick: (Movie) -> Unit,
    cartItemCount: Int,
    onFavoriteClick: () -> Unit,
    onAddToCartClick: () -> Unit,
    onDecreaseFromCartClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
        ),

        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(8.dp)
            .clickable { onMovieClick(movie) },
        shape = RoundedCornerShape(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween


        ) {
            // Movie Image
            GlideImage(
                imageModel = "http://kasimadalan.pe.hu/movies/images/${movie.image}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp, 150.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.Gray)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                )
                {
                    Text(
                        text = movie.name,
                        fontFamily = robotoregular,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = CustomTextColor
                    )
                    // Favorite Button
                    AnimatedIconButton(
                        onClick = onFavoriteClick,
                        icon = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))


                //Film detay
                Text(
                    text = movie.director,
                    fontFamily = robotoregular,
                    color = CustomTextColor,
                    fontSize = 16.sp,
                    maxLines = 1
                )

                Text(
                    text = "${movie.year} • Rate: ${movie.rating}",
                    fontSize = 14.sp,
                    fontFamily = robotoregular,

                    color = CustomTextColor
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,


                    ) {
                    Text(
                        text = "${movie.price}₺",
                        fontFamily = robotoregular,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = CustomTextColor
                    )

                    // Cart Buttons
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .border(1.dp, Color.White, RoundedCornerShape(4.dp))
                                .padding(4.dp)
                        ) {
                            AnimatedContent(
                                targetState = cartItemCount,
                                transitionSpec = {
                                    fadeIn() + scaleIn() with fadeOut() + scaleOut()
                                }
                            ) { targetCartItemCount ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    if (targetCartItemCount > 0) {
                                        AnimatedIconButton(
                                            onClick = onDecreaseFromCartClick,
                                            icon = ImageVector.vectorResource(id = R.drawable.ic_remove),
                                            tint = Color.White
                                        )
                                        Text(
                                            text = "$targetCartItemCount",
                                            fontFamily = robotoregular,
                                            fontSize = 16.sp,
                                            color = CustomTextColor
                                        )
                                    }
                                    AnimatedIconButton(
                                        onClick = onAddToCartClick,
                                        icon = Icons.Default.Add,
                                        tint = Color.White
                                    )
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}

