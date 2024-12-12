package com.example.habittrackerapp_

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.habittrackerapp_.data.Habit
import com.example.habittrackerapp_.data.HabitRepository
import com.example.habittrackerapp_.ui.screens.HabitDetailScreen
import com.example.habittrackerapp_.ui.screens.HabitListScreen
import com.example.habittrackerapp_.utils.Utils
import com.example.habittrackerapp_.viewmodel.HabitViewModel
import com.example.habittrackerapp_.viewmodel.HabitViewModelFactory

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: HabitViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = HabitRepository(applicationContext)
        viewModel = ViewModelProvider(this, HabitViewModelFactory(repository))[HabitViewModel::class.java]
        Utils.scheduleHabitWorker(this)

        setContent {
            val navController = rememberNavController()
            val habits = viewModel.habits.collectAsState().value
            val errorMessage = viewModel.error.collectAsState().value

            NavHost(navController = navController, startDestination = "habit_list") {
                composable("habit_list") {
                    HabitListScreen(
                        habits = habits,
                        onHabitClick = { habit ->
                            navController.navigate("habit_detail/${habit.id}")
                        },
                        onStatusChange = { habit, completed ->
                            viewModel.updateHabitStatus(habit, completed)
                        },
                        errorMessage = errorMessage,         // Передаём сообщение об ошибке
                        onDismissError = { viewModel.dismissError() } // Добавляем обработчик сброса ошибки

                    )
                }
                composable("habit_detail/{habitId}") { backStackEntry ->
                    val habitId = backStackEntry.arguments?.getString("habitId")?.toIntOrNull()
                    val selectedHabit = habits.find { it.id == habitId }
                    selectedHabit?.let { HabitDetailScreen(it) }
                }
            }
        }
    }
}
