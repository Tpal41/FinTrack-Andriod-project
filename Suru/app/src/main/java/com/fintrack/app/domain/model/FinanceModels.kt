package com.fintrack.app.domain.model

data class FinanceTransaction(
    val id: Long = 0,
    val type: TransactionType,
    val amount: Double,
    val category: String,
    val note: String,
    val paymentMethod: String,
    val date: Long,
    val createdAt: Long
)

data class Budget(
    val id: Long = 0,
    val category: String,
    val budgetLimit: Double,
    val month: Int,
    val year: Int
)

data class SavingsGoal(
    val id: Long = 0,
    val title: String,
    val targetAmount: Double,
    val savedAmount: Double,
    val targetDate: Long
)

data class UserSettings(
    val darkMode: Boolean = true,
    val currency: String = "₹",
    val notificationsEnabled: Boolean = true,
    val appLockEnabled: Boolean = false
)

data class DashboardSummary(
    val balance: Double,
    val totalIncome: Double,
    val totalExpenses: Double,
    val monthlySavings: Double,
    val healthIndex: String,
    val recentTransactions: List<FinanceTransaction>,
    val categoryExpenses: List<CategoryTotal>,
    val monthlyTotals: List<MonthlyTotal>
)

data class CategoryTotal(
    val category: String,
    val total: Double
)

data class MonthlyTotal(
    val month: Int,
    val year: Int,
    val income: Double,
    val expense: Double
)

data class BudgetUsage(
    val budget: Budget,
    val spent: Double,
    val remaining: Double,
    val percentageUsed: Float,
    val isExceeded: Boolean
)

data class SmartInsight(
    val title: String,
    val description: String,
    val severity: InsightSeverity
)

enum class InsightSeverity {
    POSITIVE,
    WARNING,
    NEUTRAL
}

data class GoalProgress(
    val goal: SavingsGoal,
    val progress: Float,
    val remainingAmount: Double,
    val estimatedDailySaving: Double
)

data class RecurringTransaction(
    val id: Long = 0,
    val title: String,
    val amount: Double,
    val category: String,
    val frequency: Frequency,
    val nextDate: Long,
    val type: TransactionType = TransactionType.EXPENSE,
    val isActive: Boolean = true
)

enum class Frequency {
    DAILY, WEEKLY, MONTHLY, YEARLY
}
