package com.wepe.moviedb.ui.detailTvShow

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.wepe.moviedb.service.local.TvShowDao
import com.wepe.moviedb.service.model.popular.tvShow.ResultsItem
import com.wepe.moviedb.service.model.popular.video.ResponseVideo
import com.wepe.moviedb.service.network.ApiService
import com.wepe.moviedb.service.repository.TvShowRepository
import io.reactivex.Completable
import io.reactivex.Single

class DetailTvShowVM(apiService: ApiService?, tvShowDao: TvShowDao) : ViewModel(){
    private val repository = apiService?.let { TvShowRepository(it, tvShowDao) }

    fun getTrailers(tvId: String) : LiveData<ResponseVideo>? {
        return repository?.getTrailersTv(tvId)
    }

    fun getSingleData(id : Int) : Single<ResultsItem>? {
        return repository?.getSingleLocalData(id)
    }

    fun updateFavorite(id : Int, value : Boolean): Completable? {
        return repository?.updateFavorite(id, value)
    }

}