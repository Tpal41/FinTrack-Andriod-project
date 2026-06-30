package com.fintrack.app.di

import android.content.Context
import com.fintrack.app.data.export.ExportManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExportModule {
    @Provides
    @Singleton
    fun provideExportManager(@ApplicationContext context: Context): ExportManager = ExportManager(context)
}
