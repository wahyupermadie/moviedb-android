package com.wepe.moviedb.ui.searchActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.wepe.moviedb.service.model.popular.ResponseMovies
import com.wepe.moviedb.service.model.popular.tvShow.ResponseTvShow
import com.wepe.moviedb.service.repository.SearchRepository
import com.wepe.moviedb.utils.Resources

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