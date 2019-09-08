package com.example.moviedb.ui.tvshow

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.moviedb.service.model.popular.tvShow.ResponseTvShow
import com.example.moviedb.service.network.ApiService
import com.example.moviedb.service.repository.TvShowRepository

class TvShowViewModel(apiService: ApiService?) : ViewModel(){
    private val repository = apiService?.let { TvShowRepository(it) }

    fun getTvShowPopular(page : Int) : LiveData<ResponseTvShow>?{
        return repository?.getPopularTvShow(page)
    }
}