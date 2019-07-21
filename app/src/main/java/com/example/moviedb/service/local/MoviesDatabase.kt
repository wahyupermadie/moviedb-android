package com.example.moviedb.service.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.moviedb.service.model.popular.ResponseMovies
import com.example.moviedb.service.model.popular.ResultsItem
import com.example.moviedb.service.model.popular.favorite.FavoriteItem
import com.example.moviedb.utils.DatabaseConverter

@Database(entities = [ResultsItem::class, ResponseMovies::class,
    FavoriteItem::class], version = 1)
@TypeConverters(DatabaseConverter::class)
abstract class MoviesDatabase  : RoomDatabase() {
    abstract fun movieDao() : MoviesDao
    abstract fun topRatedDao() : FavoritDao
}