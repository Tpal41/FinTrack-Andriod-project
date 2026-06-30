package com.fintrack.app.presentation.recurring

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fintrack.app.core.designsystem.component.GlassCard
import com.fintrack.app.core.designsystem.component.PremiumHeader
import com.fintrack.app.domain.model.Frequency
import com.fintrack.app.domain.model.RecurringTransaction
import com.fintrack.app.domain.model.TransactionType
import kotlinx.coroutines.flow.collectLatest
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecurringRoute(
    viewModel: RecurringViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val transactions by viewModel.recurringTransactions.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        topBar = {
            PremiumHeader(
                title = "Recurring",
                subtitle = "Automated transactions"
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onToggleDialog(true) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Recurring")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Scheduled Obligations",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            if (transactions.isEmpty()) {
                item {
                    Box(Modifier.fillParentMaxHeight(0.7f), contentAlignment = Alignment.Center) {
                        Text("No recurring transactions yet", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            items(transactions) { item ->
                RecurringItem(
                    transaction = item,
                    onDelete = { viewModel.deleteRecurring(item.id) }
                )
            }
        }
    }

    if (uiState.showAddDialog) {
        AddRecurringDialog(
            state = uiState,
            onTitleChange = viewModel::onTitleChange,
            onAmountChange = viewModel::onAmountChange,
            onCategoryChange = viewModel::onCategoryChange,
            onFrequencyChange = viewModel::onFrequencyChange,
            onTypeChange = viewModel::onTypeChange,
            onDismiss = { viewModel.onToggleDialog(false) },
            onConfirm = viewModel::saveRecurring
        )
    }
}

@Composable
fun RecurringItem(
    transaction: RecurringTransaction,
    onDelete: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }

    GlassCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${transaction.category} • ${transaction.frequency.name}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Next: ${dateFormat.format(Date(transaction.nextDate))}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = (if (transaction.type == TransactionType.EXPENSE) "-" else "+") + "₹${transaction.amount}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = if (transaction.type == TransactionType.EXPENSE) Color(0xFFEF5350) else Color(0xFF66BB6A)
                )
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecurringDialog(
    state: RecurringUiState,
    onTitleChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onFrequencyChange: (Frequency) -> Unit,
    onTypeChange: (TransactionType) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Recurring Plan") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = state.title,
                    onValueChange = onTitleChange,
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = state.amount,
                    onValueChange = onAmountChange,
                    label = { Text("Amount") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Text("Frequency", style = MaterialTheme.typography.labelMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Frequency.values().forEach { freq ->
                        FilterChip(
                            selected = state.frequency == freq,
                            onClick = { onFrequencyChange(freq) },
                            label = { Text(freq.name.lowercase().capitalize()) }
                        )
                    }
                }

                if (state.error != null) {
                    Text(state.error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
