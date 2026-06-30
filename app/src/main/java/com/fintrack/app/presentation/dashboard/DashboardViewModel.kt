package com.fintrack.app.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fintrack.app.domain.model.DashboardSummary
import com.fintrack.app.domain.model.SmartInsight
import com.fintrack.app.domain.model.UserSettings
import com.fintrack.app.domain.usecase.ObserveDashboardUseCase
import com.fintrack.app.domain.usecase.ObserveSettingsUseCase
import com.fintrack.app.domain.usecase.ObserveSmartInsightsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class DashboardUiState(
    val summary: DashboardSummary = DashboardSummary(
        balance = 0.0,
        totalIncome = 0.0,
        totalExpenses = 0.0,
        monthlySavings = 0.0,
        recentTransactions = emptyList(),
        categoryExpenses = emptyList(),
        monthlyTotals = emptyList()
    ),
    val insights: List<SmartInsight> = emptyList(),
    val settings: UserSettings = UserSettings()
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    observeDashboard: ObserveDashboardUseCase,
    observeInsights: ObserveSmartInsightsUseCase,
    observeSettings: ObserveSettingsUseCase
) : ViewModel() {
    val uiState: StateFlow<DashboardUiState> = combine(
        observeDashboard(),
        observeInsights(),
        observeSettings()
    ) { dashboard, insights, settings ->
        DashboardUiState(dashboard, insights, settings)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DashboardUiState())
}
