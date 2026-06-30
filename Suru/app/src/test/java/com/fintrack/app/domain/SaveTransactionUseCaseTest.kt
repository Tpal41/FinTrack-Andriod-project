package com.fintrack.app.domain

import com.fintrack.app.domain.model.FinanceTransaction
import com.fintrack.app.domain.model.TransactionQuery
import com.fintrack.app.domain.model.TransactionType
import com.fintrack.app.domain.repository.TransactionRepository
import com.fintrack.app.domain.usecase.SaveTransactionUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

class SaveTransactionUseCaseTest {
    private val repository = object : TransactionRepository {
        override fun observeTransactions(query: TransactionQuery): Flow<List<FinanceTransaction>> = flowOf(emptyList())
        override fun observeTransaction(id: Long): Flow<FinanceTransaction?> = flowOf(null)
        override suspend fun getTransaction(id: Long): FinanceTransaction? = null
        override suspend fun saveTransaction(transaction: FinanceTransaction): Long = 42L
        override suspend fun deleteTransaction(id: Long) = Unit
    }

    private val useCase = SaveTransactionUseCase(repository)

    @Test
    fun saveTransaction_rejectsZeroAmount() = runTest {
        val result = useCase(validTransaction().copy(amount = 0.0))

        assertTrue(result.isFailure)
    }

    @Test
    fun saveTransaction_acceptsValidExpense() = runTest {
        val result = useCase(validTransaction())

        assertTrue(result.isSuccess)
    }

    private fun validTransaction() = FinanceTransaction(
        type = TransactionType.EXPENSE,
        amount = 250.0,
        category = "Food",
        note = "Lunch",
        paymentMethod = "UPI",
        date = System.currentTimeMillis(),
        createdAt = System.currentTimeMillis()
    )
}
