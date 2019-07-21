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
}