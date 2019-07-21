package com.example.moviedb.ui.popular

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.moviedb.service.local.MoviesDao
import com.example.moviedb.service.model.popular.ResponseMovies
import com.example.moviedb.service.model.popular.ResultsItem
import com.example.moviedb.service.network.ApiService
import com.example.moviedb.service.repository.MovieRepository


class PopularViewModel(apiService: ApiService?, moviesDao: MoviesDao) : ViewModel(){
    private val repository = MovieRepository(apiService, moviesDao)

    fun getPopularMovies(page: Int) : LiveData<ResponseMovies>?{
        return repository.getPopularMovie(page)
    }

    fun getLocalMovies() : LiveData<List<ResultsItem>> {
        return repository.getLocalData()
    }
}