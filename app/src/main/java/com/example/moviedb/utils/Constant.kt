package com.example.moviedb.utils

import java.text.SimpleDateFormat
import java.util.*

object Constant{
    const val IMAGE_URL = "https://image.tmdb.org/t/p/w342"
    const val VIDEO_URL = "https://www.youtube.com/watch?v="
    fun dateFormat(value : String) : String{

        val sdf = SimpleDateFormat("yyyy-mm-dd", Locale.US);
        val date = sdf.parse(value)

        val newSdf = SimpleDateFormat("dd-MMM-yyyy", Locale.US)
        return newSdf.format(date!!)
    }
}