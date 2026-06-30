package com.fintrack.app.data.repository

import com.fintrack.app.data.local.dao.BudgetDao
import com.fintrack.app.data.local.entity.toDomain
import com.fintrack.app.data.local.entity.toEntity
import com.fintrack.app.domain.model.Budget
import com.fintrack.app.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BudgetRepositoryImpl @Inject constructor(
    private val dao: BudgetDao
) : BudgetRepository {
    override fun observeBudgets(): Flow<List<Budget>> =
        dao.observeAll().map { entities -> entities.map { it.toDomain() } }

    override fun observeBudgetsForMonth(month: Int, year: Int): Flow<List<Budget>> =
        dao.observeForMonth(month, year).map { entities -> entities.map { it.toDomain() } }

    override suspend fun saveBudget(budget: Budget): Long = dao.upsert(budget.toEntity())

    override suspend fun deleteBudget(id: Long) {
        dao.deleteById(id)
    }
}
