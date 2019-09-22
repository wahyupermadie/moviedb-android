package com.example.moviedb.ui.tvshow

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.moviedb.service.local.TvShowDao
import com.example.moviedb.service.model.popular.tvShow.ResponseTvShow
import com.example.moviedb.service.model.popular.tvShow.ResultsItem
import com.example.moviedb.service.network.ApiService
import com.example.moviedb.service.repository.TvShowRepository

class TvShowViewModel(apiService: ApiService?, tvShowDao: TvShowDao?) : ViewModel(){
    private val repository = apiService?.let { tvShowDao?.let { it1 -> TvShowRepository(it, it1) } }

    fun getTvShowPopular(page : Int) : LiveData<ResponseTvShow>?{
        return repository?.getPopularTvShow(page)
    }

    fun getLocalData() : LiveData<List<ResultsItem>>? {
        return repository?.getLocalData()
    }
}