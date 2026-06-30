package com.fintrack.app.data.repository

import com.fintrack.app.data.local.dao.RecurringTransactionDao
import com.fintrack.app.data.local.entity.toDomain
import com.fintrack.app.data.local.entity.toEntity
import com.fintrack.app.domain.model.RecurringTransaction
import com.fintrack.app.domain.repository.RecurringTransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RecurringTransactionRepositoryImpl @Inject constructor(
    private val dao: RecurringTransactionDao
) : RecurringTransactionRepository {
    override fun observeAll(): Flow<List<RecurringTransaction>> =
        dao.observeAll().map { entities -> entities.map { it.toDomain() } }

    override fun observeActive(): Flow<List<RecurringTransaction>> =
        dao.observeActive().map { entities -> entities.map { it.toDomain() } }

    override suspend fun getById(id: Long): RecurringTransaction? =
        dao.getById(id)?.toDomain()

    override suspend fun save(recurringTransaction: RecurringTransaction): Long =
        dao.insert(recurringTransaction.toEntity())

    override suspend fun delete(id: Long) {
        dao.getById(id)?.let { dao.delete(it) }
    }

    override suspend fun updateNextDate(id: Long, nextDate: Long) {
        dao.updateNextDate(id, nextDate)
    }

    override suspend fun getPendingTransactions(currentTime: Long): List<RecurringTransaction> =
        dao.getPendingTransactions(currentTime).map { it.toDomain() }
}
