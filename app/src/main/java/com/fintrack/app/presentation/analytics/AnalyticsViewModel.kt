package com.fintrack.app.presentation.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fintrack.app.domain.model.DashboardSummary
import com.fintrack.app.domain.model.UserSettings
import com.fintrack.app.domain.repository.ExportRepository
import com.fintrack.app.domain.usecase.ObserveDashboardUseCase
import com.fintrack.app.domain.usecase.ObserveSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AnalyticsUiState(
    val summary: DashboardSummary = DashboardSummary(
        balance = 0.0,
        totalIncome = 0.0,
        totalExpenses = 0.0,
        monthlySavings = 0.0,
        recentTransactions = emptyList(),
        categoryExpenses = emptyList(),
        monthlyTotals = emptyList()
    ),
    val settings: UserSettings = UserSettings(),
    val exportPath: String? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    observeDashboard: ObserveDashboardUseCase,
    observeSettings: ObserveSettingsUseCase,
    private val exportRepository: ExportRepository
) : ViewModel() {

    private val _exportState = MutableStateFlow<String?>(null)
    
    val uiState: StateFlow<AnalyticsUiState> = combine(
        observeDashboard(),
        observeSettings(),
        _exportState
    ) { dashboard, settings, exportPath ->
        AnalyticsUiState(
            summary = dashboard,
            settings = settings,
            exportPath = exportPath
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AnalyticsUiState())

    fun generateReport() {
        viewModelScope.launch {
            try {
                val path = exportRepository.generateTransactionsCsv()
                _exportState.value = path
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun clearExportPath() {
        _exportState.value = null
    }
}
