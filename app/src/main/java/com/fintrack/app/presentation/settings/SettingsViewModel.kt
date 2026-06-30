package com.fintrack.app.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fintrack.app.domain.model.UserSettings
import com.fintrack.app.domain.repository.ExportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.fintrack.app.notification.NotificationScheduler

@HiltViewModel
class SettingsViewModel @Inject constructor(
    observeSettings: ObserveSettingsUseCase,
    private val repository: SettingsRepository,
    private val exportRepository: ExportRepository,
    private val scheduler: NotificationScheduler
) : ViewModel() {
    val settings: StateFlow<UserSettings> =
        observeSettings().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UserSettings())

    private val _exportFile = MutableSharedFlow<String>()
    val exportFile: SharedFlow<String> = _exportFile.asSharedFlow()

    fun updateDarkMode(enabled: Boolean) {
        viewModelScope.launch { repository.updateDarkMode(enabled) }
    }

    fun updateCurrency(currency: String) {
        viewModelScope.launch { repository.updateCurrency(currency) }
    }

    fun updateNotifications(enabled: Boolean) {
        viewModelScope.launch { repository.updateNotifications(enabled) }
    }

    fun updateAppLock(enabled: Boolean) {
        viewModelScope.launch { repository.updateAppLock(enabled) }
    }

    fun exportData() {
        viewModelScope.launch {
            val path = exportRepository.generateTransactionsCsv()
            _exportFile.emit(path)
        }
    }

    fun scheduleNotifications() {
        viewModelScope.launch { scheduler.scheduleDailyReminder() }
    }

    fun cancelNotifications() {
        viewModelScope.launch { scheduler.cancelReminders() }
    }
}
