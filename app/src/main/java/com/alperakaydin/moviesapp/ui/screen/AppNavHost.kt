package com.alperakaydin.moviesapp.ui.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.alperakaydin.moviesapp.data.entity.movies.Movie
import com.alperakaydin.moviesapp.ui.viewmodel.AppViewModel
import com.alperakaydin.moviesapp.ui.viewmodel.CartViewModel
import com.alperakaydin.moviesapp.ui.viewmodel.HomeViewModel
import com.alperakaydin.moviesapp.ui.viewmodel.ListScreenViewModel
import com.alperakaydin.moviesapp.ui.viewmodel.MovieDetailViewModel
import com.alperakaydin.moviesapp.ui.viewmodel.SearchViewModel
import com.google.gson.Gson

@Composable
fun AppNavHost(
    navController: NavHostController, homeViewModel: HomeViewModel,
    cartViewModel: CartViewModel,
    movieDetailViewModel: MovieDetailViewModel,
    searchViewModel: SearchViewModel,
    listScreenViewModel: ListScreenViewModel,
    appViewModel: AppViewModel,
    padding: PaddingValues
) {
    //val navController = rememberNavController()
    val isOnboarded by appViewModel.isOnboarded.collectAsState()
    val isLoggedIn by appViewModel.isLoggedIn.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (isOnboarded) "login" else "onboarding"
    ) { //if (isOnboarded) "login" else "onboarding")
        composable("onboarding") {
            OnboardingScreen(onComplete = {
                appViewModel.completeOnboarding()
                navController.navigate("login") { popUpTo("onboarding") { inclusive = true } }
            })
        }
        composable("login") {
            LoginScreen(onLogin = { username, password ->
                appViewModel.login(username)
                navController.navigate("home") { popUpTo("login") { inclusive = true } }
            }, padding)
        }
        composable("home") {
            HomeScreen(homeViewModel, navController, padding)
        }
        composable(
            "movieDetailScreen/{movie}",
            arguments = listOf(navArgument("movie") {
                type = androidx.navigation.NavType.StringType
            })
        ) {
            val json = it.arguments?.getString("movie")
            val movie = Gson().fromJson(json, Movie::class.java)
            MovieDetailScreen(
                movie, navController,
                movieDetailViewModel, padding
            )
        }
        composable(
            route = "trailerScreen/{trailerUrl}",
            arguments = listOf(navArgument("trailerUrl") { type = NavType.StringType })
        ) {
            val trailerUrl = it.arguments?.getString("trailerUrl") ?: ""
            TrailerScreen(trailerUrl)
        }
        composable("cartScreen") {
            CartScreen(cartViewModel, navController, padding)
        }
        composable("searchScreen") {

            SearchScreen(searchViewModel = searchViewModel, navController = navController, padding)
        }
        composable(
            "listScreen/{title}/{moviesJson}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("moviesJson") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: "List Screen"
            val moviesJson = backStackEntry.arguments?.getString("moviesJson")
            val movies: List<Movie> = Gson().fromJson(moviesJson, Array<Movie>::class.java).toList()

            ListScreen(
                title = title,
                movies = movies,
                listScreenViewModel = listScreenViewModel,
                navController = navController,
                padding = padding
            )
        }


    }
}