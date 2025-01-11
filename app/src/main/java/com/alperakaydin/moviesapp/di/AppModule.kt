package com.alperakaydin.moviesapp.di

import android.content.Context
import com.alperakaydin.moviesapp.data.datasource.AppPref
import com.alperakaydin.moviesapp.data.datasource.MovieDataSource
import com.alperakaydin.moviesapp.data.datasource.UserSessionManager
import com.alperakaydin.moviesapp.data.repo.CartRepository
import com.alperakaydin.moviesapp.data.repo.MovieRepository
import com.alperakaydin.moviesapp.retrofit.ApiUtils
import com.alperakaydin.moviesapp.retrofit.MoviesDAO
import com.alperakaydin.moviesapp.shared.viewmodel.SharedCartViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideMovieRepository(
        movieDataSource: MovieDataSource,
        cartRepository: CartRepository,
        userSessionManager: UserSessionManager,
        appPref: AppPref
    ): MovieRepository {
        return MovieRepository(movieDataSource, cartRepository, userSessionManager, appPref)
    }

    @Provides
    @Singleton
    fun provideCartRepository(
        movieDataSource: MovieDataSource,
        userSessionManager: UserSessionManager,
        appPref: AppPref
    ): CartRepository {
        return CartRepository(movieDataSource, userSessionManager, appPref)
    }

    @Provides
    @Singleton
    fun provideMovieDataSource(moviesDAO: MoviesDAO): MovieDataSource {
        return MovieDataSource(moviesDAO)
    }

    @Provides
    @Singleton
    fun provideMovieDAO(): MoviesDAO {
        return ApiUtils.getMoviesDao()

    }

    @Provides
    @Singleton
    fun provideAppPref(@ApplicationContext context: Context): AppPref {
        return AppPref(context)
    }

    @Provides
    fun provideUserSessionManager(appPref: AppPref): UserSessionManager {
        return UserSessionManager(appPref)
    }

    @Provides
    @Singleton
    fun provideSharedCartViewModel(
        movieRepository: MovieRepository,
        cartRepository: CartRepository
    ): SharedCartViewModel {
        return SharedCartViewModel(movieRepository, cartRepository)
    }

}