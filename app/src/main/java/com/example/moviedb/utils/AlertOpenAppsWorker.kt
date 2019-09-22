package com.example.moviedb.utils

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.*

class AlertOpenAppsWorker(ctx: Context, param: WorkerParameters) : Worker(ctx, param){
    override fun doWork(): Result {
        val appContext = applicationContext
        return try {
            Log.d("DATA_GUE","START")
            if (checkTime()){
                NotificationHandler.sendNotification("Hello Bosku", appContext)
            }
            Result.success()
        }catch (e : Exception){
            Log.d("DATA_GUE","FAIL "+e.localizedMessage)
            Result.failure()
        }
    }

    private fun checkTime(): Boolean{
        val locale = Locale("id", "ID")
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.HOUR_OF_DAY).toString() == "24"
    }
}