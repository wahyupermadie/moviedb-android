package com.example.moviedb.service.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moviedb.BuildConfig
import com.example.moviedb.service.local.MoviesDao
import com.example.moviedb.service.model.popular.ResponsePopular
import com.example.moviedb.service.model.popular.video.ResponseVideo
import com.example.moviedb.service.model.popular.video.ResultsItem
import com.example.moviedb.service.network.ApiService
import com.example.moviedb.utils.Constant
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MovieRepository(
    private val apiService: ApiService?,
    private val moviesDao: MoviesDao
){

    @SuppressLint("CheckResult")
    fun getPopularMovie(page : Int): LiveData<ResponsePopular>?{
        val popularMovies = MutableLiveData<ResponsePopular>()
        apiService?.getPopularMovies(BuildConfig.API_KEY, page)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribe({
                popularMovies.value = it
                insertLocal(it)
            },{
                popularMovies.value = null
            })
        return popularMovies
    }

    private fun insertLocal(responsePopular: ResponsePopular?) {
        responsePopular?.let {
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

    fun getLocalData(page : Int) : LiveData<ResponsePopular>{
        return moviesDao.getAll(page)
    }

    @SuppressLint("CheckResult")
    fun getTrailersMovies(movieId : String) : LiveData<ResponseVideo>{
        val moviesTrailer = MutableLiveData<ResponseVideo>()
        apiService?.getMoviesTrailers(movieId,BuildConfig.API_KEY)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribe({
                moviesTrailer.value = it
            },{
                moviesTrailer.value = null
            })
        return moviesTrailer
    }
}