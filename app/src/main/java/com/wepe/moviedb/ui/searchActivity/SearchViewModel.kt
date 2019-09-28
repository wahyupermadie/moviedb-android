package com.wepe.moviedb.ui.searchActivity

import androidx.lifecycle.*
import com.wepe.moviedb.service.model.popular.ResponseMovies
import com.wepe.moviedb.service.model.popular.tvShow.ResponseTvShow
import com.wepe.moviedb.utils.Resources
import com.wepe.moviedb.utils.ViewExtention

class SearchViewModel(
    private val searchUseCases: SearchUseCases
) : ViewModel(){
    private lateinit var viewExtention : ViewExtention
    private lateinit var context: LifecycleOwner
    fun setListener(viewExtention: ViewExtention, context: LifecycleOwner){
        this.viewExtention = viewExtention
        this.context = context
    }

    private val _movies = MutableLiveData<ResponseMovies>()
    val movies : MutableLiveData<ResponseMovies> get() = _movies

    private val _tv = MutableLiveData<ResponseTvShow>()
    val tv : MutableLiveData<ResponseTvShow> get() = _tv


    fun searchMovie(param : String){
        searchUseCases.searchMovies(param).observe(context, Observer {
            when(it.status){
                Resources.Status.SUCCESS -> _movies.value = it.data
                Resources.Status.ERROR -> viewExtention.onError(it.message.toString())
            }
        })
    }

    fun searchTv(param : String){
        searchUseCases.searchTv(param).observe(context, Observer {
            when(it.status){
                Resources.Status.SUCCESS -> _tv.value = it.data
                Resources.Status.ERROR -> viewExtention.onError(it.message.toString())
            }
        })
    }
}