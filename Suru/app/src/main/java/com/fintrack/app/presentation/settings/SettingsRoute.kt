package com.fintrack.app.presentation.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import com.fintrack.app.core.designsystem.theme.FinTrackEmerald
import com.fintrack.app.core.designsystem.theme.FinTrackGold
import com.fintrack.app.presentation.component.SectionCard
import com.fintrack.app.core.designsystem.component.PremiumHeader
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import java.io.File

@Composable
fun SettingsRoute(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val settings by viewModel.settings.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.exportFile.collect { path ->
            val file = File(path)
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/csv"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(intent, "Export Transactions"))
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp)) {
                PremiumHeader(
                    title = "Settings",
                    subtitle = "Personalize your financial experience"
                )
            }
        }
        
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                SectionCard("Security & Privacy") {
                    SettingSwitch(
                        "Biometric Lock",
                        "Secure your data with fingerprint/face",
                        settings.appLockEnabled,
                        viewModel::updateAppLock
                    )
                }
            }
        }

        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                SectionCard("Experience") {
                    SettingSwitch(
                        "Dark Mode",
                        "Sleek dark theme for late nights",
                        settings.darkMode,
                        viewModel::updateDarkMode
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                    )
                    SettingSwitch(
                        "Daily Reminders",
                        "Smart notifications to stay on track",
                        settings.notificationsEnabled
                    ) { enabled ->
                        viewModel.updateNotifications(enabled)
                        if (enabled) viewModel.scheduleNotifications() else viewModel.cancelNotifications()
                    }
                }
            }
        }

        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                SectionCard("Localization") {
                    Text(
                        "Preferred Currency", 
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Display all monetary values in your local currency",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf("₹", "$", "€", "£").forEach { currency ->
                            val isSelected = settings.currency == currency
                            androidx.compose.material3.Surface(
                                onClick = { viewModel.updateCurrency(currency) },
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) FinTrackEmerald.copy(alpha = 0.2f) else Color.Transparent,
                                border = BorderStroke(
                                    1.dp, 
                                    if (isSelected) FinTrackEmerald else MaterialTheme.colorScheme.outlineVariant
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        currency, 
                                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = if (isSelected) FinTrackEmerald else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                SectionCard("Data Management") {
                    Text(
                        "Export Ledger", 
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Generate a CSV report of all your recorded transactions.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.exportData() },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = FinTrackEmerald)
                    ) {
                        Text("Export to CSV", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingSwitch(
    label: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(label, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            Text(
                description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = checked, 
            onCheckedChange = onCheckedChange,
            colors = androidx.compose.material3.SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = FinTrackEmerald,
                uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        )
    }
}
