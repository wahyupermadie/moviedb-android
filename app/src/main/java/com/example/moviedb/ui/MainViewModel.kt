package com.example.moviedb.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.moviedb.service.local.MoviesDao
import com.example.moviedb.service.model.popular.ResponsePopular
import com.example.moviedb.service.network.ApiService
import com.example.moviedb.service.repository.MovieRepository


class MainViewModel(apiService: ApiService?, moviesDao: MoviesDao) : ViewModel(){
    private val repository = MovieRepository(apiService, moviesDao)

    fun getPopularMovies(page: Int) : LiveData<ResponsePopular>?{
        return repository.getPopularMovie(page)
    }

    fun getLocalMovies(page: Int) : LiveData<ResponsePopular>?{
        return repository.getLocalData(page)
    }
}