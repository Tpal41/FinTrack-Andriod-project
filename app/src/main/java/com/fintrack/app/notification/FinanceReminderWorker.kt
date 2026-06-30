package com.fintrack.app.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.fintrack.app.R
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class FinanceReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        ensureChannel()
        if (
            android.os.Build.VERSION.SDK_INT >= 33 &&
            ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            return Result.success()
        }
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle("FinTrack reminder")
            .setContentText("Log today's expenses and keep your budget accurate.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()
        NotificationManagerCompat.from(applicationContext).notify(1001, notification)
        return Result.success()
    }

    private fun ensureChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "Finance reminders", NotificationManager.IMPORTANCE_DEFAULT)
            val manager = applicationContext.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private companion object {
        const val CHANNEL_ID = "finance_reminders"
    }
}
