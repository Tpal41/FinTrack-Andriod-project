package com.fintrack.app.core.work

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object WorkScheduler {
    private const val RECURRING_WORK_NAME = "process_recurring_transactions"

    fun scheduleRecurringTransactionProcessing(context: Context) {
        val workRequest = PeriodicWorkRequestBuilder<RecurringTransactionWorker>(
            1, TimeUnit.HOURS // Check every hour
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            RECURRING_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}
