package com.fintrack.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.fintrack.app.data.local.entity.RecurringTransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecurringTransactionDao {
    @Query("SELECT * FROM recurring_transactions ORDER BY nextDate ASC")
    fun observeAll(): Flow<List<RecurringTransactionEntity>>

    @Query("SELECT * FROM recurring_transactions WHERE isActive = 1")
    fun observeActive(): Flow<List<RecurringTransactionEntity>>

    @Query("SELECT * FROM recurring_transactions WHERE id = :id")
    suspend fun getById(id: Long): RecurringTransactionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recurringTransaction: RecurringTransactionEntity): Long

    @Update
    suspend fun update(recurringTransaction: RecurringTransactionEntity)

    @Delete
    suspend fun delete(recurringTransaction: RecurringTransactionEntity)

    @Query("UPDATE recurring_transactions SET nextDate = :nextDate WHERE id = :id")
    suspend fun updateNextDate(id: Long, nextDate: Long)

    @Query("SELECT * FROM recurring_transactions WHERE isActive = 1 AND nextDate <= :currentTime")
    suspend fun getPendingTransactions(currentTime: Long): List<RecurringTransactionEntity>
}
