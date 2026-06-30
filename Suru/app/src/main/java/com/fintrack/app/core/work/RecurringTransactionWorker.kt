package com.fintrack.app.core.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.fintrack.app.domain.model.FinanceTransaction
import com.fintrack.app.domain.model.Frequency
import com.fintrack.app.domain.repository.RecurringTransactionRepository
import com.fintrack.app.domain.repository.TransactionRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.Calendar

@HiltWorker
class RecurringTransactionWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val recurringRepository: RecurringTransactionRepository,
    private val transactionRepository: TransactionRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val currentTime = System.currentTimeMillis()
        val pendingTransactions = recurringRepository.getPendingTransactions(currentTime)

        for (recurring in pendingTransactions) {
            // Create the actual transaction
            val transaction = FinanceTransaction(
                type = recurring.type,
                amount = recurring.amount,
                category = recurring.category,
                note = recurring.title,
                paymentMethod = "Recurring",
                date = recurring.nextDate,
                createdAt = currentTime
            )
            transactionRepository.saveTransaction(transaction)

            // Calculate next date
            val nextDate = calculateNextDate(recurring.nextDate, recurring.frequency)
            recurringRepository.updateNextDate(recurring.id, nextDate)
        }

        return Result.success()
    }

    private fun calculateNextDate(currentNextDate: Long, frequency: Frequency): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = currentNextDate
        }
        when (frequency) {
            Frequency.DAILY -> calendar.add(Calendar.DAY_OF_YEAR, 1)
            Frequency.WEEKLY -> calendar.add(Calendar.WEEK_OF_YEAR, 1)
            Frequency.MONTHLY -> calendar.add(Calendar.MONTH, 1)
            Frequency.YEARLY -> calendar.add(Calendar.YEAR, 1)
        }
        return calendar.timeInMillis
    }
}
