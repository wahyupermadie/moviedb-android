package com.example.moviedb.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.moviedb.service.local.MoviesDao
import com.example.moviedb.service.local.FavoritDao
import com.example.moviedb.service.model.popular.video.ResponseVideo
import com.example.moviedb.service.network.ApiService
import com.example.moviedb.service.repository.MovieRepository

class DetailActivityViewModel(apiService: ApiService?, moviesDao: MoviesDao, favoritDao: FavoritDao) : ViewModel(){
    private val repository = MovieRepository(apiService, moviesDao, favoritDao)

    fun getTrailers(movieId: String) : LiveData<ResponseVideo>{
        return repository.getTrailersMovies(movieId)
    }
}