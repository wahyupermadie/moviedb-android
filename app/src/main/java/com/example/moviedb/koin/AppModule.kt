package com.example.moviedb.koin

import androidx.room.Room
import com.example.moviedb.service.local.MoviesDatabase
import com.example.moviedb.ui.popular.PopularViewModel
import com.example.moviedb.ui.detail.DetailActivityViewModel
import com.example.moviedb.ui.toprated.TopRatedViewModel
import com.google.gson.Gson
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { Gson() }
    single { Room.databaseBuilder(get(), MoviesDatabase::class.java, "movies_database").fallbackToDestructiveMigration().build() }
    single { get<MoviesDatabase>().movieDao() }
    single { get<MoviesDatabase>().topRatedDao() }
    viewModel { PopularViewModel(get(), get(), get()) }
    viewModel { TopRatedViewModel(get(), get(), get()) }
    viewModel { DetailActivityViewModel(get(), get(), get()) }
}