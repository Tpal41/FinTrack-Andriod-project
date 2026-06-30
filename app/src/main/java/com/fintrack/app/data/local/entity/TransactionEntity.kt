package com.fintrack.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.fintrack.app.domain.model.FinanceTransaction
import com.fintrack.app.domain.model.TransactionType

@Entity(
    tableName = "transactions",
    indices = [
        Index(value = ["type"]),
        Index(value = ["category"]),
        Index(value = ["date"]),
        Index(value = ["paymentMethod"])
    ]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String,
    val amount: Double,
    val category: String,
    val note: String,
    val paymentMethod: String,
    val date: Long,
    val createdAt: Long
)

fun TransactionEntity.toDomain(): FinanceTransaction = FinanceTransaction(
    id = id,
    type = TransactionType.valueOf(type),
    amount = amount,
    category = category,
    note = note,
    paymentMethod = paymentMethod,
    date = date,
    createdAt = createdAt
)

fun FinanceTransaction.toEntity(): TransactionEntity = TransactionEntity(
    id = id,
    type = type.name,
    amount = amount,
    category = category,
    note = note,
    paymentMethod = paymentMethod,
    date = date,
    createdAt = createdAt
)
