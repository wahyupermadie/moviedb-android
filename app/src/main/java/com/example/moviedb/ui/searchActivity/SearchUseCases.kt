package com.example.moviedb.ui.searchActivity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.moviedb.service.model.popular.ResponseMovies
import com.example.moviedb.service.model.popular.tvShow.ResponseTvShow
import com.example.moviedb.service.repository.SearchRepository
import com.example.moviedb.utils.Resources

class SearchUseCases (private val searchRepository: SearchRepository){

    fun searchMovies(param : String) : LiveData<Resources<ResponseMovies>>{
        return Transformations.map(searchRepository.searchMovie(param)){
            it
        }
    }

    fun searchTv(param : String) : LiveData<Resources<ResponseTvShow>>{
        return Transformations.map(searchRepository.searchTv(param)){
            it
        }
    }
}