package com.gentlman.habittracker.ui.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gentlman.habittracker.data.entity.HabitEntity

private val habitColors = listOf(
    0xFF356B68L,
    0xFFE57373L,
    0xFFFFB74DL,
    0xFFFFD54FL,
    0xFF81C784L,
    0xFF64B5F6L,
    0xFF9575CDL,
    0xFFBA68C8L
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditHabitScreen(
    habit: HabitEntity?,
    onSave: (String, Long) -> Unit,
    onDelete: (HabitEntity) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember(habit?.id, habit?.name) {
        mutableStateOf(habit?.name.orEmpty())
    }
    var selectedColor by remember(habit?.id, habit?.color) {
        mutableStateOf(habit?.color ?: habitColors.first())
    }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (habit == null) "Add Habit" else "Edit Habit")
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Habit name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Choose a color",
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                habitColors.forEach { colorValue ->
                    val selected = selectedColor == colorValue
                    Spacer(
                        modifier = Modifier
                            .size(28.dp)
                            .background(Color(colorValue), CircleShape)
                            .then(
                                if (selected) {
                                    Modifier.border(
                                        3.dp,
                                        MaterialTheme.colorScheme.primary,
                                        CircleShape
                                    )
                                } else {
                                    Modifier
                                }
                            )
                            .clickable { selectedColor = colorValue }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { onSave(name.trim(), selectedColor) },
                enabled = name.trim().isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Habit")
            }

            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancel")
            }

            if (habit != null) {
                TextButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Delete Habit", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }

    if (showDeleteDialog && habit != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete habit?") },
            text = {
                Text("This will permanently delete this habit and its completion history.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDelete(habit)
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
