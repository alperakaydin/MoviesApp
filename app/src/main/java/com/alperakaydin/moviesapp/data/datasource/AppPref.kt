package com.alperakaydin.moviesapp.data.datasource

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

class AppPref(var context: Context) {

    val Context.ds: DataStore<Preferences> by preferencesDataStore("favori_movies")

    companion object {
        val FAVORITES_LIST = stringSetPreferencesKey("FAVORITES")
        val USERNAME_KEY = stringPreferencesKey("username")
        val IS_LOGGED_IN_KEY = booleanPreferencesKey("is_logged_in")
        val IS_ONBOARDED_KEY = booleanPreferencesKey("is_onboarded")

    }

    suspend fun saveOnboardingState(isCompleted: Boolean) {
        context.ds.edit { preferences ->
            preferences[IS_ONBOARDED_KEY] = isCompleted
        }
    }

    suspend fun isOnboarded(): Boolean {
        val preferences = context.ds.data.first()
        return preferences[IS_ONBOARDED_KEY] ?: false
    }

    suspend fun saveUsername(username: String) {
        context.ds.edit {
            it[USERNAME_KEY] = username
        }
    }

    suspend fun readUsername(): String {
        val p = context.ds.data.first()
        return p[USERNAME_KEY] ?: ""
    }

    suspend fun saveLoginState(isLoggedIn: Boolean) {
        context.ds.edit { preferences ->
            preferences[IS_LOGGED_IN_KEY] = isLoggedIn
        }
    }

    suspend fun isLoggedIn(): Boolean {
        val preferences = context.ds.data.first()
        return preferences[IS_LOGGED_IN_KEY] ?: false
    }

    suspend fun saveFavorites(list: Set<String>) {
        context.ds.edit {
            it[FAVORITES_LIST] = list
        }
    }

    suspend fun readFavorites(): Set<String> {
        val p = context.ds.data.first()
        return p[FAVORITES_LIST] ?: setOf()
    }

    suspend fun deleteFavorites() {
        context.ds.edit {
            it.remove(FAVORITES_LIST)
        }
    }
}