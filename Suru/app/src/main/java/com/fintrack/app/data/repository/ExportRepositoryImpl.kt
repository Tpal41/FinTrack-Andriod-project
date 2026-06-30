package com.fintrack.app.data.repository

import android.content.Context
import com.fintrack.app.domain.model.TransactionQuery
import com.fintrack.app.domain.repository.ExportRepository
import com.fintrack.app.domain.repository.TransactionRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class ExportRepositoryImpl @Inject constructor(
    private val transactionRepository: TransactionRepository,
    @ApplicationContext private val context: Context
) : ExportRepository {

    override suspend fun generateTransactionsCsv(): String {
        val transactions = transactionRepository.observeTransactions(TransactionQuery()).first()
        
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "FinTrack_Transactions_$timestamp.csv"
        val file = File(context.cacheDir, fileName)
        
        val header = "ID,Type,Amount,Category,Note,Payment Method,Date\n"
        val rows = transactions.joinToString("\n") { t ->
            val dateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(t.date))
            "${t.id},${t.type},${t.amount},\"${t.category}\",\"${t.note}\",\"${t.paymentMethod}\",$dateStr"
        }
        
        file.writeText(header + rows)
        return file.absolutePath
    }
}
