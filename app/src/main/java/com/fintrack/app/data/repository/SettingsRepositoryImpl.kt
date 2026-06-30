package com.fintrack.app.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.fintrack.app.domain.model.UserSettings
import com.fintrack.app.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {
    override fun observeSettings(): Flow<UserSettings> =
        dataStore.data.map { preferences ->
            UserSettings(
                darkMode = preferences[DARK_MODE] ?: true,
                currency = preferences[CURRENCY] ?: "₹",
                notificationsEnabled = preferences[NOTIFICATIONS] ?: true,
                appLockEnabled = preferences[APP_LOCK] ?: false
            )
        }

    override suspend fun updateDarkMode(enabled: Boolean) {
        dataStore.edit { it[DARK_MODE] = enabled }
    }

    override suspend fun updateCurrency(currency: String) {
        dataStore.edit { it[CURRENCY] = currency }
    }

    override suspend fun updateNotifications(enabled: Boolean) {
        dataStore.edit { it[NOTIFICATIONS] = enabled }
    }

    override suspend fun updateAppLock(enabled: Boolean) {
        dataStore.edit { it[APP_LOCK] = enabled }
    }

    private companion object {
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val CURRENCY = stringPreferencesKey("currency")
        val NOTIFICATIONS = booleanPreferencesKey("notifications_enabled")
        val APP_LOCK = booleanPreferencesKey("app_lock_enabled")
    }
}
