package com.alperakaydin.moviesapp.ui.screen

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.alperakaydin.moviesapp.R
import com.alperakaydin.moviesapp.ui.components.CartItemCard
import com.alperakaydin.moviesapp.ui.theme.CustomBackground
import com.alperakaydin.moviesapp.ui.theme.CustomButtonColor
import com.alperakaydin.moviesapp.ui.theme.CustomTextColor
import com.alperakaydin.moviesapp.ui.theme.robotoregular
import com.alperakaydin.moviesapp.ui.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartViewModel: CartViewModel,
    navController: NavController,
    padding: PaddingValues

) {
    val cartItems by cartViewModel.cart.collectAsState()
    val totalCartPrice by cartViewModel.totalCartPrice.collectAsState()

    LaunchedEffect(cartItems) {
        cartViewModel.loadCartItems()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .background(CustomBackground)
    )
    {

        if (cartItems.isEmpty()) {
            // Sepet Boş Durumu
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_shopping_bag),
                        contentDescription = "Cart is empty",
                        tint = Color.Gray,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Cart is empty",
                        fontFamily = robotoregular,
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyLarge
                    )

                }
            }
        } else {
            // Sepet İçeriği
            Column(
                modifier = Modifier
                    .fillMaxSize()

            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(cartItems) { cartItem ->
                        CartItemCard(
                            cartItem = cartItem,
                            cartItemCount = cartItem.orderAmount,
                            onAddToCartClick = { cartViewModel.increaseToMovieCountInCart(cartItem.name) },
                            onDecreaseFromCartClick = {
                                cartViewModel.decreaseToMovieCountInCart(
                                    cartItem.name
                                )
                            }
                        )

                    }
                }


                // Toplam Fiyat ve Ödeme Butonu
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(start = 30.dp)
                    ) {
                        Text(
                            text = "Total ",
                            fontFamily = robotoregular,
                            fontSize = 12.sp,
                            color = CustomTextColor
                        )
                        Text(
                            text = "${totalCartPrice}₺",
                            fontFamily = robotoregular,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = CustomTextColor,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(80.dp))

                    Button(
                        onClick = {
                            cartViewModel.clearCart()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CustomButtonColor
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "Complete Order",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }


}

