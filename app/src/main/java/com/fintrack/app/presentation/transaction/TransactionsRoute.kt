package com.fintrack.app.presentation.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fintrack.app.core.designsystem.component.GlassCard
import com.fintrack.app.core.designsystem.component.PremiumHeader
import com.fintrack.app.core.designsystem.theme.FinTrackEmerald
import com.fintrack.app.core.designsystem.theme.FinTrackGold
import com.fintrack.app.domain.model.DateFilter
import com.fintrack.app.domain.model.TransactionSort
import com.fintrack.app.domain.model.TransactionType
import com.fintrack.app.domain.model.expenseCategoryLabels
import com.fintrack.app.domain.model.incomeCategoryLabels
import com.fintrack.app.domain.model.paymentMethodLabels
import com.fintrack.app.domain.model.paymentMethodLabels
import com.fintrack.app.presentation.component.EmptyPanel
import com.fintrack.app.presentation.component.TransactionRow
import com.fintrack.app.presentation.component.EnumDropdown
import com.fintrack.app.presentation.component.SectionCard
import com.fintrack.app.presentation.component.TypeFilterBar

@Composable
fun TransactionsRoute(
    viewModel: TransactionViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(state.message) {
        state.message?.let {
            snackbar.showSnackbar(it)
            viewModel.clearMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) },
        containerColor = Color.Transparent,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { 
                    haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                    viewModel.openNew() 
                }, 
                containerColor = FinTrackEmerald,
                shape = RoundedCornerShape(20.dp)
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "Add transaction", tint = Color.Black)
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp, top = 20.dp)) {
                    PremiumHeader(
                        title = "Audit Ledger",
                        subtitle = "Analyze every financial movement"
                    )
                }
            }

            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    SectionCard(title = "Audit Controls") {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedTextField(
                                value = state.query.search,
                                onValueChange = viewModel::updateSearch,
                                placeholder = { Text("Search category, note...") },
                                leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    unfocusedIndicatorColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                                )
                            )
                            
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                                EnumDropdown(
                                    label = "Timeline",
                                    value = state.query.dateFilter.name,
                                    options = DateFilter.entries.map { it.name },
                                    onSelected = { viewModel.updateDateFilter(DateFilter.valueOf(it)) },
                                    modifier = Modifier.weight(1f)
                                )
                                EnumDropdown(
                                    label = "Order",
                                    value = state.query.sort.name,
                                    options = TransactionSort.entries.map { it.name },
                                    onSelected = { viewModel.updateSort(TransactionSort.valueOf(it)) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            
                            TypeFilterBar(
                                selectedType = state.query.type,
                                onTypeSelected = viewModel::updateType
                            )
                        }
                    }
                }
            }

            if (state.transactions.isEmpty()) {
                item { 
                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        EmptyPanel("Your ledger is clean. Time to record your first transaction.")
                    }
                }
            } else {
                items(state.transactions, key = { it.id }) { transaction ->
                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp), 
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(Modifier.weight(1f)) {
                                TransactionRow(transaction, state.settings.currency, onClick = { viewModel.edit(transaction) })
                            }
                            IconButton(
                                onClick = { 
                                    haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                                    viewModel.delete(transaction.id) 
                                },
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(Icons.Rounded.Delete, contentDescription = "Delete", tint = Color(0xFFFF7A90))
                            }
                        }
                    }
                }
            }
        }
    }

    if (state.isEditorOpen) {
        TransactionEditorDialog(
            form = state.form,
            onDismiss = viewModel::closeEditor,
            onChange = viewModel::updateForm,
            onSave = {
                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                viewModel.save()
            }
        )
    }
}


@Composable
private fun TransactionEditorDialog(
    form: TransactionFormState,
    onDismiss: () -> Unit,
    onChange: (TransactionFormState.() -> TransactionFormState) -> Unit,
    onSave: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (form.id == 0L) "New Transaction" else "Modify Entry", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.heightIn(max = 560.dp)) {
                TypeFilterBar(
                    selectedType = form.type, 
                    onTypeSelected = { type -> 
                        type?.let { onChange { copy(type = it) } }
                    }
                )
                
                OutlinedTextField(
                    value = form.amount,
                    onValueChange = { value -> onChange { copy(amount = value.filter { it.isDigit() || it == '.' }) } },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                
                val categories = if (form.type == TransactionType.EXPENSE) expenseCategoryLabels() else incomeCategoryLabels()
                EnumDropdown("Category", form.category, categories, { selected -> onChange { copy(category = selected) } })
                EnumDropdown("Payment Method", form.paymentMethod, paymentMethodLabels(), { selected -> onChange { copy(paymentMethod = selected) } })
                
                OutlinedTextField(
                    value = form.note,
                    onValueChange = { value -> onChange { copy(note = value) } },
                    label = { Text("Note (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        confirmButton = { 
            Button(
                onClick = onSave,
                colors = ButtonDefaults.buttonColors(containerColor = FinTrackEmerald),
                shape = RoundedCornerShape(12.dp)
            ) { 
                Text("Confirm", color = Color.Black, fontWeight = FontWeight.Bold) 
            } 
        },
        dismissButton = { 
            TextButton(onClick = onDismiss) { 
                Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant) 
            } 
        },
        shape = RoundedCornerShape(24.dp)
    )
}
