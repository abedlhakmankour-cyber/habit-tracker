package com.gentlman.habittracker.data.repository

import com.gentlman.habittracker.data.dao.HabitCompletionDao
import com.gentlman.habittracker.data.dao.HabitDao
import com.gentlman.habittracker.data.entity.HabitCompletionEntity
import com.gentlman.habittracker.data.entity.HabitEntity
import kotlinx.coroutines.flow.Flow

class HabitRepository(
    private val habitDao: HabitDao,
    private val completionDao: HabitCompletionDao
) {

    fun getAllHabits(): Flow<List<HabitEntity>> {
        return habitDao.getAllHabits()
    }

    fun getCompletionsForDate(date: String): Flow<List<HabitCompletionEntity>> {
        return completionDao.getCompletionsForDate(date)
    }

    suspend fun insertHabit(habit: HabitEntity): Long {
        return habitDao.insertHabit(habit)
    }

    suspend fun updateHabit(habit: HabitEntity) {
        habitDao.updateHabit(habit)
    }

    suspend fun deleteHabit(habit: HabitEntity) {
        habitDao.deleteHabit(habit)
    }

    suspend fun completeHabit(habitId: Long, date: String) {
        completionDao.insertCompletion(
            HabitCompletionEntity(
                habitId = habitId,
                date = date
            )
        )
    }

    suspend fun undoCompletion(habitId: Long, date: String) {
        completionDao.deleteCompletion(habitId, date)
    }
}
