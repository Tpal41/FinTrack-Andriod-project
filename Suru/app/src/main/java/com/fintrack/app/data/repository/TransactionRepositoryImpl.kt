package com.fintrack.app.data.repository

import com.fintrack.app.core.util.endOfToday
import com.fintrack.app.core.util.startOfMonth
import com.fintrack.app.core.util.startOfToday
import com.fintrack.app.core.util.startOfWeek
import com.fintrack.app.data.local.dao.TransactionDao
import com.fintrack.app.data.local.entity.toDomain
import com.fintrack.app.data.local.entity.toEntity
import com.fintrack.app.domain.model.DateFilter
import com.fintrack.app.domain.model.FinanceTransaction
import com.fintrack.app.domain.model.TransactionQuery
import com.fintrack.app.domain.model.TransactionSort
import com.fintrack.app.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val dao: TransactionDao
) : TransactionRepository {
    override fun observeTransactions(query: TransactionQuery): Flow<List<FinanceTransaction>> {
        return dao.observeAll().map { entities ->
            entities.map { it.toDomain() }
                .filter { it.matches(query) }
                .let { list ->
                    when (query.sort) {
                        TransactionSort.NEWEST -> list.sortedWith(compareByDescending<FinanceTransaction> { it.date }.thenByDescending { it.createdAt })
                        TransactionSort.OLDEST -> list.sortedWith(compareBy<FinanceTransaction> { it.date }.thenBy { it.createdAt })
                        TransactionSort.HIGHEST_AMOUNT -> list.sortedByDescending { it.amount }
                        TransactionSort.LOWEST_AMOUNT -> list.sortedBy { it.amount }
                    }
                }
        }
    }

    override fun observeTransaction(id: Long): Flow<FinanceTransaction?> =
        dao.observeById(id).map { it?.toDomain() }

    override suspend fun getTransaction(id: Long): FinanceTransaction? =
        dao.getById(id)?.toDomain()

    override suspend fun saveTransaction(transaction: FinanceTransaction): Long =
        dao.insert(transaction.toEntity())

    override suspend fun deleteTransaction(id: Long) {
        dao.deleteById(id)
    }

    private fun FinanceTransaction.matches(query: TransactionQuery): Boolean {
        if (query.type != null && type != query.type) return false
        if (query.category != null && category != query.category) return false

        val searchText = query.search.trim()
        if (searchText.isNotBlank()) {
            val haystack = "$category $note $paymentMethod $amount".lowercase()
            if (!haystack.contains(searchText.lowercase())) return false
        }

        val (start, end) = when (query.dateFilter) {
            DateFilter.ALL -> null to null
            DateFilter.TODAY -> startOfToday() to endOfToday()
            DateFilter.THIS_WEEK -> startOfWeek() to System.currentTimeMillis()
            DateFilter.THIS_MONTH -> startOfMonth() to System.currentTimeMillis()
            DateFilter.CUSTOM -> query.customStart to query.customEnd
        }

        if (start != null && date < start) return false
        if (end != null && date > end) return false
        return true
    }
}
