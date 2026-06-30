package com.fintrack.app.domain.usecase

import com.fintrack.app.core.util.currentMonth
import com.fintrack.app.core.util.currentYear
import com.fintrack.app.domain.model.Budget
import com.fintrack.app.domain.model.BudgetUsage
import com.fintrack.app.domain.model.CategoryTotal
import com.fintrack.app.domain.model.DashboardSummary
import com.fintrack.app.domain.model.FinanceTransaction
import com.fintrack.app.domain.model.GoalProgress
import com.fintrack.app.domain.model.InsightSeverity
import com.fintrack.app.domain.model.MonthlyTotal
import com.fintrack.app.domain.model.SavingsGoal
import com.fintrack.app.domain.model.SmartInsight
import com.fintrack.app.domain.model.TransactionQuery
import com.fintrack.app.domain.model.TransactionType
import com.fintrack.app.domain.repository.BudgetRepository
import com.fintrack.app.domain.repository.GoalRepository
import com.fintrack.app.domain.repository.SettingsRepository
import com.fintrack.app.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.max

class ObserveTransactionsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(query: TransactionQuery = TransactionQuery()): Flow<List<FinanceTransaction>> =
        repository.observeTransactions(query)
}

class SaveTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transaction: FinanceTransaction): Result<Long> = runCatching {
        require(transaction.amount > 0) { "Amount must be greater than zero." }
        require(transaction.category.isNotBlank()) { "Category is required." }
        repository.saveTransaction(transaction)
    }
}

class DeleteTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(id: Long) = repository.deleteTransaction(id)
}

class ObserveDashboardUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(): Flow<DashboardSummary> =
        transactionRepository.observeTransactions().map { transactions ->
            val income = transactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
            val expenses = transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
            val month = currentMonth()
            val year = currentYear()
            val monthTransactions = transactions.filter { it.month() == month && it.year() == year }
            val monthlyIncome = monthTransactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
            val monthlyExpense = monthTransactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
            val savings = monthlyIncome - monthlyExpense
            
            val health = when {
                monthlyIncome <= 0 -> "N/A"
                savings / monthlyIncome >= 0.3 -> "Elite"
                savings / monthlyIncome >= 0.15 -> "Optimal"
                savings > 0 -> "Stable"
                else -> "Warning"
            }

            DashboardSummary(
                balance = income - expenses,
                totalIncome = income,
                totalExpenses = expenses,
                monthlySavings = savings,
                healthIndex = health,
                recentTransactions = transactions.take(6),
                categoryExpenses = transactions
                    .filter { it.type == TransactionType.EXPENSE && it.month() == month && it.year() == year }
                    .groupBy { it.category }
                    .map { CategoryTotal(it.key, it.value.sumOf { item -> item.amount }) }
                    .sortedByDescending { it.total },
                monthlyTotals = transactions.toMonthlyTotals()
            )
        }
}

class ObserveBudgetUsageUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(month: Int = currentMonth(), year: Int = currentYear()): Flow<List<BudgetUsage>> =
        combine(
            budgetRepository.observeBudgetsForMonth(month, year),
            transactionRepository.observeTransactions()
        ) { budgets, transactions ->
            budgets.map { budget ->
                val spent = transactions
                    .filter {
                        it.type == TransactionType.EXPENSE &&
                            it.category == budget.category &&
                            it.month() == budget.month &&
                            it.year() == budget.year
                    }
                    .sumOf { it.amount }
                val percentage = if (budget.budgetLimit <= 0) 0f else (spent / budget.budgetLimit).toFloat()
                BudgetUsage(
                    budget = budget,
                    spent = spent,
                    remaining = budget.budgetLimit - spent,
                    percentageUsed = percentage.coerceAtLeast(0f),
                    isExceeded = spent > budget.budgetLimit
                )
            }
        }
}

class SaveBudgetUseCase @Inject constructor(
    private val repository: BudgetRepository
) {
    suspend operator fun invoke(budget: Budget): Result<Long> = runCatching {
        require(budget.budgetLimit > 0) { "Budget limit must be greater than zero." }
        require(budget.category.isNotBlank()) { "Category is required." }
        repository.saveBudget(budget)
    }
}

class ObserveGoalsUseCase @Inject constructor(
    private val repository: GoalRepository
) {
    operator fun invoke(): Flow<List<GoalProgress>> =
        repository.observeGoals().map { goals ->
            goals.map { goal ->
                val progress = if (goal.targetAmount <= 0) 0f else (goal.savedAmount / goal.targetAmount).toFloat()
                val remaining = max(0.0, goal.targetAmount - goal.savedAmount)
                val daysLeft = max(1L, TimeUnit.MILLISECONDS.toDays(goal.targetDate - System.currentTimeMillis()))
                GoalProgress(
                    goal = goal,
                    progress = progress.coerceIn(0f, 1f),
                    remainingAmount = remaining,
                    estimatedDailySaving = remaining / daysLeft
                )
            }
        }
}

class SaveGoalUseCase @Inject constructor(
    private val repository: GoalRepository
) {
    suspend operator fun invoke(goal: SavingsGoal): Result<Long> = runCatching {
        require(goal.title.isNotBlank()) { "Goal title is required." }
        require(goal.targetAmount > 0) { "Target amount must be greater than zero." }
        require(goal.savedAmount >= 0) { "Saved amount cannot be negative." }
        repository.saveGoal(goal.copy(savedAmount = goal.savedAmount.coerceAtMost(goal.targetAmount)))
    }
}

class ObserveSmartInsightsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(): Flow<List<SmartInsight>> =
        transactionRepository.observeTransactions().map { transactions ->
            buildInsights(transactions)
        }

    private fun buildInsights(transactions: List<FinanceTransaction>): List<SmartInsight> {
        val currentMonth = currentMonth()
        val currentYear = currentYear()
        val previous = Calendar.getInstance().apply {
            set(Calendar.MONTH, currentMonth - 2)
        }
        val previousMonth = previous.get(Calendar.MONTH) + 1
        val previousYear = previous.get(Calendar.YEAR)

        val currentExpenses = transactions.filter {
            it.type == TransactionType.EXPENSE && it.month() == currentMonth && it.year() == currentYear
        }
        val previousExpenses = transactions.filter {
            it.type == TransactionType.EXPENSE && it.month() == previousMonth && it.year() == previousYear
        }

        val insights = mutableListOf<SmartInsight>()
        val topCategory = currentExpenses.groupBy { it.category }
            .mapValues { it.value.sumOf { transaction -> transaction.amount } }
            .maxByOrNull { it.value }

        if (topCategory != null) {
            insights += SmartInsight(
                title = "Highest spending category",
                description = "${topCategory.key} leads this month with ${topCategory.value.toInt()} spent.",
                severity = InsightSeverity.NEUTRAL
            )
        }

        val currentTotal = currentExpenses.sumOf { it.amount }
        val previousTotal = previousExpenses.sumOf { it.amount }
        if (previousTotal > 0) {
            val delta = ((currentTotal - previousTotal) / previousTotal) * 100
            insights += if (delta > 0) {
                SmartInsight(
                    title = "Spending increased",
                    description = "You spent ${delta.toInt()}% more than last month.",
                    severity = InsightSeverity.WARNING
                )
            } else {
                SmartInsight(
                    title = "Spending improved",
                    description = "Expenses decreased by ${(-delta).toInt()}% compared with last month.",
                    severity = InsightSeverity.POSITIVE
                )
            }
        }

        if (insights.isEmpty()) {
            insights += SmartInsight(
                title = "Start tracking",
                description = "Add a few transactions to unlock personalized money insights.",
                severity = InsightSeverity.NEUTRAL
            )
        }
        return insights
    }
}

class ObserveSettingsUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke() = repository.observeSettings()
}

class ObserveRecurringTransactionsUseCase @Inject constructor(
    private val repository: com.fintrack.app.domain.repository.RecurringTransactionRepository
) {
    operator fun invoke(): Flow<List<com.fintrack.app.domain.model.RecurringTransaction>> = repository.observeAll()
}

class SaveRecurringTransactionUseCase @Inject constructor(
    private val repository: com.fintrack.app.domain.repository.RecurringTransactionRepository
) {
    suspend operator fun invoke(transaction: com.fintrack.app.domain.model.RecurringTransaction): Result<Long> = runCatching {
        require(transaction.title.isNotBlank()) { "Title is required." }
        require(transaction.amount > 0) { "Amount must be greater than zero." }
        require(transaction.category.isNotBlank()) { "Category is required." }
        repository.save(transaction)
    }
}

class DeleteRecurringTransactionUseCase @Inject constructor(
    private val repository: com.fintrack.app.domain.repository.RecurringTransactionRepository
) {
    suspend operator fun invoke(id: Long) = repository.delete(id)
}

private fun FinanceTransaction.month(): Int = Calendar.getInstance().apply {
    timeInMillis = date
}.get(Calendar.MONTH) + 1

private fun FinanceTransaction.year(): Int = Calendar.getInstance().apply {
    timeInMillis = date
}.get(Calendar.YEAR)

private fun List<FinanceTransaction>.toMonthlyTotals(): List<MonthlyTotal> =
    groupBy { it.year() to it.month() }
        .map { (key, transactions) ->
            MonthlyTotal(
                month = key.second,
                year = key.first,
                income = transactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount },
                expense = transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
            )
        }
        .sortedWith(compareBy<MonthlyTotal> { it.year }.thenBy { it.month })
        .takeLast(6)
