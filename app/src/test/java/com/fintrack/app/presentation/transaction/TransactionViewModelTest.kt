package com.fintrack.app.presentation.transaction

import com.fintrack.app.domain.model.FinanceTransaction
import com.fintrack.app.domain.model.TransactionQuery
import com.fintrack.app.domain.model.TransactionType
import com.fintrack.app.domain.usecase.DeleteTransactionUseCase
import com.fintrack.app.domain.usecase.ObserveTransactionsUseCase
import com.fintrack.app.domain.usecase.SaveTransactionUseCase
import com.fintrack.app.domain.repository.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: TransactionViewModel
    private lateinit var repository: FakeTransactionRepository

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeTransactionRepository()
        viewModel = TransactionViewModel(
            ObserveTransactionsUseCase(repository),
            SaveTransactionUseCase(repository),
            DeleteTransactionUseCase(repository)
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialState_isCorrect() = runTest {
        val state = viewModel.uiState.first()
        assertFalse(state.isEditorOpen)
        assertTrue(state.transactions.isEmpty())
    }

    @Test
    fun openNew_updatesFormAndOpensEditor() = runTest {
        viewModel.openNew(TransactionType.INCOME)
        val state = viewModel.uiState.first()
        assertTrue(state.isEditorOpen)
        assertEquals(TransactionType.INCOME, state.form.type)
    }

    @Test
    fun save_withValidAmount_savesAndClosesEditor() = runTest {
        viewModel.openNew()
        viewModel.updateForm { copy(amount = "100.50", category = "Food") }
        
        viewModel.save()
        advanceUntilIdle()

        val state = viewModel.uiState.first()
        assertEquals("Transaction saved.", state.message)
        assertFalse(state.isEditorOpen)
        assertEquals(1, repository.transactions.size)
    }

    @Test
    fun save_withInvalidAmount_showsError() = runTest {
        viewModel.openNew()
        viewModel.updateForm { copy(amount = "-10", category = "Food") }
        
        viewModel.save()
        advanceUntilIdle()

        val state = viewModel.uiState.first()
        assertEquals("Enter a valid amount.", state.message)
        assertTrue(state.isEditorOpen)
    }

    private class FakeTransactionRepository : TransactionRepository {
        val transactions = mutableListOf<FinanceTransaction>()
        
        override fun observeTransactions(query: TransactionQuery): Flow<List<FinanceTransaction>> = flowOf(transactions)
        override fun observeTransaction(id: Long): Flow<FinanceTransaction?> = flowOf(transactions.find { it.id == id })
        override suspend fun getTransaction(id: Long): FinanceTransaction = transactions.first { it.id == id }
        override suspend fun saveTransaction(transaction: FinanceTransaction): Long {
            transactions.add(transaction)
            return 1
        }
        override suspend fun deleteTransaction(id: Long) {
            transactions.removeIf { it.id == id }
        }
    }
}
