package com.fintrack.app.data.repository

import com.fintrack.app.data.local.dao.GoalDao
import com.fintrack.app.data.local.entity.toDomain
import com.fintrack.app.data.local.entity.toEntity
import com.fintrack.app.domain.model.SavingsGoal
import com.fintrack.app.domain.repository.GoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GoalRepositoryImpl @Inject constructor(
    private val dao: GoalDao
) : GoalRepository {
    override fun observeGoals(): Flow<List<SavingsGoal>> =
        dao.observeAll().map { entities -> entities.map { it.toDomain() } }

    override suspend fun saveGoal(goal: SavingsGoal): Long = dao.upsert(goal.toEntity())

    override suspend fun deleteGoal(id: Long) {
        dao.deleteById(id)
    }
}
