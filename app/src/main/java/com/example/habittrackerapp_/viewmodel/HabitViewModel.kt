package com.example.habittrackerapp_.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittrackerapp_.data.Habit
import com.example.habittrackerapp_.data.HabitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HabitViewModel(
    private val repository: HabitRepository
) : ViewModel() {

    private val _habits = MutableStateFlow<List<Habit>>(emptyList())
    val habits: StateFlow<List<Habit>> = _habits

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error


    init {
        loadHabits()
    }

    private fun loadHabits() {
        viewModelScope.launch {
            try {
                _habits.value = repository.getHabits()
            } catch (e: Exception) {
                handleError(e.message ?: "Ошибка")
            }
        }
    }

    fun updateHabitStatus(habit: Habit, completed: Boolean) {
        viewModelScope.launch {
            try {
                // Обновляем статус привычки в локальном хранилище
                repository.updateHabitStatus(habit, completed)

                // Обновляем данные списка, чтобы UI обновился
                _habits.value = _habits.value.map {
                    if (it.id == habit.id) it.copy(completed = completed) else it
                }
            } catch (e: Exception) {
                handleError(e.message ?: "Ошибка обновления привычки")
            }
        }
    }


    private fun handleError(message: String) {
        _error.value = message
    }

    fun dismissError() {
        _error.value = null // Сбрасываем ошибку
    }

}
