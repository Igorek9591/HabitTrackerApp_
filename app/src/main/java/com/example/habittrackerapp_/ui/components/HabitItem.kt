package com.example.habittrackerapp_.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.habittrackerapp_.data.Habit

@Composable
fun HabitItem(
    habit: Habit,
    onClick: () -> Unit,
    onStatusChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = habit.name, style = MaterialTheme.typography.bodyLarge)
            Text(text = habit.goal, style = MaterialTheme.typography.bodySmall)
        }
        Checkbox(
            checked = habit.completed,
            onCheckedChange = { isChecked ->
                onStatusChange(isChecked) // Передача нового состояния
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = onClick) {
            Text(text = "Подробно")
        }
    }
}
