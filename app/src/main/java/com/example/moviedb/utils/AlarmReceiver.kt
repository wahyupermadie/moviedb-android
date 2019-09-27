package com.example.moviedb.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import android.app.AlarmManager
import android.widget.Toast
import android.app.PendingIntent
import android.util.Log
import androidx.work.ListenableWorker
import com.example.moviedb.BuildConfig
import com.example.moviedb.koin.apiService
import com.example.moviedb.koin.createOkHttpClient
import com.example.moviedb.service.model.popular.ResponseMovies
import com.example.moviedb.service.network.ApiService
import com.example.moviedb.utils.Constant.DATE_FORMAT
import com.example.moviedb.utils.Constant.MOVIES_NEW
import com.example.moviedb.utils.Constant.REMINDER
import com.example.moviedb.utils.Constant.TIME_FORMAT
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class AlarmReceiver : BroadcastReceiver(){
    private val EXTRA_TYPE = "type"
    private val ID_REPEATING_REMINDER = 101
    private val ID_REPEATING_NEW_MOVIES = 105
    private val calendar = Calendar.getInstance()
    override fun onReceive(p0: Context?, intent: Intent?) {
        val type = intent?.getStringExtra(EXTRA_TYPE)
        val today = calendar.time
        val sdf = SimpleDateFormat(DATE_FORMAT, Locale.US)
        val date = sdf.format(today)
        when(type){
                MOVIES_NEW -> {
                apiService<ApiService>(okHttpClient = createOkHttpClient(), url = BuildConfig.BASE_URL)
                    .getNewMovies(BuildConfig.API_KEY, date,date)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        p0?.let { it1 -> sendNotifications(it, it1) }
                    },{
                        ListenableWorker.Result.retry()
                    })
            }
            REMINDER ->{
                p0?.let {
                    NotificationHandler.sendNotification(
                        message = "Yuk buka aplikasi Movies App Sekarang",
                        context = it, isGroup = false, movieList = arrayListOf()
                    )
                }
            }
        }
    }

    private fun sendNotifications(it: ResponseMovies?, context: Context) {
        it?.results?.let {
            NotificationHandler.sendNotification(context = context, isGroup = true, movieList = it)
        }
    }

    fun setRepeatingAlarm(context: Context, type: String, time: String){

        if (isDateInvalid(time, TIME_FORMAT)) return
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(EXTRA_TYPE, type)

        val alarmTime = time.split(":")
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(alarmTime[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(alarmTime[1]))
        calendar.set(Calendar.SECOND, 0)

        if (type == MOVIES_NEW){
            val pendingIntent = PendingIntent.getBroadcast(context,ID_REPEATING_NEW_MOVIES, intent, 0)
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }else{
            val pendingIntent = PendingIntent.getBroadcast(context,ID_REPEATING_REMINDER , intent, 0)
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }
        Toast.makeText(context, "Repeating alarm set up", Toast.LENGTH_SHORT).show()
    }

    private fun isDateInvalid(date: String, format: String): Boolean {
        return try {
            val df = SimpleDateFormat(format, Locale.getDefault())
            df.isLenient = false
            df.parse(date)
            false
        } catch (e: ParseException) {
            true
        }

    }

    fun cancelAlarm(context: Context, type: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val requestCode =
            if (type == MOVIES_NEW) ID_REPEATING_NEW_MOVIES else ID_REPEATING_REMINDER
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()

        alarmManager.cancel(pendingIntent)

        Toast.makeText(context, "Repeating alarm dibatalkan", Toast.LENGTH_SHORT).show()
    }
}