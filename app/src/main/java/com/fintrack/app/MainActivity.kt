package com.fintrack.app

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.fintrack.app.core.navigation.FinTrackNavHost
import com.fintrack.app.core.designsystem.theme.FinTrackTheme
import com.fintrack.app.presentation.settings.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

import androidx.compose.runtime.*
import com.fintrack.app.core.util.BiometricHelper

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val settings by settingsViewModel.settings.collectAsState()
            
            var isUnlocked by remember { mutableStateOf(false) }
            val context = this

            LaunchedEffect(settings.appLockEnabled) {
                if (settings.appLockEnabled && !isUnlocked) {
                    BiometricHelper.showBiometricPrompt(
                        activity = context,
                        onSuccess = { isUnlocked = true },
                        onError = { 
                            // In a real app, you might want to show a custom error screen
                            // or allow retry. For now, we'll just keep it locked.
                        }
                    )
                } else {
                    isUnlocked = true
                }
            }

            FinTrackTheme(darkTheme = settings.darkMode) {
                if (isUnlocked || !settings.appLockEnabled) {
                    FinTrackNavHost()
                } else {
                    // Blank screen while waiting for biometric
                    androidx.compose.foundation.layout.Box(
                        modifier = androidx.compose.ui.Modifier
                            .fillMaxSize()
                            .androidx.compose.foundation.background(androidx.compose.material3.MaterialTheme.colorScheme.background)
                    )
                }
            }
        }
    }
}
