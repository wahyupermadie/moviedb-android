package com.example.moviedb.service.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.moviedb.service.model.popular.ResponsePopular
import com.example.moviedb.service.model.popular.ResultsItem
import com.example.moviedb.utils.DatabaseConverter

@Database(entities = [ResultsItem::class, ResponsePopular::class], version = 1)
@TypeConverters(DatabaseConverter::class)
abstract class MoviesDatabase  : RoomDatabase() {
    abstract fun movieDao() : MoviesDao
}