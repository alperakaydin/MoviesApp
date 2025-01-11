package com.alperakaydin.moviesapp.retrofit


class ApiUtils {
    companion object {
        val BASE_URL = "http://kasimadalan.pe.hu/"
        fun getMoviesDao(): MoviesDAO {
            return RefrofitClient.getClient(BASE_URL).create(MoviesDAO::class.java)
        }

    }
}