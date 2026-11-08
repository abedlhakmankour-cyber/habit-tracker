package com.gentlman.habittracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.gentlman.habittracker.data.entity.HabitEntity
import com.gentlman.habittracker.ui.edit.EditHabitScreen
import com.gentlman.habittracker.ui.home.HomeScreen
import com.gentlman.habittracker.ui.theme.HabitTrackerTheme
import com.gentlman.habittracker.viewmodel.HabitViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HabitTrackerApp()
        }
    }
}

@Composable
fun HabitTrackerApp() {
    val habitViewModel: HabitViewModel = viewModel()
    val uiState by habitViewModel.uiState.collectAsStateWithLifecycle()

    var selectedHabit by remember { mutableStateOf<HabitEntity?>(null) }
    var showEditor by remember { mutableStateOf(false) }

    HabitTrackerTheme {
        if (showEditor) {
            EditHabitScreen(
                habit = selectedHabit,
                onSave = { name, color ->
                    val currentHabit = selectedHabit
                    if (currentHabit == null) {
                        habitViewModel.addHabit(name, color)
                    } else {
                        habitViewModel.updateHabit(
                            currentHabit.copy(name = name, color = color)
                        )
                    }
                    selectedHabit = null
                    showEditor = false
                },
                onDelete = { habit ->
                    habitViewModel.deleteHabit(habit)
                    selectedHabit = null
                    showEditor = false
                },
                onCancel = {
                    selectedHabit = null
                    showEditor = false
                }
            )
        } else {
            HomeScreen(
                uiState = uiState,
                onCompletionChange = { habitId, isCompleted ->
                    if (isCompleted) {
                        habitViewModel.completeHabit(habitId)
                    } else {
                        habitViewModel.undoCompletion(habitId)
                    }
                },
                onEditClick = { habit ->
                    selectedHabit = habit
                    showEditor = true
                },
                onAddClick = {
                    selectedHabit = null
                    showEditor = true
                }
            )
        }
    }
}
