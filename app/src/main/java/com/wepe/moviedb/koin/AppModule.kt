package com.wepe.moviedb.koin

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.wepe.moviedb.BuildConfig
import com.wepe.moviedb.service.local.MoviesDatabase
import com.wepe.moviedb.ui.detail.DetailMovieActivityViewModel
import com.wepe.moviedb.ui.detailTvShow.DetailTvShowVM
import com.wepe.moviedb.ui.popular.PopularViewModel
import com.wepe.moviedb.ui.toprated.TopRatedViewModel
import com.wepe.moviedb.ui.tvshow.TvShowViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { Room.databaseBuilder(get(), MoviesDatabase::class.java, "movies_database").fallbackToDestructiveMigration().build() }
    single { get<MoviesDatabase>().movieDao() }
    single { get<MoviesDatabase>().tvShowDao() }
    single { provideSettingsPreferences(androidApplication()) }
    viewModel { PopularViewModel(get(), get() )}
    viewModel { TopRatedViewModel(get(), get()) }
    viewModel { DetailMovieActivityViewModel(get(), get()) }
    viewModel { TvShowViewModel(get(), get()) }
    viewModel { DetailTvShowVM(get(), get()) }

}

private const val PREFERENCES_FILE_KEY = BuildConfig.APPLICATION_ID

private fun provideSettingsPreferences(app: Application): SharedPreferences =
    app.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)