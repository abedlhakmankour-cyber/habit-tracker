package com.gentlman.habittracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gentlman.habittracker.data.database.HabitDatabaseProvider
import com.gentlman.habittracker.data.entity.HabitEntity
import com.gentlman.habittracker.data.repository.HabitRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

data class HabitUiState(
    val habits: List<HabitEntity> = emptyList(),
    val completedHabitIds: Set<Long> = emptySet(),
    val completedCount: Int = 0,
    val totalCount: Int = 0,
    val progress: Float = 0f
)

class HabitViewModel(application: Application) : AndroidViewModel(application) {

    private val database = HabitDatabaseProvider.getDatabase(application)

    private val repository = HabitRepository(
        database.habitDao(),
        database.habitCompletionDao()
    )

    private val today = LocalDate.now().toString()

    val uiState: StateFlow<HabitUiState> = combine(
        repository.getAllHabits(),
        repository.getCompletionsForDate(today)
    ) { habits, completions ->

        val completedIds = completions.map { it.habitId }.toSet()
        val completedCount = habits.count { it.id in completedIds }
        val totalCount = habits.size

        HabitUiState(
            habits = habits,
            completedHabitIds = completedIds,
            completedCount = completedCount,
            totalCount = totalCount,
            progress = if (totalCount == 0) {
                0f
            } else {
                completedCount.toFloat() / totalCount
            }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HabitUiState()
    )

    fun addHabit(name: String, color: Long) {
        val cleanName = name.trim()
        if (cleanName.isEmpty()) return

        viewModelScope.launch {
            repository.insertHabit(
                HabitEntity(
                    name = cleanName,
                    color = color
                )
            )
        }
    }

    fun updateHabit(habit: HabitEntity) {
        val cleanName = habit.name.trim()
        if (cleanName.isEmpty()) return

        viewModelScope.launch {
            repository.updateHabit(
                habit.copy(name = cleanName)
            )
        }
    }

    fun deleteHabit(habit: HabitEntity) {
        viewModelScope.launch {
            repository.deleteHabit(habit)
        }
    }

    fun completeHabit(habitId: Long) {
        viewModelScope.launch {
            repository.completeHabit(habitId, today)
        }
    }

    fun undoCompletion(habitId: Long) {
        viewModelScope.launch {
            repository.undoCompletion(habitId, today)
        }
    }
}
