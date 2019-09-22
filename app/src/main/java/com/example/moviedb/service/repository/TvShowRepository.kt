package com.example.moviedb.service.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moviedb.BuildConfig
import com.example.moviedb.service.local.TvShowDao
import com.example.moviedb.service.model.popular.tvShow.ResponseTvShow
import com.example.moviedb.service.model.popular.tvShow.ResultsItem
import com.example.moviedb.service.model.popular.video.ResponseVideo
import com.example.moviedb.service.network.ApiService
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TvShowRepository(val apiService: ApiService, val tvShowDao: TvShowDao){

    @SuppressLint("CheckResult")
    fun getPopularTvShow(page : Int) : LiveData<ResponseTvShow> {
        val tvList = MutableLiveData<ResponseTvShow>()
        apiService.getTvPopular(BuildConfig.API_KEY, page)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                tvList.value = it
                insertLocal(it)
            },{
                tvList.value = null
            })
        return tvList
    }

    fun updateFavorite(id : Int, value : Boolean): Completable {
        return tvShowDao.setFavorite(value, id)
    }

    private fun insertLocal(responseTvShow: ResponseTvShow?) {
        responseTvShow?.results?.forEach {
            tvShowDao.insert(it)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    Log.d("RxJava","Insert Sukses")
                },{error ->
                    Log.d("RxJava","Error Insert "+error.localizedMessage)
                })
        }
    }

    fun getLocalData() : LiveData<List<ResultsItem>> {
        return tvShowDao.getData()
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

    fun getSingleLocalData(id: Int): Single<ResultsItem> {
        return tvShowDao.getItem(id)
    }
}