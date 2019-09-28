package com.wepe.moviedb.service.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wepe.moviedb.service.model.popular.ResponseMovies
import com.wepe.moviedb.service.model.popular.ResultsItem

@Database(entities = [ResultsItem::class, ResponseMovies::class,
    com.wepe.moviedb.service.model.popular.tvShow.ResultsItem::class], version = 4)
abstract class MoviesDatabase  : RoomDatabase() {
    abstract fun movieDao() : MoviesDao
    abstract fun tvShowDao() : TvShowDao
}