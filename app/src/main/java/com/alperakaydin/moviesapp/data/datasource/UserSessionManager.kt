package com.alperakaydin.moviesapp.data.datasource

import javax.inject.Inject

class UserSessionManager @Inject constructor(private val appPref: AppPref) {
    suspend fun login(username: String) {
        appPref.saveUsername(username)
        appPref.saveLoginState(true)
    }

    suspend fun logout() {
        appPref.saveUsername("")
        appPref.saveLoginState(false)
    }

    suspend fun isLoggedIn(): Boolean {
        return appPref.isLoggedIn()
    }

    suspend fun getUsername(): String {
        return appPref.readUsername()
    }
}