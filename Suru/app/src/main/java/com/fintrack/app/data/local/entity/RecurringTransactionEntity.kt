package com.fintrack.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fintrack.app.domain.model.Frequency
import com.fintrack.app.domain.model.RecurringTransaction
import com.fintrack.app.domain.model.TransactionType

@Entity(tableName = "recurring_transactions")
data class RecurringTransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val amount: Double,
    val category: String,
    val frequency: String,
    val nextDate: Long,
    val type: String,
    val isActive: Boolean
)

fun RecurringTransactionEntity.toDomain(): RecurringTransaction = RecurringTransaction(
    id = id,
    title = title,
    amount = amount,
    category = category,
    frequency = Frequency.valueOf(frequency),
    nextDate = nextDate,
    type = TransactionType.valueOf(type),
    isActive = isActive
)

fun RecurringTransaction.toEntity(): RecurringTransactionEntity = RecurringTransactionEntity(
    id = id,
    title = title,
    amount = amount,
    category = category,
    frequency = frequency.name,
    nextDate = nextDate,
    type = type.name,
    isActive = isActive
)
