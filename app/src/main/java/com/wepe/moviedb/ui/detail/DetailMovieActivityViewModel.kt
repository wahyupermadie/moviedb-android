package com.wepe.moviedb.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.wepe.moviedb.service.local.MoviesDao
import com.wepe.moviedb.service.model.popular.ResultsItem
import com.wepe.moviedb.service.model.popular.video.ResponseVideo
import com.wepe.moviedb.service.network.ApiService
import com.wepe.moviedb.service.repository.MovieRepository
import io.reactivex.Completable
import io.reactivex.Single

class DetailMovieActivityViewModel(apiService: ApiService?, moviesDao: MoviesDao) : ViewModel(){
    private val repository = MovieRepository(apiService, moviesDao)

    fun getTrailers(movieId: String) : LiveData<ResponseVideo>{
        return repository.getTrailersMovies(movieId)
    }

    fun getSingleData(id : Int) : Single<ResultsItem> {
        return repository.getSingleLocalData(id)
    }

    fun updateFavorite(id : Int, value : Boolean): Completable {
        return repository.updateFavorite(id, value)
    }
}