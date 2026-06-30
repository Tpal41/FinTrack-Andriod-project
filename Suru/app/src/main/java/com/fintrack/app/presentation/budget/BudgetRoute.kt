package com.fintrack.app.presentation.budget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
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
import com.fintrack.app.core.designsystem.theme.FinTrackEmerald
import com.fintrack.app.core.util.asMoney
import com.fintrack.app.domain.model.expenseCategoryLabels
import com.fintrack.app.presentation.component.EmptyPanel
import com.fintrack.app.presentation.component.EnumDropdown
import com.fintrack.app.presentation.component.SectionCard
import com.fintrack.app.core.designsystem.component.PremiumHeader

@Composable
fun BudgetRoute(viewModel: BudgetViewModel = hiltViewModel()) {
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
                    title = "Monthly Budgets",
                    subtitle = "Precision allocation for your wealth"
                )
            }
            
            item {
                SectionCard("Strategic Allocation") {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        EnumDropdown(
                            label = "Category", 
                            value = state.form.category, 
                            options = expenseCategoryLabels(), 
                            onSelected = { viewModel.updateForm { copy(category = it) } }
                        )
                        
                        OutlinedTextField(
                            value = state.form.limit,
                            onValueChange = { value -> 
                                viewModel.updateForm { copy(limit = value.filter { it.isDigit() || it == '.' }) } 
                            },
                            label = { Text("Budget limit") },
                            prefix = { Text(state.settings.currency + " ") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        )
                        
                        Button(
                            onClick = {
                                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                                viewModel.save()
                            },
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = FinTrackEmerald)
                        ) { 
                            Text("Activate Budget", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium) 
                        }
                    }
                }
            }
            
            item {
                SectionCard(title = "Active Guardrails") {
                    if (state.budgets.isEmpty()) {
                        EmptyPanel("Your financial strategy starts with a budget.")
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            state.budgets.forEach { usage ->
                                BudgetUsageItem(
                                    usage = usage,
                                    currency = state.settings.currency,
                                    onDelete = {
                                        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                                        viewModel.delete(usage.budget.id)
                                    }
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
private fun BudgetUsageItem(
    usage: com.fintrack.app.domain.model.BudgetUsage,
    currency: String,
    onDelete: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = (if (usage.isExceeded) Color(0xFFFF7A90) else FinTrackEmerald).copy(alpha = 0.1f),
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            usage.budget.category.take(1).uppercase(),
                            fontWeight = FontWeight.Bold,
                            color = if (usage.isExceeded) Color(0xFFFF7A90) else FinTrackEmerald
                        )
                    }
                }
                Spacer(Modifier.width(12.dp))
                Text(usage.budget.category, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Rounded.Delete,
                    contentDescription = "Delete budget",
                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                )
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(
                    "Spent ${usage.spent.asMoney(currency)}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    "Limit ${usage.budget.budgetLimit.asMoney(currency)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            LinearProgressIndicator(
                progress = { usage.percentageUsed.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp)),
                color = if (usage.isExceeded) Color(0xFFFF7A90) else FinTrackEmerald,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Text(
                if (usage.isExceeded) "Exceeded by ${(-usage.remaining).asMoney(currency)}"
                else "${usage.remaining.asMoney(currency)} remaining",
                color = if (usage.isExceeded) Color(0xFFFF7A90) else FinTrackEmerald,
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
