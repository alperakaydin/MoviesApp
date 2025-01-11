package com.alperakaydin.moviesapp.ui.screen

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.alperakaydin.moviesapp.R
import com.alperakaydin.moviesapp.data.entity.movies.Movie
import com.alperakaydin.moviesapp.ui.theme.CustomBackground
import com.alperakaydin.moviesapp.ui.theme.CustomButtonColor
import com.alperakaydin.moviesapp.ui.theme.CustomButtonColorLight
import com.alperakaydin.moviesapp.ui.theme.robotoregular
import com.alperakaydin.moviesapp.ui.viewmodel.SearchViewModel
import com.skydoves.landscapist.glide.GlideImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel,
    navController: NavController,
    padding: PaddingValues
) {
    val query by searchViewModel.query.observeAsState("")
    val searchResults by searchViewModel.searchResults.observeAsState(emptyList())
    val movies by searchViewModel.movies.observeAsState(emptyList())
    val context = LocalContext.current
    val speechRecognizer = remember { SpeechRecognizer.createSpeechRecognizer(context) }
    val recognizerIntent = remember {
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
        }
    }

    val speechResult = remember { mutableStateOf("") }

    // SpeechRecognizer Listener
    speechRecognizer.setRecognitionListener(object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {}
        override fun onBeginningOfSpeech() {}
        override fun onRmsChanged(rmsdB: Float) {}
        override fun onBufferReceived(buffer: ByteArray?) {}
        override fun onEndOfSpeech() {}
        override fun onError(error: Int) {
            Log.e("SpeechRecognizer", "Error occurred: $error")
        }

        override fun onResults(results: Bundle?) {
            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            if (!matches.isNullOrEmpty()) {
                val spokenText = matches[0]
                speechResult.value = spokenText
                searchViewModel.updateQuery(spokenText)
            }
        }

        override fun onPartialResults(partialResults: Bundle?) {}
        override fun onEvent(eventType: Int, params: Bundle?) {}
    })

    val activity = context as? Activity

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                1001
            )
        }
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
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .background(Color.Transparent)

        ) {
            // Search Bar
            Row(
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth()
                    .background(CustomButtonColor, RoundedCornerShape(16.dp))
                    .padding(start = 16.dp, end = 16.dp),

                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(2.dp))
                TextField(
                    value = query,
                    onValueChange = { searchViewModel.updateQuery(it) },
                    placeholder = {
                        Text(
                            "Search by actor, title...",
                            color = Color.LightGray
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.Transparent),
                    colors = TextFieldDefaults.textFieldColors(
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        cursorColor = Color.White,
                        containerColor = Color.Transparent
                    ),
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_mic),
                    contentDescription = "Voice Search",
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            if (ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.RECORD_AUDIO
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                speechRecognizer.startListening(recognizerIntent)
                            } else {
                                ActivityCompat.requestPermissions(
                                    activity!!,
                                    arrayOf(Manifest.permission.RECORD_AUDIO),
                                    1001
                                )
                            }
                        }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Category Grid
            if (query == "") {
                CategoryGrid(movies = movies, navController = navController)
            }


            Spacer(modifier = Modifier.height(16.dp))

            // Search Results
            if (searchResults.isNotEmpty()) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp), // Daha geniş boşluklar
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent)

                ) {
                    items(searchResults) { movie ->
                        SearchResultCard(movie = movie, navController = navController)
                    }
                }
            } else if (query != "") {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "No Results",
                            tint = Color.Gray,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No results found",
                            fontFamily = robotoregular,
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Try searching for different keywords.",
                            fontFamily = robotoregular,
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchResultCard(movie: Movie, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                CustomButtonColorLight,
                RoundedCornerShape(12.dp)
            )
            .padding(2.dp)
            .clickable {
                val movieJson = com.google.gson
                    .Gson()
                    .toJson(movie)
                navController.navigate("movieDetailScreen/${movieJson}")
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        GlideImage(
            imageModel = "http://kasimadalan.pe.hu/movies/images/${movie.image}",
            modifier = Modifier
                .size(80.dp) // Görsel boyutu
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = movie.name,
                fontFamily = robotoregular,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1
            )

            Text(
                text = movie.director,
                fontFamily = robotoregular,
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Rate: ${movie.rating}",
                fontFamily = robotoregular,
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1
            )
            Text(
                text = "Price: ${movie.price}₺",
                fontFamily = robotoregular,
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1
            )
        }
    }
}

@Composable
fun CategoryGrid(navController: NavController, movies: List<Movie>) {
    val categories = listOf(
        Pair("Action", R.drawable.action),
        Pair("Fantastic", R.drawable.fantastic),
        Pair("Drama", R.drawable.dram),
        Pair("Science Fiction", R.drawable.scifi)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        // .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        categories.chunked(2).forEach { rowCategories ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                rowCategories.forEach { category ->
                    CategoryImage(
                        category = category,
                        movies = movies,
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryImage(category: Pair<String, Int>, movies: List<Movie>, navController: NavController) {
    Image(
        painter = painterResource(id = category.second),
        contentDescription = category.first,

        modifier = Modifier
            .size(180.dp)
            .clip(RoundedCornerShape(32.dp))
            .clickable {
                val movieJson =
                    com.google.gson
                        .Gson()
                        .toJson(movies.filter { it.category == category.first })
                navController.navigate("listScreen/${category.first}/${movieJson}")
            }
            .padding(8.dp)
    )
}