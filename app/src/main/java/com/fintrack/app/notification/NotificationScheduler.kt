package com.fintrack.app.notification

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NotificationScheduler @Inject constructor(
    private val workManager: WorkManager
) {
    fun scheduleDailyReminder() {
        val request = PeriodicWorkRequestBuilder<FinanceReminderWorker>(1, TimeUnit.DAYS).build()
        workManager.enqueueUniquePeriodicWork(
            "daily_expense_reminder",
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    fun cancelReminders() {
        workManager.cancelUniqueWork("daily_expense_reminder")
    }
}
