package com.fintrack.app.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import com.fintrack.app.core.util.asMoney
import com.fintrack.app.domain.model.InsightSeverity
import com.fintrack.app.core.designsystem.component.FinTrackMetricCard
import com.fintrack.app.core.designsystem.theme.FinTrackEmerald
import com.fintrack.app.core.designsystem.theme.FinTrackGold
import com.fintrack.app.core.designsystem.theme.FinTrackSurface
import com.fintrack.app.domain.model.TransactionType
import com.fintrack.app.presentation.component.EmptyPanel
import com.fintrack.app.presentation.component.TransactionRow
import com.fintrack.app.presentation.component.SectionCard

import com.fintrack.app.core.designsystem.component.GlassCard
import com.fintrack.app.core.designsystem.component.PremiumHeader

@Composable
fun DashboardRoute(
    onAddTransaction: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    DashboardScreen(
        state = state,
        onAddTransaction = onAddTransaction
    )
}

@Composable
private fun DashboardScreen(
    state: DashboardUiState,
    onAddTransaction: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            PremiumHeader(
                title = "FinTrack",
                subtitle = "Precision Finance Management"
            )
        }
        item {
            BalanceHeroCard(
                balance = state.summary.balance.asMoney(state.settings.currency),
                savings = state.summary.monthlySavings.asMoney(state.settings.currency),
                health = state.summary.healthIndex
            )
        }
        item {
            Button(
                onClick = onAddTransaction,
                colors = ButtonDefaults.buttonColors(
                    containerColor = FinTrackEmerald,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .shadow(12.dp, RoundedCornerShape(20.dp)),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                androidx.compose.material3.Icon(
                    Icons.Rounded.Add, 
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    "Add transaction", 
                    modifier = Modifier.padding(start = 12.dp), 
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        item {
            SectionCard(title = "Smart Insights") {
                state.insights.forEachIndexed { index, insight ->
                    val color = when (insight.severity) {
                        InsightSeverity.POSITIVE -> FinTrackEmerald
                        InsightSeverity.WARNING -> Color(0xFFFF7A90)
                        InsightSeverity.NEUTRAL -> FinTrackGold
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = color.copy(alpha = 0.1f),
                            modifier = Modifier.size(48.dp)
                        ) {
                            Box(contentAlignment = androidx.compose.ui.Alignment.Center) {
                                androidx.compose.material3.Icon(
                                    Icons.Rounded.TrendingUp, 
                                    contentDescription = null, 
                                    tint = color,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Column {
                            Text(
                                insight.title, 
                                fontWeight = FontWeight.Bold, 
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                insight.description, 
                                color = MaterialTheme.colorScheme.onSurfaceVariant, 
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    if (index < state.insights.lastIndex) {
                        androidx.compose.material3.HorizontalDivider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
        item {
            SectionCard(title = "Category Distribution") {
                if (state.summary.categoryExpenses.isEmpty()) {
                    EmptyPanel("No expenses tracked this month.")
                } else {
                    state.summary.categoryExpenses.take(5).forEach { category ->
                        val max = state.summary.categoryExpenses.first().total.coerceAtLeast(1.0)
                        Column(modifier = Modifier.padding(vertical = 6.dp)) {
                            Row(
                                Modifier.fillMaxWidth(), 
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                            ) {
                                Text(category.category, fontWeight = FontWeight.SemiBold)
                                Text(
                                    category.total.asMoney(state.settings.currency),
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Spacer(Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = { (category.total / max).toFloat().coerceIn(0f, 1f) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(5.dp)),
                                color = FinTrackEmerald,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        }
                    }
                }
            }
        }
        item {
            SectionCard(title = "Recent Transactions") {
                if (state.summary.recentTransactions.isEmpty()) {
                    EmptyPanel("Your financial journey starts here.")
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        state.summary.recentTransactions.forEach { transaction ->
                            TransactionRow(transaction, state.settings.currency)
                        }
                    }
                }
            }
        }
        item { Spacer(Modifier.height(100.dp)) }
    }
}

@Composable
private fun BalanceHeroCard(balance: String, savings: String, health: String) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column {
            Text(
                "Total Balance", 
                color = MaterialTheme.colorScheme.onSurfaceVariant, 
                style = MaterialTheme.typography.labelLarge,
                letterSpacing = androidx.compose.ui.unit.TextUnit.Unspecified
            )
            Text(
                balance, 
                style = MaterialTheme.typography.displayMedium, 
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            androidx.compose.material3.HorizontalDivider(
                modifier = Modifier.padding(vertical = 20.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        Box(Modifier.size(8.dp).background(FinTrackEmerald, RoundedCornerShape(2.dp)))
                        Spacer(Modifier.width(8.dp))
                        Text("Monthly Savings", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text(savings, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = FinTrackEmerald)
                }
                Column(modifier = Modifier.weight(1f), horizontalAlignment = androidx.compose.ui.Alignment.End) {
                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        Text("Health Index", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.width(8.dp))
                        Box(Modifier.size(8.dp).background(FinTrackGold, RoundedCornerShape(2.dp)))
                    }
                    Text(health, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = FinTrackGold)
                }
            }
        }
    }
}

