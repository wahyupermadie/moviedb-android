package com.wepe.moviedb.utils

import android.content.Context
import android.net.ConnectivityManager
import java.text.SimpleDateFormat
import java.util.*

object Constant{
    const val NOTIFICATION_TITLE = "Hello Notification Here"
    const val IMAGE_URL = "https://image.tmdb.org/t/p/w342"
    const val BACKDROP_URL = "https://image.tmdb.org/t/p/w500"
    const val LIST_DATA_KEY = "LIST_DATA"
    const val LIST_STATE_KEY = "RECYCLER_VIEW_STATE"
    const val LANGUAGE = "en-US"
    const val CHANNEL_ID = "DICODING_WAHYU"
    const val SETTING_REMINDER_OPEN = "BUKALAGI"
    const val SETTING_REMINDER_NEW = "FILMBARU"
    const val MOVIES_NEW = "NEW_MOVIES"
    const val REMINDER = "REMINDER"
    const val TIME_FORMAT = "HH:mm"
    const val DATE_FORMAT = "yyyy-MM-dd"

    fun dateFormat(value : String) : String{

        val sdf = SimpleDateFormat("yyyy-mm-dd", Locale.US)
        val date = sdf.parse(value)

        val newSdf = SimpleDateFormat("dd-MMM-yyyy", Locale.US)
        return newSdf.format(date!!)
    }

    fun isConnected(ctx: Context?) : Boolean {
        val connectivityManager = ctx?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetwork != null
    }
}