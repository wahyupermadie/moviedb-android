package com.example.moviedb.utils

import androidx.room.TypeConverter
import com.example.moviedb.service.model.popular.ResultsItem
import com.google.gson.Gson

class DatabaseConverter {
    @TypeConverter
    fun listToJson(value: List<ResultsItem>?): String {

        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): List<ResultsItem>? {

        val objects = Gson().fromJson(value, Array<ResultsItem>::class.java) as Array<ResultsItem>
        return objects.toList()
    }

    @TypeConverter
    fun listToJsonTopRated(value: List<com.example.moviedb.service.model.popular.favorite.FavoriteItem>?): String {

        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToListTopRated(value: String): List<com.example.moviedb.service.model.popular.favorite.FavoriteItem>? {

        val objects = Gson().fromJson(value, Array<com.example.moviedb.service.model.popular.favorite.FavoriteItem>::class.java) as Array<com.example.moviedb.service.model.popular.favorite.FavoriteItem>
        return objects.toList()
    }
}