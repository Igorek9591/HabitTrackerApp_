package com.example.habittrackerapp_.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.habittrackerapp_.data.Habit



@Composable
fun HabitDetailScreen(habit: Habit) {
    // Форматируем строки вручную, проверяя их на null и "null"
    val formattedStartedAt = if (habit.startedAt.isNullOrBlank() || habit.startedAt == "null") {
        "Не начата"
    } else {
        habit.startedAt.replace("T", " ").take(19) // Убираем 'T' и оставляем дату и время
    }

    val formattedCompletedAt = if (habit.completedAt.isNullOrBlank() || habit.completedAt == "null") {
        "Не закончена"
    } else {
        habit.completedAt.replace("T", " ").take(19) // Убираем 'T' и оставляем дату и время
    }


    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "О привычке", style = MaterialTheme.typography.headlineMedium)
            Text(text = "Название: ${habit.name}")
            Text(text = "Время: ${habit.goal}")
            Text(text = "Начата: $formattedStartedAt")
            Text(text = "Закончена: $formattedCompletedAt")
        }
    }
}

