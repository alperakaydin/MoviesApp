package com.alperakaydin.moviesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.alperakaydin.moviesapp.ui.components.CustomBottomNavigationBar
import com.alperakaydin.moviesapp.ui.components.CustomTopBar
import com.alperakaydin.moviesapp.ui.screen.AppNavHost
import com.alperakaydin.moviesapp.ui.theme.CustomBackground
import com.alperakaydin.moviesapp.ui.theme.MoviesAppTheme
import com.alperakaydin.moviesapp.ui.viewmodel.AppViewModel
import com.alperakaydin.moviesapp.ui.viewmodel.CartViewModel
import com.alperakaydin.moviesapp.ui.viewmodel.HomeViewModel
import com.alperakaydin.moviesapp.ui.viewmodel.ListScreenViewModel
import com.alperakaydin.moviesapp.ui.viewmodel.MovieDetailViewModel
import com.alperakaydin.moviesapp.ui.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val homeViewModel: HomeViewModel by viewModels()
    private val cartViewModel: CartViewModel by viewModels()
    private val movieDetailViewModel: MovieDetailViewModel by viewModels()
    private val searchViewModel: SearchViewModel by viewModels()
    private val listScreenViewModel: ListScreenViewModel by viewModels()
    private val appViewModel: AppViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val currentBackStackEntry = navController.currentBackStackEntryAsState()
            MoviesAppTheme {


                Scaffold(
                    topBar = {
                        when (currentBackStackEntry.value?.destination?.route) {
                            "login", "trailerScreen", "onboarding" -> Unit
                            else -> CustomTopBar()
                        }
                    },

                    bottomBar = {
                        when (currentBackStackEntry.value?.destination?.route) {
                            "login", "trailerScreen", "onboarding" -> Unit
                            else -> CustomBottomNavigationBar(
                                navController = navController,
                                sharedCartViewModel = cartViewModel.sharedCartViewModel
                            )
                        }
                    },

                    content = { innerPadding ->
                        AppNavHost(
                            navController = navController,
                            homeViewModel = homeViewModel,
                            cartViewModel = cartViewModel,
                            movieDetailViewModel = movieDetailViewModel,
                            searchViewModel = searchViewModel,
                            listScreenViewModel = listScreenViewModel,
                            appViewModel = appViewModel,
                            padding = innerPadding
                        )
                    },
                    containerColor = CustomBackground
                )
            }
        }
    }
}
