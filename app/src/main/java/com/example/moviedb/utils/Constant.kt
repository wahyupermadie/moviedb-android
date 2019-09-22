package com.example.moviedb.utils

import android.content.Context
import android.net.ConnectivityManager
import java.text.SimpleDateFormat
import java.util.*

object Constant{
    const val IMAGE_URL = "https://image.tmdb.org/t/p/w342"
    const val LIST_DATA_KEY = "LIST_DATA"
    const val LIST_STATE_KEY = "RECYCLER_VIEW_STATE"
    fun dateFormat(value : String) : String{

        val sdf = SimpleDateFormat("yyyy-mm-dd", Locale.US);
        val date = sdf.parse(value)

        val newSdf = SimpleDateFormat("dd-MMM-yyyy", Locale.US)
        return newSdf.format(date!!)
    }

    fun isConnected(ctx: Context?) : Boolean {
        val connectivityManager = ctx?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetwork != null
    }
}