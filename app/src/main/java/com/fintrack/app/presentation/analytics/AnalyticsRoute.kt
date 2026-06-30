package com.fintrack.app.presentation.analytics

import android.content.Intent
import android.graphics.Color as AndroidColor
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FileDownload
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.fintrack.app.core.designsystem.component.PremiumHeader
import com.fintrack.app.core.designsystem.theme.FinTrackEmerald
import com.fintrack.app.core.designsystem.theme.FinTrackGold
import com.fintrack.app.core.designsystem.theme.FinTrackSurface
import com.fintrack.app.core.util.asMoney
import com.fintrack.app.domain.model.CategoryTotal
import com.fintrack.app.domain.model.MonthlyTotal
import com.fintrack.app.presentation.component.EmptyPanel
import com.fintrack.app.presentation.component.SectionCard
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.io.File

@Composable
fun AnalyticsRoute(
    viewModel: AnalyticsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val isDark = MaterialTheme.colorScheme.surface == FinTrackSurface

    LaunchedEffect(state.exportPath) {
        state.exportPath?.let { path ->
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
            context.startActivity(Intent.createChooser(intent, "Share Financial Report"))
            viewModel.clearExportPath()
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Row(
                modifier = Modifier.padding(horizontal = 20.dp, top = 20.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    PremiumHeader(
                        title = "Financial Insights",
                        subtitle = "Data-driven analysis of your wealth"
                    )
                }
                IconButton(
                    onClick = viewModel::generateReport,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Icon(
                        Icons.Rounded.FileDownload,
                        contentDescription = "Export Report",
                        tint = FinTrackEmerald,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }

        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                SectionCard(title = "Expense Distribution") {
                    if (state.summary.categoryExpenses.isEmpty()) {
                        EmptyPanel("Add expenses to generate the pie chart.")
                    } else {
                        PieChartView(state.summary.categoryExpenses, isDark)
                        CategoryBreakdownList(state.summary.categoryExpenses, state.settings.currency)
                    }
                }
            }
        }

        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                SectionCard(title = "Monthly Comparison") {
                    if (state.summary.monthlyTotals.isEmpty()) {
                        EmptyPanel("Monthly data appears after transactions are added.")
                    } else {
                        BarChartView(state.summary.monthlyTotals, isDark)
                    }
                }
            }
        }

        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                SectionCard(title = "Growth Trajectory") {
                    if (state.summary.monthlyTotals.isEmpty()) {
                        EmptyPanel("Trend lines appear after transactions are added.")
                    } else {
                        LineChartView(state.summary.monthlyTotals, isDark)
                    }
                }
            }
        }
    }
}


@Composable
private fun CategoryBreakdownList(categories: List<CategoryTotal>, currency: String) {
    Column(
        modifier = Modifier.padding(top = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        categories.sortedByDescending { it.total }.forEach { category ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(category.category, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                Text(
                    category.total.asMoney(currency),
                    style = MaterialTheme.typography.bodyLarge,
                    color = FinTrackEmerald,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun PieChartView(entries: List<CategoryTotal>, isDark: Boolean) {
    val labelColor = if (isDark) AndroidColor.WHITE else AndroidColor.BLACK
    AndroidView(
        modifier = Modifier.fillMaxWidth().height(280.dp),
        factory = { context -> PieChart(context) },
        update = { chart ->
            val dataSet = PieDataSet(entries.map { PieEntry(it.total.toFloat(), it.category) }, "")
            dataSet.colors = listOf(
                FinTrackEmerald.toArgb(),
                FinTrackGold.toArgb(),
                AndroidColor.rgb(255, 122, 144),
                AndroidColor.rgb(112, 161, 255),
                AndroidColor.rgb(162, 155, 254),
                AndroidColor.rgb(250, 177, 160)
            )
            dataSet.valueTextColor = labelColor
            dataSet.valueTextSize = 12f
            dataSet.sliceSpace = 4f
            
            chart.data = PieData(dataSet)
            chart.description.isEnabled = false
            chart.legend.textColor = labelColor
            chart.legend.isEnabled = false
            chart.setEntryLabelColor(labelColor)
            chart.setEntryLabelTextSize(10f)
            chart.setHoleColor(AndroidColor.TRANSPARENT)
            chart.animateXY(1200, 1200)
            chart.invalidate()
        }
    )
}

@Composable
private fun BarChartView(entries: List<MonthlyTotal>, isDark: Boolean) {
    val labelColor = if (isDark) AndroidColor.WHITE else AndroidColor.BLACK
    AndroidView(
        modifier = Modifier.fillMaxWidth().height(280.dp),
        factory = { context -> BarChart(context) },
        update = { chart ->
            val dataSet = BarDataSet(entries.mapIndexed { index, total -> BarEntry(index.toFloat(), total.expense.toFloat()) }, "Expenses")
            dataSet.color = FinTrackEmerald.toArgb()
            dataSet.valueTextColor = labelColor
            dataSet.setDrawValues(false)
            
            chart.data = BarData(dataSet)
            chart.description.isEnabled = false
            chart.legend.textColor = labelColor
            chart.xAxis.textColor = labelColor
            chart.xAxis.position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
            chart.xAxis.setDrawGridLines(false)
            chart.axisLeft.textColor = labelColor
            chart.axisLeft.setDrawGridLines(false)
            chart.axisRight.isEnabled = false
            chart.animateY(1200)
            chart.invalidate()
        }
    )
}

@Composable
private fun LineChartView(entries: List<MonthlyTotal>, isDark: Boolean) {
    val labelColor = if (isDark) AndroidColor.WHITE else AndroidColor.BLACK
    AndroidView(
        modifier = Modifier.fillMaxWidth().height(280.dp),
        factory = { context -> LineChart(context) },
        update = { chart ->
            val income = LineDataSet(entries.mapIndexed { index, total -> Entry(index.toFloat(), total.income.toFloat()) }, "Income")
            val expense = LineDataSet(entries.mapIndexed { index, total -> Entry(index.toFloat(), total.expense.toFloat()) }, "Expense")
            
            income.apply {
                color = FinTrackEmerald.toArgb()
                setCircleColor(FinTrackEmerald.toArgb())
                lineWidth = 3f
                circleRadius = 5f
                setDrawValues(false)
                setDrawFilled(true)
                fillColor = FinTrackEmerald.toArgb()
                fillAlpha = 40
                mode = LineDataSet.Mode.CUBIC_BEZIER
            }
            
            expense.apply {
                color = AndroidColor.rgb(255, 122, 144)
                setCircleColor(AndroidColor.rgb(255, 122, 144))
                lineWidth = 3f
                circleRadius = 5f
                setDrawValues(false)
                setDrawFilled(true)
                fillColor = AndroidColor.rgb(255, 122, 144)
                fillAlpha = 40
                mode = LineDataSet.Mode.CUBIC_BEZIER
            }
            
            chart.data = LineData(income, expense)
            chart.description.isEnabled = false
            chart.legend.textColor = labelColor
            chart.xAxis.textColor = labelColor
            chart.xAxis.position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
            chart.xAxis.setDrawGridLines(false)
            chart.axisLeft.textColor = labelColor
            chart.axisLeft.setDrawGridLines(false)
            chart.axisRight.isEnabled = false
            chart.animateX(1200)
            chart.invalidate()
        }
    )
}

