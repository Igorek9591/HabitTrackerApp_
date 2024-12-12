package com.example.habittrackerapp_.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.habittrackerapp_.data.Habit
import com.example.habittrackerapp_.ui.components.HabitItem
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.rememberCoroutineScope

@Composable
fun HabitListScreen(
    habits: List<Habit>,
    onHabitClick: (Habit) -> Unit,
    onStatusChange: (Habit, Boolean) -> Unit,
    errorMessage: String? = null,
    onDismissError: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    if (errorMessage != null) {
        LaunchedEffect(errorMessage) {
            snackbarHostState.showSnackbar(errorMessage)
            onDismissError()
        }
    }

    Box {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(habits.size) { index ->
                val habit = habits[index]
                HabitItem(
                    habit = habit,
                    onClick = { onHabitClick(habit) },
                    onStatusChange = { isChecked ->
                        onStatusChange(habit, isChecked)
                    }
                )
            }
        }

        SnackbarHost(hostState = snackbarHostState)
    }
}