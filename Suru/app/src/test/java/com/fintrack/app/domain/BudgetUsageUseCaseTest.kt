package com.fintrack.app.domain

import com.fintrack.app.domain.model.Budget
import com.fintrack.app.domain.model.FinanceTransaction
import com.fintrack.app.domain.model.TransactionQuery
import com.fintrack.app.domain.model.TransactionType
import com.fintrack.app.domain.repository.BudgetRepository
import com.fintrack.app.domain.repository.TransactionRepository
import com.fintrack.app.domain.usecase.ObserveBudgetUsageUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar

class BudgetUsageUseCaseTest {
    @Test
    fun budgetUsage_marksExceededWhenSpendingPassesLimit() = runTest {
        val date = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2026)
            set(Calendar.MONTH, Calendar.MAY)
        }.timeInMillis
        val useCase = ObserveBudgetUsageUseCase(
            budgetRepository = fakeBudgetRepository(Budget(1, "Food", 1000.0, 5, 2026)),
            transactionRepository = fakeTransactionRepository(
                FinanceTransaction(
                    type = TransactionType.EXPENSE,
                    amount = 1200.0,
                    category = "Food",
                    note = "",
                    paymentMethod = "UPI",
                    date = date,
                    createdAt = date
                )
            )
        )

        val result = useCase(month = 5, year = 2026).first().first()

        assertEquals(-200.0, result.remaining, 0.01)
        assertTrue(result.isExceeded)
    }

    private fun fakeBudgetRepository(budget: Budget) = object : BudgetRepository {
        override fun observeBudgets(): Flow<List<Budget>> = flowOf(listOf(budget))
        override fun observeBudgetsForMonth(month: Int, year: Int): Flow<List<Budget>> = flowOf(listOf(budget))
        override suspend fun saveBudget(budget: Budget): Long = budget.id
        override suspend fun deleteBudget(id: Long) = Unit
    }

    private fun fakeTransactionRepository(transaction: FinanceTransaction) = object : TransactionRepository {
        override fun observeTransactions(query: TransactionQuery): Flow<List<FinanceTransaction>> = flowOf(listOf(transaction))
        override fun observeTransaction(id: Long): Flow<FinanceTransaction?> = flowOf(transaction)
        override suspend fun getTransaction(id: Long): FinanceTransaction = transaction
        override suspend fun saveTransaction(transaction: FinanceTransaction): Long = 1
        override suspend fun deleteTransaction(id: Long) = Unit
    }
}
