package com.example.moviedb.service.worker

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.moviedb.BuildConfig
import com.example.moviedb.koin.apiService
import com.example.moviedb.koin.createOkHttpClient
import com.example.moviedb.service.model.popular.ResponseMovies
import com.example.moviedb.service.network.ApiService
import com.example.moviedb.utils.NotificationHandler
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class NewMoviesWorker(ctx: Context, param: WorkerParameters) : Worker(ctx, param){
    private val calendar = Calendar.getInstance()
    override fun doWork(): Result {
        val appContext = applicationContext
        return try {
            if (checkTime()){
                checkMovies(appContext)
            }
            Result.success()
        }catch (e : Exception){
            Result.failure()
        }
    }

    @SuppressLint("CheckResult")
    private fun checkMovies(context: Context) {
        val today = calendar.time
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val date = sdf.format(today)

        apiService<ApiService>(okHttpClient = createOkHttpClient(), url = BuildConfig.BASE_URL)
            .getNewMovies(BuildConfig.API_KEY, date,date)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                Log.d("WORKER_RESPONSE", "SUCCESS $it")
                sendNotifications(it, context)
            },{
                Log.d("WORKER_RESPONSE", "ERROR $it")
                Result.retry()
            })
    }

    private fun sendNotifications(it: ResponseMovies?, context: Context) {
        it?.results?.let {
            NotificationHandler.sendNotification(context = context, isGroup = true, movieList = it)
        }
    }

    private fun checkTime(): Boolean{
        return calendar.get(Calendar.HOUR_OF_DAY).toString() == "23"
    }
}