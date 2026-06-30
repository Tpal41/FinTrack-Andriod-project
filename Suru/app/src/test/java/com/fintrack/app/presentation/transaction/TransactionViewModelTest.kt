package com.fintrack.app.presentation.transaction

import com.fintrack.app.domain.model.FinanceTransaction
import com.fintrack.app.domain.model.TransactionQuery
import com.fintrack.app.domain.model.UserSettings
import com.fintrack.app.domain.repository.SettingsRepository
import com.fintrack.app.domain.repository.TransactionRepository
import com.fintrack.app.domain.usecase.DeleteTransactionUseCase
import com.fintrack.app.domain.usecase.ObserveSettingsUseCase
import com.fintrack.app.domain.usecase.ObserveTransactionsUseCase
import com.fintrack.app.domain.usecase.SaveTransactionUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionViewModelTest {

    private lateinit var viewModel: TransactionViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        val fakeTransactionRepository = object : TransactionRepository {
            override fun observeTransactions(query: TransactionQuery): Flow<List<FinanceTransaction>> = flowOf(emptyList())
            override fun observeTransaction(id: Long): Flow<FinanceTransaction?> = flowOf(null)
            override suspend fun getTransaction(id: Long): FinanceTransaction? = null
            override suspend fun saveTransaction(transaction: FinanceTransaction): Long = 1L
            override suspend fun deleteTransaction(id: Long) = Unit
        }

        val fakeSettingsRepository = object : SettingsRepository {
            override fun observeSettings(): Flow<UserSettings> = flowOf(UserSettings())
            override suspend fun updateDarkMode(enabled: Boolean) = Unit
            override suspend fun updateCurrency(currency: String) = Unit
            override suspend fun updateNotifications(enabled: Boolean) = Unit
            override suspend fun updateAppLock(enabled: Boolean) = Unit
        }

        viewModel = TransactionViewModel(
            observeTransactions = ObserveTransactionsUseCase(fakeTransactionRepository),
            observeSettings = ObserveSettingsUseCase(fakeSettingsRepository),
            saveTransaction = SaveTransactionUseCase(fakeTransactionRepository),
            deleteTransaction = DeleteTransactionUseCase(fakeTransactionRepository)
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() = runTest(testDispatcher) {
        assertEquals(false, viewModel.uiState.value.isEditorOpen)
    }
}
