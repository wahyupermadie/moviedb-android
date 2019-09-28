package com.wepe.moviedb.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.wepe.moviedb.service.local.MoviesDao
import com.wepe.moviedb.service.model.popular.ResultsItem

class FavoriteViewModel(private val moviesDao: MoviesDao) : ViewModel(){

    fun fetchData() : LiveData<List<ResultsItem>>{
        return moviesDao.getFavorite(true)
    }
}