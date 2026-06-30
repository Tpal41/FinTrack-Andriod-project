package com.fintrack.app.domain.repository

import com.fintrack.app.domain.model.Budget
import com.fintrack.app.domain.model.FinanceTransaction
import com.fintrack.app.domain.model.RecurringTransaction
import com.fintrack.app.domain.model.SavingsGoal
import com.fintrack.app.domain.model.TransactionQuery
import com.fintrack.app.domain.model.UserSettings
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun observeTransactions(query: TransactionQuery = TransactionQuery()): Flow<List<FinanceTransaction>>
    fun observeTransaction(id: Long): Flow<FinanceTransaction?>
    suspend fun getTransaction(id: Long): FinanceTransaction?
    suspend fun saveTransaction(transaction: FinanceTransaction): Long
    suspend fun deleteTransaction(id: Long)
}

interface BudgetRepository {
    fun observeBudgets(): Flow<List<Budget>>
    fun observeBudgetsForMonth(month: Int, year: Int): Flow<List<Budget>>
    suspend fun saveBudget(budget: Budget): Long
    suspend fun deleteBudget(id: Long)
}

interface GoalRepository {
    fun observeGoals(): Flow<List<SavingsGoal>>
    suspend fun saveGoal(goal: SavingsGoal): Long
    suspend fun deleteGoal(id: Long)
}

interface RecurringTransactionRepository {
    fun observeAll(): Flow<List<RecurringTransaction>>
    fun observeActive(): Flow<List<RecurringTransaction>>
    suspend fun getById(id: Long): RecurringTransaction?
    suspend fun save(recurringTransaction: RecurringTransaction): Long
    suspend fun delete(id: Long)
    suspend fun updateNextDate(id: Long, nextDate: Long)
    suspend fun getPendingTransactions(currentTime: Long): List<RecurringTransaction>
}

interface SettingsRepository {
    fun observeSettings(): Flow<UserSettings>
    suspend fun updateDarkMode(enabled: Boolean)
    suspend fun updateCurrency(currency: String)
    suspend fun updateNotifications(enabled: Boolean)
    suspend fun updateAppLock(enabled: Boolean)
}

interface ExportRepository {
    suspend fun generateTransactionsCsv(): String
}
