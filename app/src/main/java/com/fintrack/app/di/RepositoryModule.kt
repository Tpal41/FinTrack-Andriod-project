package com.fintrack.app.di

import com.fintrack.app.data.repository.BudgetRepositoryImpl
import com.fintrack.app.data.repository.GoalRepositoryImpl
import com.fintrack.app.data.repository.RecurringTransactionRepositoryImpl
import com.fintrack.app.data.repository.SettingsRepositoryImpl
import com.fintrack.app.data.repository.TransactionRepositoryImpl
import com.fintrack.app.domain.repository.BudgetRepository
import com.fintrack.app.domain.repository.GoalRepository
import com.fintrack.app.domain.repository.RecurringTransactionRepository
import com.fintrack.app.domain.repository.SettingsRepository
import com.fintrack.app.domain.repository.TransactionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindTransactionRepository(impl: TransactionRepositoryImpl): TransactionRepository

    @Binds
    @Singleton
    abstract fun bindBudgetRepository(impl: BudgetRepositoryImpl): BudgetRepository

    @Binds
    @Singleton
    abstract fun bindGoalRepository(impl: GoalRepositoryImpl): GoalRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository

    @Binds
    @Singleton
    abstract fun bindRecurringTransactionRepository(impl: RecurringTransactionRepositoryImpl): RecurringTransactionRepository

    @Binds
    @Singleton
    abstract fun bindExportRepository(impl: com.fintrack.app.data.repository.ExportRepositoryImpl): com.fintrack.app.domain.repository.ExportRepository
}
