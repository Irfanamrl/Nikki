package com.example.nikki

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class ReminderBroadcast : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        var builder = NotificationCompat.Builder(context, "notifyNikki")
            .setSmallIcon(R.drawable.title_16px)
            .setContentTitle("Diary Reminder")
            .setContentText("It's time to write your Diary for today.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        var notificationManager : NotificationManagerCompat = NotificationManagerCompat.from(context)

        notificationManager.notify(200, builder.build())
    }
}