package com.wepe.myfav.utils

import android.net.Uri
import java.text.SimpleDateFormat
import java.util.*

object Constant {
    const val IMAGE_URL = "https://image.tmdb.org/t/p/w342"
    const val AUTHORITY = "com.wepe.moviedb"
    const val LIST_DATA_KEY = "LIST_DATA"
    const val LIST_STATE_KEY = "RECYCLER_VIEW_STATE"
    val URI_MOVIES = Uri.parse(
        "content://$AUTHORITY/result_item"
    )
    val URI_TV = Uri.parse(
        "content://$AUTHORITY/results_show"
    )
    fun dateFormat(value : String) : String{

        val sdf = SimpleDateFormat("yyyy-mm-dd", Locale.US)
        val date = sdf.parse(value)

        val newSdf = SimpleDateFormat("dd-MMM-yyyy", Locale.US)
        return newSdf.format(date!!)
    }
}