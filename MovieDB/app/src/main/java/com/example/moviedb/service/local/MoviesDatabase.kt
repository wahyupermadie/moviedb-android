package com.example.moviedb.service.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.moviedb.service.model.popular.ResponseMovies
import com.example.moviedb.service.model.popular.ResultsItem

@Database(entities = [ResultsItem::class, ResponseMovies::class], version = 3)
abstract class MoviesDatabase  : RoomDatabase() {
    abstract fun movieDao() : MoviesDao
}