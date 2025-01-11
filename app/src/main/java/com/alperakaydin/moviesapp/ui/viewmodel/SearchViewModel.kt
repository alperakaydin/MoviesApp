package com.alperakaydin.moviesapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alperakaydin.moviesapp.data.entity.movies.Movie
import com.alperakaydin.moviesapp.data.repo.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    val movies = MutableLiveData<List<Movie>>()

    private val _searchResults = MutableLiveData<List<Movie>>()
    val searchResults: LiveData<List<Movie>> = _searchResults

    private val _query = MutableLiveData<String>("")
    val query: LiveData<String> = _query

    fun updateQuery(newQuery: String) {
        _query.value = newQuery
        performSearch()
    }

    init {
        viewModelScope.launch {
            movies.value = movieRepository.moviesLoad()
        }
    }

    private fun performSearch() {
        viewModelScope.launch {
            val queryText = _query.value ?: ""
            if (queryText.isNotBlank()) {
                val results = movieRepository.moviesLoad().filter {
                    it.name.contains(queryText, ignoreCase = true) ||
                            it.director.contains(queryText, ignoreCase = true)
                }
                _searchResults.value = results
            } else {
                _searchResults.value = emptyList()
            }
        }
    }
}