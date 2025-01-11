package com.alperakaydin.moviesapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alperakaydin.moviesapp.data.datasource.AppPref
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(private val appPref: AppPref) : ViewModel() {

    private val _isOnboarded = MutableStateFlow(false)
    val isOnboarded: StateFlow<Boolean> = _isOnboarded

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    init {
        checkUserState()
    }

    private fun checkUserState() {
        viewModelScope.launch {
            _isOnboarded.value = appPref.isOnboarded()
            _isLoggedIn.value = appPref.isLoggedIn()
        }
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            appPref.saveOnboardingState(true)
            _isOnboarded.value = true
        }
    }

    fun login(username: String) {
        viewModelScope.launch {
            appPref.saveUsername(username)
            appPref.saveLoginState(true)
            _isLoggedIn.value = true
        }
    }
}