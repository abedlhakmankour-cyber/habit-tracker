package com.gentlman.habittracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gentlman.habittracker.data.entity.HabitCompletionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitCompletionDao {

    @Query("SELECT * FROM habit_completions WHERE date = :date")
    fun getCompletionsForDate(date: String): Flow<List<HabitCompletionEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCompletion(completion: HabitCompletionEntity)

    @Query(
        "DELETE FROM habit_completions " +
        "WHERE habitId = :habitId AND date = :date"
    )
    suspend fun deleteCompletion(habitId: Long, date: String)
}
