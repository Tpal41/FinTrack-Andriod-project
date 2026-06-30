package com.fintrack.app.presentation.goals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Flag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fintrack.app.core.designsystem.component.GlassCard
import com.fintrack.app.core.designsystem.component.PremiumHeader
import com.fintrack.app.core.designsystem.theme.FinTrackEmerald
import com.fintrack.app.core.designsystem.theme.FinTrackGold
import com.fintrack.app.core.util.asMoney
import com.fintrack.app.core.util.toDisplayDate
import com.fintrack.app.presentation.component.EmptyPanel
import com.fintrack.app.presentation.component.SectionCard

@Composable
fun GoalsRoute(viewModel: GoalsViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current
    val snackbar = remember { SnackbarHostState() }

    androidx.compose.runtime.LaunchedEffect(state.message) {
        state.message?.let {
            snackbar.showSnackbar(it)
            viewModel.clearMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                PremiumHeader(
                    title = "Savings Goals",
                    subtitle = "Fuel your ambitions with smart savings"
                )
            }

            item {
                SectionCard(title = "Define New Target") {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        OutlinedTextField(
                            value = state.form.title,
                            onValueChange = { value -> viewModel.updateForm { copy(title = value) } },
                            label = { Text("What are you saving for?") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedTextField(
                                value = state.form.targetAmount,
                                onValueChange = { value -> viewModel.updateForm { copy(targetAmount = value.filter { it.isDigit() || it == '.' }) } },
                                label = { Text("Target") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            OutlinedTextField(
                                value = state.form.savedAmount,
                                onValueChange = { value -> viewModel.updateForm { copy(savedAmount = value.filter { it.isDigit() || it == '.' }) } },
                                label = { Text("Initial") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                        Text(
                            "Target Date: ${state.form.targetDate.toDisplayDate()}", 
                            style = MaterialTheme.typography.bodySmall, 
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Button(
                            onClick = {
                                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                                viewModel.save()
                            },
                            modifier = Modifier.fillMaxWidth().height(54.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = FinTrackEmerald),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(Icons.Rounded.Flag, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Activate Goal", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            item {
                SectionCard(title = "Active Pursuits") {
                    if (state.goals.isEmpty()) {
                        EmptyPanel("Your future self will thank you. Start a goal for a laptop, vacation, or emergency fund.")
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            state.goals.forEach { progress ->
                                GoalItem(
                                    progress = progress, 
                                    currency = state.settings.currency, 
                                    onDelete = { viewModel.delete(progress.goal.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GoalItem(
    progress: com.fintrack.app.domain.model.GoalProgress, 
    currency: String, 
    onDelete: () -> Unit
) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween, 
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(progress.goal.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(
                        "Target: ${progress.goal.targetAmount.asMoney(currency)}", 
                        color = MaterialTheme.colorScheme.onSurfaceVariant, 
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Rounded.Delete, contentDescription = "Delete", tint = Color(0xFFFF7A90))
                }
            }
            
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "${(progress.progress * 100).toInt()}% achieved", 
                        style = MaterialTheme.typography.labelLarge, 
                        color = FinTrackEmerald
                    )
                    Text(progress.goal.savedAmount.asMoney(currency), fontWeight = FontWeight.Bold)
                }
                LinearProgressIndicator(
                    progress = { progress.progress },
                    modifier = Modifier.fillMaxWidth().height(10.dp).clip(RoundedCornerShape(5.dp)),
                    color = FinTrackEmerald,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }

            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text("Gap to bridge", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelSmall)
                    Text(progress.remainingAmount.asMoney(currency), fontWeight = FontWeight.SemiBold)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Recommended daily", color = FinTrackGold, style = MaterialTheme.typography.labelSmall)
                    Text(progress.estimatedDailySaving.asMoney(currency), fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
