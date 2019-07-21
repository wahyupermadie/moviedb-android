package com.example.moviedb.ui.popular

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.moviedb.service.local.MoviesDao
import com.example.moviedb.service.local.FavoritDao
import com.example.moviedb.service.model.popular.ResponseMovies
import com.example.moviedb.service.network.ApiService
import com.example.moviedb.service.repository.MovieRepository


class PopularViewModel(apiService: ApiService?, moviesDao: MoviesDao, favoritDao: FavoritDao) : ViewModel(){
    private val repository = MovieRepository(apiService, moviesDao, favoritDao)

    fun getPopularMovies(page: Int) : LiveData<ResponseMovies>?{
        return repository.getPopularMovie(page)
    }

    fun getLocalMovies(page: Int) : LiveData<ResponseMovies>?{
        return repository.getLocalData(page)
    }
}