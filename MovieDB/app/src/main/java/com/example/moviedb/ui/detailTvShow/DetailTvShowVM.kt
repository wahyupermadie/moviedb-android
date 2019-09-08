package com.example.moviedb.ui.detailTvShow

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.moviedb.service.model.popular.video.ResponseVideo
import com.example.moviedb.service.network.ApiService
import com.example.moviedb.service.repository.TvShowRepository

class DetailTvShowVM(apiService: ApiService?) : ViewModel(){
    private val repository = apiService?.let { TvShowRepository(it) }

    fun getTrailers(tvId: String) : LiveData<ResponseVideo>? {
        return repository?.getTrailersTv(tvId)
    }

}