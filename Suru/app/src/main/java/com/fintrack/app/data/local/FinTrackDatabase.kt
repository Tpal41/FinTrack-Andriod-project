package com.fintrack.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fintrack.app.data.local.dao.AttendanceDao
import com.fintrack.app.data.local.dao.BudgetDao
import com.fintrack.app.data.local.dao.GoalDao
import com.fintrack.app.data.local.dao.RecurringTransactionDao
import com.fintrack.app.data.local.dao.SettingsDao
import com.fintrack.app.data.local.dao.TransactionDao
import com.fintrack.app.data.local.entity.AttendanceEntity
import com.fintrack.app.data.local.entity.BudgetEntity
import com.fintrack.app.data.local.entity.GoalEntity
import com.fintrack.app.data.local.entity.RecurringTransactionEntity
import com.fintrack.app.data.local.entity.SettingsEntity
import com.fintrack.app.data.local.entity.StudentEntity
import com.fintrack.app.data.local.entity.TransactionEntity

@Database(
    entities = [
        TransactionEntity::class,
        BudgetEntity::class,
        GoalEntity::class,
        SettingsEntity::class,
        RecurringTransactionEntity::class,
        StudentEntity::class,
        AttendanceEntity::class
    ],
    version = 3,
    exportSchema = true
)
abstract class FinTrackDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao
    abstract fun goalDao(): GoalDao
    abstract fun settingsDao(): SettingsDao
    abstract fun recurringTransactionDao(): RecurringTransactionDao
    abstract fun attendanceDao(): AttendanceDao
}
