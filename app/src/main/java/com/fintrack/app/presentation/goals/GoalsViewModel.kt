package com.fintrack.app.presentation.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fintrack.app.domain.model.GoalProgress
import com.fintrack.app.domain.model.SavingsGoal
import com.fintrack.app.domain.repository.GoalRepository
import com.fintrack.app.domain.usecase.ObserveGoalsUseCase
import com.fintrack.app.domain.usecase.SaveGoalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

data class GoalFormState(
    val id: Long = 0,
    val title: String = "",
    val targetAmount: String = "",
    val savedAmount: String = "",
    val targetDate: Long = Calendar.getInstance().apply { add(Calendar.MONTH, 6) }.timeInMillis
)

data class GoalsUiState(
    val goals: List<GoalProgress> = emptyList(),
    val form: GoalFormState = GoalFormState(),
    val settings: com.fintrack.app.domain.model.UserSettings = com.fintrack.app.domain.model.UserSettings(),
    val message: String? = null
)

@HiltViewModel
class GoalsViewModel @Inject constructor(
    observeGoals: ObserveGoalsUseCase,
    observeSettings: com.fintrack.app.domain.usecase.ObserveSettingsUseCase,
    private val saveGoal: SaveGoalUseCase,
    private val repository: GoalRepository
) : ViewModel() {
    private val form = MutableStateFlow(GoalFormState())
    private val message = MutableStateFlow<String?>(null)

    val uiState: StateFlow<GoalsUiState> = combine(
        observeGoals(),
        form,
        observeSettings(),
        message
    ) { goals, formValue, settingsValue, messageValue ->
        GoalsUiState(goals, formValue, settingsValue, messageValue)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), GoalsUiState())

    fun updateForm(update: GoalFormState.() -> GoalFormState) {
        form.value = form.value.update()
    }

    fun edit(goal: GoalProgress) {
        form.value = GoalFormState(
            id = goal.goal.id,
            title = goal.goal.title,
            targetAmount = goal.goal.targetAmount.toString(),
            savedAmount = goal.goal.savedAmount.toString(),
            targetDate = goal.goal.targetDate
        )
    }

    fun save() {
        val current = form.value
        val target = current.targetAmount.toDoubleOrNull()
        val saved = current.savedAmount.toDoubleOrNull() ?: 0.0
        if (current.title.isBlank() || target == null || target <= 0) {
            message.value = "Enter a title and target amount."
            return
        }
        viewModelScope.launch {
            saveGoal(SavingsGoal(current.id, current.title.trim(), target, saved, current.targetDate))
                .onSuccess {
                    form.value = GoalFormState()
                    message.value = "Goal saved."
                }
                .onFailure { message.value = it.message }
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch {
            repository.deleteGoal(id)
            message.value = "Goal deleted."
        }
    }

    fun clearMessage() {
        message.value = null
    }
}
