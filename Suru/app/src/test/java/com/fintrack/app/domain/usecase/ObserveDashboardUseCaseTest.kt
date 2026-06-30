package com.fintrack.app.domain.usecase

import com.fintrack.app.domain.model.FinanceTransaction
import com.fintrack.app.domain.model.TransactionQuery
import com.fintrack.app.domain.model.TransactionType
import com.fintrack.app.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar

class ObserveDashboardUseCaseTest {

    @Test
    fun dashboard_calculatesCorrectTotalsAndBalance() = runTest {
        val transactions = listOf(
            FinanceTransaction(1, TransactionType.INCOME, 5000.0, "Salary", "Bonus", "Bank", System.currentTimeMillis(), System.currentTimeMillis()),
            FinanceTransaction(2, TransactionType.EXPENSE, 1000.0, "Food", "Lunch", "Cash", System.currentTimeMillis(), System.currentTimeMillis()),
            FinanceTransaction(3, TransactionType.EXPENSE, 500.0, "Travel", "Uber", "UPI", System.currentTimeMillis(), System.currentTimeMillis())
        )
        
        val useCase = ObserveDashboardUseCase(fakeTransactionRepository(transactions))
        val summary = useCase().first()

        assertEquals(3500.0, summary.balance, 0.01)
        assertEquals(5000.0, summary.totalIncome, 0.01)
        assertEquals(1500.0, summary.totalExpenses, 0.01)
        assertEquals(3, summary.recentTransactions.size)
    }

    @Test
    fun dashboard_groupsCategoryExpensesCorrectly() = runTest {
        val now = System.currentTimeMillis()
        val transactions = listOf(
            FinanceTransaction(1, TransactionType.EXPENSE, 200.0, "Food", "", "", now, now),
            FinanceTransaction(2, TransactionType.EXPENSE, 300.0, "Food", "", "", now, now),
            FinanceTransaction(3, TransactionType.EXPENSE, 500.0, "Rent", "", "", now, now)
        )

        val useCase = ObserveDashboardUseCase(fakeTransactionRepository(transactions))
        val summary = useCase().first()

        val foodTotal = summary.categoryExpenses.find { it.category == "Food" }?.total
        val rentTotal = summary.categoryExpenses.find { it.category == "Rent" }?.total

        assertEquals(500.0, foodTotal ?: 0.0, 0.01)
        assertEquals(500.0, rentTotal ?: 0.0, 0.01)
        assertEquals(2, summary.categoryExpenses.size)
    }

    private fun fakeTransactionRepository(transactions: List<FinanceTransaction>) = object : TransactionRepository {
        override fun observeTransactions(query: TransactionQuery): Flow<List<FinanceTransaction>> = flowOf(transactions)
        override fun observeTransaction(id: Long): Flow<FinanceTransaction?> = flowOf(transactions.find { it.id == id })
        override suspend fun getTransaction(id: Long): FinanceTransaction = transactions.first { it.id == id }
        override suspend fun saveTransaction(transaction: FinanceTransaction): Long = 1
        override suspend fun deleteTransaction(id: Long) = Unit
    }
}
