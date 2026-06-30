package com.fintrack.app.domain

import com.fintrack.app.domain.model.Budget
import com.fintrack.app.domain.model.FinanceTransaction
import com.fintrack.app.domain.model.SavingsGoal
import com.fintrack.app.domain.model.TransactionType
import com.fintrack.app.domain.repository.BudgetRepository
import com.fintrack.app.domain.repository.GoalRepository
import com.fintrack.app.domain.repository.TransactionRepository
import com.fintrack.app.domain.usecase.SaveBudgetUseCase
import com.fintrack.app.domain.usecase.SaveGoalUseCase
import com.fintrack.app.domain.usecase.SaveTransactionUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlinx.coroutines.flow.Flow
import com.fintrack.app.domain.model.TransactionQuery

class EdgeCaseTest {

    @Test
    fun saveTransaction_failsWithNegativeAmount() = runTest {
        val useCase = SaveTransactionUseCase(fakeTransactionRepository())
        val transaction = FinanceTransaction(amount = -10.0, category = "Food", type = TransactionType.EXPENSE, note = "", paymentMethod = "", date = 0, createdAt = 0)
        
        val result = useCase(transaction)
        
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message == "Amount must be greater than zero.")
    }

    @Test
    fun saveBudget_failsWithZeroLimit() = runTest {
        val useCase = SaveBudgetUseCase(fakeBudgetRepository())
        val budget = Budget(category = "Rent", budgetLimit = 0.0, month = 5, year = 2026)
        
        val result = useCase(budget)
        
        assertTrue(result.isFailure)
    }

    @Test
    fun saveGoal_failsWithBlankTitle() = runTest {
        val useCase = SaveGoalUseCase(fakeGoalRepository())
        val goal = SavingsGoal(title = "", targetAmount = 1000.0, savedAmount = 0.0, targetDate = 0)
        
        val result = useCase(goal)
        
        assertTrue(result.isFailure)
    }

    private fun fakeTransactionRepository() = object : TransactionRepository {
        override fun observeTransactions(query: TransactionQuery): Flow<List<FinanceTransaction>> = TODO()
        override fun observeTransaction(id: Long): Flow<FinanceTransaction?> = TODO()
        override suspend fun getTransaction(id: Long): FinanceTransaction = TODO()
        override suspend fun saveTransaction(transaction: FinanceTransaction): Long = 1
        override suspend fun deleteTransaction(id: Long) = Unit
    }

    private fun fakeBudgetRepository() = object : BudgetRepository {
        override fun observeBudgets(): Flow<List<Budget>> = TODO()
        override fun observeBudgetsForMonth(month: Int, year: Int): Flow<List<Budget>> = TODO()
        override suspend fun saveBudget(budget: Budget): Long = 1
        override suspend fun deleteBudget(id: Long) = Unit
    }

    private fun fakeGoalRepository() = object : GoalRepository {
        override fun observeGoals(): Flow<List<SavingsGoal>> = TODO()
        override suspend fun saveGoal(goal: SavingsGoal): Long = 1
        override suspend fun deleteGoal(id: Long) = Unit
    }
}
