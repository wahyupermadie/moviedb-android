package com.example.moviedb.utils

import android.content.Context
import android.app.NotificationManager
import android.app.NotificationChannel
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationCompat
import com.example.moviedb.R
import com.example.moviedb.utils.Constant.CHANNEL_ID
import android.app.PendingIntent
import android.content.Intent
import com.example.moviedb.service.model.popular.ResultsItem
import com.example.moviedb.ui.MainActivity

object NotificationHandler {

    fun sendNotification(message: String? = "", context: Context, isGroup: Boolean = false, movieList : List<ResultsItem>){
        val sizeList = movieList.size - 1
        var mBuilder: NotificationCompat.Builder? = null
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the Noti"ficationChannel class is new and not in the support library
            val name = "DICODING MOVIES APPS"
            val description = "Movies apps that show the schedules of movies and tv show"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID,  name, importance)
            channel.description = description

            // Add the channel
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }

        // Create the notification
       if (!isGroup){
           mBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
               .setSmallIcon(R.drawable.ic_add_favorite)
               .setContentTitle(Constant.NOTIFICATION_TITLE)
               .setContentText(message)
               .setPriority(NotificationCompat.PRIORITY_HIGH)
               .setVibrate(LongArray(0))
               .setContentIntent(pendingIntent)
       }else{
           val inboxStyle = NotificationCompat.InboxStyle()
               .addLine("New Film " + movieList[sizeList].originalTitle)
               .addLine("New Film " + movieList[sizeList-1].originalTitle)
               .addLine("New Film " + movieList[sizeList-2].originalTitle)
               .addLine("New Film " + movieList[sizeList-3].originalTitle)
               .addLine("New Film " + movieList[sizeList-4].originalTitle)
               .setBigContentTitle(movieList.size.toString() + " new Film Today")
               .setSummaryText("wahyu.film@dicoding")

           mBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
               .setSmallIcon(R.drawable.ic_add_favorite)
               .setContentTitle(movieList.size.toString() + " new Film Today")
               .setContentText("wahyu.film@dicoding")
               .setGroup("movies_key")
               .setGroupSummary(true)
               .setStyle(inboxStyle)
               .setPriority(NotificationCompat.PRIORITY_HIGH)
               .setVibrate(LongArray(0))
               .setContentIntent(pendingIntent)
       }

        // Show the notification
        NotificationManagerCompat.from(context).notify(1, mBuilder.build())
    }
}