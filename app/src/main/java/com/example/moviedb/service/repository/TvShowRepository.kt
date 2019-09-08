package com.example.moviedb.service.repository

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moviedb.BuildConfig
import com.example.moviedb.service.model.popular.tvShow.ResponseTvShow
import com.example.moviedb.service.model.popular.video.ResponseVideo
import com.example.moviedb.service.network.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TvShowRepository(val apiService: ApiService){

    @SuppressLint("CheckResult")
    fun getPopularTvShow(page : Int) : LiveData<ResponseTvShow> {
        val tvList = MutableLiveData<ResponseTvShow>()
        apiService.getTvPopular(BuildConfig.API_KEY, page)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                tvList.value = it
            },{
                tvList.value = null
            })
        return tvList
    }

    @SuppressLint("CheckResult")
    fun getTrailersTv(tvId : String) : LiveData<ResponseVideo>{
        val tvTrailers = MutableLiveData<ResponseVideo>()
        apiService.getTvTrailers(tvId,BuildConfig.API_KEY)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                tvTrailers.value = it
            },{
                tvTrailers.value = null
            })
        return tvTrailers
    }
}