package com.gentlman.habittracker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gentlman.habittracker.data.dao.HabitCompletionDao
import com.gentlman.habittracker.data.dao.HabitDao
import com.gentlman.habittracker.data.entity.HabitCompletionEntity
import com.gentlman.habittracker.data.entity.HabitEntity

@Database(
    entities = [
        HabitEntity::class,
        HabitCompletionEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class HabitDatabase : RoomDatabase() {

    abstract fun habitDao(): HabitDao

    abstract fun habitCompletionDao(): HabitCompletionDao
}
