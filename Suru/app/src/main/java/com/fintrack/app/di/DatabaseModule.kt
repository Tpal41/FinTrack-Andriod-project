package com.fintrack.app.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.fintrack.app.data.local.FinTrackDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import androidx.datastore.preferences.core.PreferenceDataStoreFactory

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): FinTrackDatabase =
        Room.databaseBuilder(context, FinTrackDatabase::class.java, "fintrack.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideTransactionDao(database: FinTrackDatabase) = database.transactionDao()

    @Provides
    fun provideBudgetDao(database: FinTrackDatabase) = database.budgetDao()

    @Provides
    fun provideGoalDao(database: FinTrackDatabase) = database.goalDao()

    @Provides
    fun provideSettingsDao(database: FinTrackDatabase) = database.settingsDao()

    @Provides
    fun provideRecurringTransactionDao(database: FinTrackDatabase) = database.recurringTransactionDao()

    @Provides
    fun provideAttendanceDao(database: FinTrackDatabase) = database.attendanceDao()

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("fintrack_preferences")
        }
}
