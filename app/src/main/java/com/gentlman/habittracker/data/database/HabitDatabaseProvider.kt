package com.gentlman.habittracker.data.database

import android.content.Context
import androidx.room.Room

object HabitDatabaseProvider {

    @Volatile
    private var INSTANCE: HabitDatabase? = null

    fun getDatabase(context: Context): HabitDatabase {
        return INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                HabitDatabase::class.java,
                "habit_tracker_database"
            ).build().also {
                INSTANCE = it
            }
        }
    }
}
