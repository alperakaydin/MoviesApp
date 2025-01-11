package com.alperakaydin.moviesapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.alperakaydin.moviesapp.R
import com.alperakaydin.moviesapp.shared.viewmodel.SharedCartViewModel
import com.alperakaydin.moviesapp.ui.theme.CustomBackground

@Composable
fun CustomBottomNavigationBar(
    navController: NavController,
    sharedCartViewModel: SharedCartViewModel
) {
    val cartItems = sharedCartViewModel.cartItems.collectAsState().value
    val cartItemCount = cartItems.sumOf { it.orderAmount }
    val loadingAction by sharedCartViewModel.isUpdatingCart.collectAsState()
    val currentRoute = navController.currentDestination?.route


    NavigationBar(
        containerColor = CustomBackground,
        tonalElevation = 0.dp,
        modifier = Modifier.height(80.dp)

    ) {
        NavigationBarItem(
            selected = currentRoute == "home",
            onClick = { navController.navigate("home") },

            icon = {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(20.dp)
                        .fillMaxSize()
                ) {
                    if (currentRoute == "home") {
                        // Işıklandırma efekti
                        Box(
                            modifier = Modifier
                                .size(78.dp)
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(Color(0x40FFFFFF), Color.Transparent),
                                        center = Offset(100f, 70f),
                                        radius = 60f
                                    ),
                                    shape = CircleShape
                                )
                        )
                    }
                    Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.White)
                }
            },
            label = {
                Text(
                    "Home",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    modifier = Modifier.padding(0.dp)

                )
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.Transparent,
                selectedIconColor = Color.White,
                unselectedIconColor = Color.Gray,
                selectedTextColor = Color.White,
                unselectedTextColor = Color.Gray
            )
        )
        NavigationBarItem(
            selected = currentRoute == "searchScreen",
            onClick = { navController.navigate("searchScreen") },
            icon = {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(20.dp)
                        .fillMaxSize()
                ) {
                    if (currentRoute == "searchScreen") {
                        // Işıklandırma efekti
                        Box(
                            modifier = Modifier
                                .size(78.dp)
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(Color(0x40FFFFFF), Color.Transparent),
                                        center = Offset(100f, 70f),
                                        radius = 60f
                                    ),
                                    shape = CircleShape
                                )
                        )
                    }
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
                }
            },
            label = {
                Text(
                    "Search",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    modifier = Modifier.padding(0.dp)

                )
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.Transparent,
                selectedIconColor = Color.White,
                unselectedIconColor = Color.Gray,
                selectedTextColor = Color.White,
                unselectedTextColor = Color.Gray
            )
        )
        NavigationBarItem(
            selected = currentRoute == "cartScreen",
            onClick = { navController.navigate("cartScreen") },
            icon = {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(20.dp)
                        .fillMaxSize()
                ) {
                    if (currentRoute == "cartScreen") {
                        Box(
                            modifier = Modifier
                                .size(78.dp)
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(Color(0x40FFFFFF), Color.Transparent),
                                        center = Offset(
                                            100f,
                                            70f
                                        ),
                                        radius = 60f
                                    ),
                                    shape = CircleShape
                                )
                        )
                    }
                    Box {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_shopping_cart),
                            contentDescription = "Cart",
                            modifier = Modifier.size(24.dp),
                            tint = Color.White
                        )

                        if (cartItemCount > 0) {
                            Box(
                                modifier = Modifier
                                    .size(14.dp)
                                    .background(Color.Red, CircleShape)
                                    .align(Alignment.TopEnd)
                            ) {
                                Text(
                                    text = if (loadingAction) "0" else cartItemCount.toString(),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                    }
                }
            },
            label = {
                Text(
                    "Cart",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    modifier = Modifier.padding(0.dp)

                )
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.Transparent,
                selectedIconColor = Color.White,
                unselectedIconColor = Color.Gray,
                selectedTextColor = Color.White,
                unselectedTextColor = Color.Gray
            )
        )
    }

}

