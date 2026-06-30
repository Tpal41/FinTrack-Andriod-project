package com.fintrack.app.presentation.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fintrack.app.domain.model.DateFilter
import com.fintrack.app.domain.model.FinanceTransaction
import com.fintrack.app.domain.model.TransactionQuery
import com.fintrack.app.domain.model.TransactionSort
import com.fintrack.app.domain.model.TransactionType
import com.fintrack.app.domain.model.expenseCategoryLabels
import com.fintrack.app.domain.model.incomeCategoryLabels
import com.fintrack.app.domain.model.paymentMethodLabels
import com.fintrack.app.domain.usecase.DeleteTransactionUseCase
import com.fintrack.app.domain.usecase.ObserveTransactionsUseCase
import com.fintrack.app.domain.usecase.SaveTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TransactionFormState(
    val id: Long = 0,
    val type: TransactionType = TransactionType.EXPENSE,
    val amount: String = "",
    val category: String = expenseCategoryLabels().first(),
    val note: String = "",
    val paymentMethod: String = paymentMethodLabels().first(),
    val date: Long = System.currentTimeMillis()
)

data class TransactionUiState(
    val transactions: List<FinanceTransaction> = emptyList(),
    val query: TransactionQuery = TransactionQuery(),
    val settings: com.fintrack.app.domain.model.UserSettings = com.fintrack.app.domain.model.UserSettings(),
    val form: TransactionFormState = TransactionFormState(),
    val isEditorOpen: Boolean = false,
    val message: String? = null
)

@HiltViewModel
class TransactionViewModel @Inject constructor(
    observeTransactions: ObserveTransactionsUseCase,
    observeSettings: com.fintrack.app.domain.usecase.ObserveSettingsUseCase,
    private val saveTransaction: SaveTransactionUseCase,
    private val deleteTransaction: DeleteTransactionUseCase
) : ViewModel() {
    private val query = MutableStateFlow(TransactionQuery())
    private val form = MutableStateFlow(TransactionFormState())
    private val editorOpen = MutableStateFlow(false)
    private val message = MutableStateFlow<String?>(null)

    val uiState: StateFlow<TransactionUiState> = combine(
        query,
        form,
        editorOpen,
        observeSettings(),
        message
    ) { queryValue, formValue, isOpen, settingsValue, messageValue ->
        LocalState(queryValue, formValue, isOpen, settingsValue, messageValue)
    }.combine(query.flatMapLatestCompat { observeTransactions(it) }) { local, transactions ->
        TransactionUiState(
            transactions = transactions,
            query = local.query,
            settings = local.settings,
            form = local.form,
            isEditorOpen = local.editorOpen,
            message = local.message
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TransactionUiState())

    fun openNew(type: TransactionType = TransactionType.EXPENSE) {
        form.value = TransactionFormState(
            type = type,
            category = if (type == TransactionType.EXPENSE) expenseCategoryLabels().first() else incomeCategoryLabels().first()
        )
        editorOpen.value = true
    }

    fun edit(transaction: FinanceTransaction) {
        form.value = TransactionFormState(
            id = transaction.id,
            type = transaction.type,
            amount = transaction.amount.toString(),
            category = transaction.category,
            note = transaction.note,
            paymentMethod = transaction.paymentMethod,
            date = transaction.date
        )
        editorOpen.value = true
    }

    fun closeEditor() {
        editorOpen.value = false
    }

    fun updateForm(update: TransactionFormState.() -> TransactionFormState) {
        val next = form.value.update()
        form.value = if (next.type != form.value.type) {
            next.copy(category = if (next.type == TransactionType.EXPENSE) expenseCategoryLabels().first() else incomeCategoryLabels().first())
        } else {
            next
        }
    }

    fun updateSearch(value: String) {
        query.value = query.value.copy(search = value)
    }

    fun updateDateFilter(value: DateFilter) {
        query.value = query.value.copy(dateFilter = value)
    }

    fun updateSort(value: TransactionSort) {
        query.value = query.value.copy(sort = value)
    }

    fun updateType(value: TransactionType?) {
        query.value = query.value.copy(type = value)
    }

    fun save() {
        val current = form.value
        val amount = current.amount.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            message.value = "Enter a valid amount."
            return
        }
        viewModelScope.launch {
            val result = saveTransaction(
                FinanceTransaction(
                    id = current.id,
                    type = current.type,
                    amount = amount,
                    category = current.category,
                    note = current.note.trim(),
                    paymentMethod = current.paymentMethod,
                    date = current.date,
                    createdAt = if (current.id == 0L) System.currentTimeMillis() else System.currentTimeMillis()
                )
            )
            result.onSuccess {
                editorOpen.value = false
                message.value = "Transaction saved."
            }.onFailure {
                message.value = it.message ?: "Unable to save transaction."
            }
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch {
            deleteTransaction(id)
            message.value = "Transaction deleted."
        }
    }

    fun clearMessage() {
        message.value = null
    }

    private data class LocalState(
        val query: TransactionQuery,
        val form: TransactionFormState,
        val editorOpen: Boolean,
        val settings: com.fintrack.app.domain.model.UserSettings,
        val message: String?
    )
}

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
private fun <T, R> kotlinx.coroutines.flow.Flow<T>.flatMapLatestCompat(
    transform: suspend (T) -> kotlinx.coroutines.flow.Flow<R>
): kotlinx.coroutines.flow.Flow<R> = flatMapLatest(transform)
