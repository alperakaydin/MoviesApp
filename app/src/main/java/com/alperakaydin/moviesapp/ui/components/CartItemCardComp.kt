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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.alperakaydin.moviesapp.data.entity.cart.CartItem
import com.alperakaydin.moviesapp.ui.theme.CustomTextColor
import com.skydoves.landscapist.glide.GlideImage

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CartItemCard(
    cartItem: CartItem,
    cartItemCount: Int,
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
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Movie Image
            GlideImage(
                imageModel = "http://kasimadalan.pe.hu/movies/images/${cartItem.image}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp, 150.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.Gray)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                )
                {
                    Text(
                        text = cartItem.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = CustomTextColor
                    )


                }
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = cartItem.director,
                        color = CustomTextColor,
                        fontSize = 16.sp,
                        maxLines = 1
                    )

                    Text(
                        text = "${cartItem.price * cartItem.orderAmount}₺",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = CustomTextColor
                    )

                }//Film detay
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically


                ) {
                    Text(
                        text = "${cartItem.year} • Rate: ${cartItem.rating}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = CustomTextColor
                    )


                    // Cart Buttons
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.height(34.dp)
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
                                        com.alperakaydin.moviesapp.ui.screen.AnimatedIconButton(
                                            onClick = onDecreaseFromCartClick,
                                            icon = ImageVector.vectorResource(id = R.drawable.ic_remove),
                                            tint = Color.White
                                        )
                                        Text(
                                            text = "$targetCartItemCount",
                                            fontSize = 16.sp,
                                            color = CustomTextColor
                                        )
                                    }
                                    com.alperakaydin.moviesapp.ui.screen.AnimatedIconButton(
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


