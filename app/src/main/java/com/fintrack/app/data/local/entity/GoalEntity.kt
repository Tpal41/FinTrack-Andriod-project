package com.fintrack.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.fintrack.app.domain.model.SavingsGoal

@Entity(
    tableName = "goals",
    indices = [Index(value = ["targetDate"])]
)
data class GoalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val targetAmount: Double,
    val savedAmount: Double,
    val targetDate: Long
)

fun GoalEntity.toDomain(): SavingsGoal = SavingsGoal(id, title, targetAmount, savedAmount, targetDate)

fun SavingsGoal.toEntity(): GoalEntity = GoalEntity(id, title, targetAmount, savedAmount, targetDate)
