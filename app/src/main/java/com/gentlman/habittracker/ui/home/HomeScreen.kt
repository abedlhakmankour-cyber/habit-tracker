package com.gentlman.habittracker.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gentlman.habittracker.data.entity.HabitEntity
import com.gentlman.habittracker.viewmodel.HabitUiState
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HabitUiState,
    onCompletionChange: (Long, Boolean) -> Unit,
    onAddClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Habit Tracker") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Text("+", fontSize = 28.sp)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp)
        ) {
            Text(
                text = LocalDate.now().toString(),
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Today's Progress",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${uiState.completedCount} of ${uiState.totalCount} habits completed"
            )
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { uiState.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))

            if (uiState.habits.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No habits yet.\nTap + to add your first habit.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = uiState.habits,
                        key = { habit -> habit.id }
                    ) { habit ->
                        HabitCard(
                            habit = habit,
                            isCompleted = habit.id in uiState.completedHabitIds,
                            onCompletionChange = onCompletionChange
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HabitCard(
    habit: HabitEntity,
    isCompleted: Boolean,
    onCompletionChange: (Long, Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .background(
                        color = Color(habit.color),
                        shape = CircleShape
                    )
            )
            Text(
                text = habit.name,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            )
            Checkbox(
                checked = isCompleted,
                onCheckedChange = { checked ->
                    onCompletionChange(habit.id, checked)
                }
            )
        }
    }
}
