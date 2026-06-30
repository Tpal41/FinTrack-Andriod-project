package com.fintrack.app.presentation.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fintrack.app.core.util.currentMonth
import com.fintrack.app.core.util.currentYear
import com.fintrack.app.domain.model.Budget
import com.fintrack.app.domain.model.BudgetUsage
import com.fintrack.app.domain.model.expenseCategoryLabels
import com.fintrack.app.domain.repository.BudgetRepository
import com.fintrack.app.domain.usecase.ObserveBudgetUsageUseCase
import com.fintrack.app.domain.usecase.SaveBudgetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.fintrack.app.domain.model.UserSettings
import com.fintrack.app.domain.usecase.ObserveSettingsUseCase
import javax.inject.Inject

data class BudgetFormState(
    val id: Long = 0,
    val category: String = expenseCategoryLabels().first(),
    val limit: String = "",
    val month: Int = currentMonth(),
    val year: Int = currentYear()
)

data class BudgetUiState(
    val budgets: List<BudgetUsage> = emptyList(),
    val settings: UserSettings = UserSettings(),
    val form: BudgetFormState = BudgetFormState(),
    val message: String? = null
)

@HiltViewModel
class BudgetViewModel @Inject constructor(
    observeBudgetUsage: ObserveBudgetUsageUseCase,
    observeSettings: ObserveSettingsUseCase,
    private val saveBudget: SaveBudgetUseCase,
    private val repository: BudgetRepository
) : ViewModel() {
    private val form = MutableStateFlow(BudgetFormState())
    private val message = MutableStateFlow<String?>(null)

    val uiState: StateFlow<BudgetUiState> = combine(
        observeBudgetUsage(),
        observeSettings(),
        form,
        message
    ) { budgets, settings, formValue, messageValue ->
        BudgetUiState(budgets, settings, formValue, messageValue)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), BudgetUiState())

    fun updateForm(update: BudgetFormState.() -> BudgetFormState) {
        form.value = form.value.update()
    }

    fun edit(usage: BudgetUsage) {
        form.value = BudgetFormState(
            id = usage.budget.id,
            category = usage.budget.category,
            limit = usage.budget.budgetLimit.toString(),
            month = usage.budget.month,
            year = usage.budget.year
        )
    }

    fun save() {
        val current = form.value
        val limit = current.limit.toDoubleOrNull()
        if (limit == null || limit <= 0) {
            message.value = "Enter a valid budget limit."
            return
        }
        viewModelScope.launch {
            saveBudget(Budget(current.id, current.category, limit, current.month, current.year))
                .onSuccess {
                    form.value = BudgetFormState()
                    message.value = "Budget saved."
                }
                .onFailure { message.value = it.message }
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch {
            repository.deleteBudget(id)
            message.value = "Budget deleted."
        }
    }

    fun clearMessage() {
        message.value = null
    }
}
