package com.example.moviedb.koin

import androidx.room.Room
import com.example.moviedb.service.local.MoviesDatabase
import com.example.moviedb.ui.detail.DetailMovieActivityViewModel
import com.example.moviedb.ui.detailTvShow.DetailTvShowVM
import com.example.moviedb.ui.popular.PopularViewModel
import com.example.moviedb.ui.toprated.TopRatedViewModel
import com.example.moviedb.ui.tvshow.TvShowViewModel
import com.google.gson.Gson
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { Room.databaseBuilder(get(), MoviesDatabase::class.java, "movies_database").fallbackToDestructiveMigration().build() }
    single { get<MoviesDatabase>().movieDao() }
    viewModel { PopularViewModel(get(), get() )}
    viewModel { TopRatedViewModel(get(), get()) }
    viewModel { DetailMovieActivityViewModel(get(), get()) }
    viewModel { TvShowViewModel(get()) }
    viewModel { DetailTvShowVM(get()) }

}