package com.fintrack.app

import android.app.Application
import com.fintrack.app.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class FinTrackApplication : Application(), Configuration.Provider {
    @Inject lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        com.fintrack.app.core.work.WorkScheduler.scheduleRecurringTransactionProcessing(this)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
