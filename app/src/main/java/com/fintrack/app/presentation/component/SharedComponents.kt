package com.fintrack.app.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fintrack.app.core.designsystem.theme.FinTrackEmerald
import com.fintrack.app.core.util.asMoney
import com.fintrack.app.core.util.toDisplayDate
import com.fintrack.app.domain.model.FinanceTransaction
import com.fintrack.app.domain.model.TransactionType

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import com.fintrack.app.core.designsystem.component.GlassCard
import com.fintrack.app.core.designsystem.theme.FinTrackGold

@Composable
fun EmptyPanel(message: String, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        modifier = modifier.fillMaxWidth(),
        border = androidx.compose.foundation.BorderStroke(
            1.dp, 
            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
        )
    ) {
        Text(
            message,
            modifier = Modifier.padding(24.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SectionCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp),
            brush = Brush.linearGradient(listOf(FinTrackEmerald, FinTrackGold))
        )
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnumDropdown(
    label: String,
    value: String,
    options: List<String>,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded, 
        onExpandedChange = { expanded = it }, 
        modifier = modifier
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            textStyle = MaterialTheme.typography.bodyMedium
        )
        ExposedDropdownMenu(
            expanded = expanded, 
            onDismissRequest = { expanded = false },
            modifier = Modifier.heightIn(max = 300.dp)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun TransactionRow(
    transaction: FinanceTransaction, 
    currency: String, 
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = { onClick?.invoke() },
        enabled = onClick != null,
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = (if (transaction.type == TransactionType.INCOME) FinTrackEmerald else Color(0xFFFF7A90)).copy(alpha = 0.1f),
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            transaction.category.take(1).uppercase(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (transaction.type == TransactionType.INCOME) FinTrackEmerald else Color(0xFFFF7A90)
                        )
                    }
                }
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(transaction.category, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                    Text(
                        listOf(transaction.note, transaction.paymentMethod, transaction.date.toDisplayDate())
                            .filter { it.isNotBlank() }
                            .joinToString(" • "),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            val signedAmount = if (transaction.type == TransactionType.EXPENSE) -transaction.amount else transaction.amount
            Text(
                signedAmount.asMoney(currency),
                color = if (transaction.type == TransactionType.INCOME) FinTrackEmerald else Color(0xFFFF7A90),
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun TypeFilterBar(
    selectedType: TransactionType?, 
    onTypeSelected: (TransactionType?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            label = "All",
            selected = selectedType == null,
            onClick = { onTypeSelected(null) },
            modifier = Modifier.weight(1f)
        )
        FilterChip(
            label = "Income",
            selected = selectedType == TransactionType.INCOME,
            onClick = { onTypeSelected(TransactionType.INCOME) },
            modifier = Modifier.weight(1f)
        )
        FilterChip(
            label = "Expense",
            selected = selectedType == TransactionType.EXPENSE,
            onClick = { onTypeSelected(TransactionType.EXPENSE) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun FilterChip(
    label: String, 
    selected: Boolean, 
    onClick: () -> Unit, 
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        color = if (selected) FinTrackEmerald.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.05f),
        shape = RoundedCornerShape(12.dp),
        border = if (selected) androidx.compose.foundation.BorderStroke(1.dp, FinTrackEmerald) else null,
        modifier = modifier.height(40.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, 
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                label, 
                style = MaterialTheme.typography.labelLarge, 
                color = if (selected) FinTrackEmerald else MaterialTheme.colorScheme.onSurface,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}
