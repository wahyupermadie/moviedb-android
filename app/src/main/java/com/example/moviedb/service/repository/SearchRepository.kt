package com.example.moviedb.service.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moviedb.BuildConfig
import com.example.moviedb.service.local.MoviesDao
import com.example.moviedb.service.local.TvShowDao
import com.example.moviedb.service.model.popular.ResponseMovies
import com.example.moviedb.service.model.popular.tvShow.ResponseTvShow
import com.example.moviedb.service.network.ApiService
import com.example.moviedb.utils.Constant
import com.example.moviedb.utils.Resources
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SearchRepository (private val apiService: ApiService, private val tvShowDao: TvShowDao,
                        private val moviesDao: MoviesDao){
    @SuppressLint("CheckResult")
    fun searchMovie(param: String) : LiveData<Resources<ResponseMovies>> {
        val data = MutableLiveData<Resources<ResponseMovies>>()
        apiService.searchMovie(Constant.LANGUAGE, BuildConfig.API_KEY, param)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                data.postValue(Resources.success(it))
                insertLocalMovies(it)
            },{
                data.value = Resources.error(it.localizedMessage, null)
            })
        return data
    }

    @SuppressLint("CheckResult")
    fun searchTv(param: String) : LiveData<Resources<ResponseTvShow>> {
        val data = MutableLiveData<Resources<ResponseTvShow>>()
        apiService.searchTv(Constant.LANGUAGE, BuildConfig.API_KEY, param)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                data.postValue(Resources.success(it))
                insertLocalTv(it)
            },{
                data.value = Resources.error(it.localizedMessage, null)
            })
        return data
    }

    private fun insertLocalTv(responseTvShow: ResponseTvShow?) {
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

    private fun insertLocalMovies(responseMovies: ResponseMovies?) {
        responseMovies?.results?.forEach {
            moviesDao.insert(it)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    Log.d("RxJava","Insert Sukses")
                },{error ->
                    Log.d("RxJava","Error Insert "+error.localizedMessage)
                })
        }
    }
}