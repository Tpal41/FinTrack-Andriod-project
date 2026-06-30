package com.fintrack.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.fintrack.app.domain.model.Budget

@Entity(
    tableName = "budgets",
    indices = [Index(value = ["category", "month", "year"], unique = true)]
)
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val category: String,
    val budgetLimit: Double,
    val month: Int,
    val year: Int
)

fun BudgetEntity.toDomain(): Budget = Budget(id, category, budgetLimit, month, year)

fun Budget.toEntity(): BudgetEntity = BudgetEntity(id, category, budgetLimit, month, year)
