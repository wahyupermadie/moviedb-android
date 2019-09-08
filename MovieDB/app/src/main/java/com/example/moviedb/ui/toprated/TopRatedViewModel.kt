package com.example.moviedb.ui.toprated

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.moviedb.service.local.MoviesDao
import com.example.moviedb.service.model.popular.ResponseMovies
import com.example.moviedb.service.model.popular.ResultsItem
import com.example.moviedb.service.network.ApiService
import com.example.moviedb.service.repository.MovieRepository

class TopRatedViewModel(apiService: ApiService?, moviesDao: MoviesDao) : ViewModel(){
    private val repository = MovieRepository(apiService, moviesDao)

    fun getTopRatedMovie(page: Int) : LiveData<ResponseMovies>?{
        return repository.getTopRatedMovie(page)
    }

    fun getTopRatedLocalData() : LiveData<List<ResultsItem>>?{
        return repository.getLocalTopRatedData()
    }
}