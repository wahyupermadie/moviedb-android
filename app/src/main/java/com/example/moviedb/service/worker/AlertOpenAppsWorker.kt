package com.example.moviedb.service.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.moviedb.utils.NotificationHandler
import java.util.*

class AlertOpenAppsWorker(ctx: Context, param: WorkerParameters) : Worker(ctx, param){
    private val calendar = Calendar.getInstance()
    override fun doWork(): Result {
        val appContext = applicationContext
        return try {
            if (checkTime()){
                NotificationHandler.sendNotification(
                    message = "Yuk buka aplikasi Movies App Sekarang",
                    context = appContext, isGroup = false, movieList = arrayListOf()
                )
            }
            Result.success()
        }catch (e : Exception){
            Result.failure()
        }
    }

    private fun checkTime(): Boolean{
        return calendar.get(Calendar.HOUR_OF_DAY).toString() == "23"
    }
}