package com.wepe.moviedb.service.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wepe.moviedb.BuildConfig
import com.wepe.moviedb.service.local.MoviesDao
import com.wepe.moviedb.service.model.popular.ResponseMovies
import com.wepe.moviedb.service.model.popular.ResultsItem
import com.wepe.moviedb.service.model.popular.video.ResponseVideo
import com.wepe.moviedb.service.network.ApiService
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MovieRepository(
    private val apiService: ApiService?,
    private val moviesDao: MoviesDao
){

    @SuppressLint("CheckResult")
    fun getPopularMovie(page : Int): LiveData<ResponseMovies>?{
        val popularMovies = MutableLiveData<ResponseMovies>()
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

    private fun insertLocal(responseMovies: ResponseMovies?) {
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

    fun getLocalData() : LiveData<List<ResultsItem>> {
        return moviesDao.getAll()
    }

    fun getLocalTopRatedData() : LiveData<List<ResultsItem>> {
        return moviesDao.getTopRatedLocalData()
    }

    fun getSingleLocalData(id : Int) : Single<ResultsItem> {
        return moviesDao.getItem(id)
    }

    fun updateFavorite(id : Int, value : Boolean): Completable {
        return moviesDao.setFavorite(value, id)
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

    @SuppressLint("CheckResult")
    fun getTopRatedMovie(page: Int): LiveData<ResponseMovies>? {
        val popularMovies = MutableLiveData<ResponseMovies>()
        apiService?.getTopRatedMovies(BuildConfig.API_KEY, page)
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

}