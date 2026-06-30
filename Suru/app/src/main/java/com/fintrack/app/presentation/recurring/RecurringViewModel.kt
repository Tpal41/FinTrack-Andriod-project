package com.fintrack.app.presentation.recurring

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fintrack.app.domain.model.Frequency
import com.fintrack.app.domain.model.RecurringTransaction
import com.fintrack.app.domain.model.TransactionType
import com.fintrack.app.domain.usecase.DeleteRecurringTransactionUseCase
import com.fintrack.app.domain.usecase.ObserveRecurringTransactionsUseCase
import com.fintrack.app.domain.usecase.SaveRecurringTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecurringUiState(
    val title: String = "",
    val amount: String = "",
    val category: String = "Subscriptions",
    val frequency: Frequency = Frequency.MONTHLY,
    val type: TransactionType = TransactionType.EXPENSE,
    val showAddDialog: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class RecurringViewModel @Inject constructor(
    private val observeUseCase: ObserveRecurringTransactionsUseCase,
    private val saveUseCase: SaveRecurringTransactionUseCase,
    private val deleteUseCase: DeleteRecurringTransactionUseCase
) : ViewModel() {

    val recurringTransactions: StateFlow<List<RecurringTransaction>> = observeUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _uiState = MutableStateFlow(RecurringUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<String>()
    val events = _events.asSharedFlow()

    fun onTitleChange(title: String) = _uiState.update { it.copy(title = title) }
    fun onAmountChange(amount: String) = _uiState.update { it.copy(amount = amount) }
    fun onCategoryChange(category: String) = _uiState.update { it.copy(category = category) }
    fun onFrequencyChange(frequency: Frequency) = _uiState.update { it.copy(frequency = frequency) }
    fun onTypeChange(type: TransactionType) = _uiState.update { it.copy(type = type) }
    fun onToggleDialog(show: Boolean) = _uiState.update { it.copy(showAddDialog = show, error = null) }

    fun saveRecurring() {
        viewModelScope.launch {
            val currentState = _uiState.value
            val transaction = RecurringTransaction(
                title = currentState.title,
                amount = currentState.amount.toDoubleOrNull() ?: 0.0,
                category = currentState.category,
                frequency = currentState.frequency,
                nextDate = System.currentTimeMillis(), // Initial next date is now
                type = currentState.type,
                isActive = true
            )

            saveUseCase(transaction).onSuccess {
                _uiState.update { it.copy(showAddDialog = false, title = "", amount = "") }
                _events.emit("Recurring transaction saved")
            }.onFailure { e ->
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun deleteRecurring(id: Long) {
        viewModelScope.launch {
            deleteUseCase(id)
            _events.emit("Deleted")
        }
    }
}
