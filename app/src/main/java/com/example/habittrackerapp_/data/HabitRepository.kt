package com.example.habittrackerapp_.data

import android.content.Context
import com.example.habittrackerapp_.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HabitRepository(private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("habit_preferences", Context.MODE_PRIVATE)

    suspend fun getHabits(): List<Habit> = withContext(Dispatchers.IO) {
        try {
            val habits = ApiService.fetchHabits()
            if (habits != null) {
                saveHabitsToLocal(habits)
                return@withContext habits
            }
        } catch (e: Exception) {
            throw Exception("Ошибка: ${e.message}")
        }
        return@withContext getHabitsFromLocal()
    }


    private fun saveHabitsToLocal(habits: List<Habit>) {
        val editor = sharedPreferences.edit()
        val json = habits.joinToString(prefix = "[", postfix = "]") { habit ->
            """
            {
                "id": ${habit.id},
                "name": "${habit.name}",
                "goal": "${habit.goal}",
                "completed": ${habit.completed},
                "started": ${habit.started},
                "started_at": "${habit.startedAt ?: "null"}",
                "completed_at": "${habit.completedAt ?: "null"}"
            }
            """
        }
        editor.putString("habits", json)
        editor.apply()
    }

    private fun getHabitsFromLocal(): List<Habit> {
        val habitJson = sharedPreferences.getString("habits", "[]")
        val habitList = mutableListOf<Habit>()

        habitJson?.let {
            val jsonArray = org.json.JSONArray(it)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                habitList.add(
                    Habit(
                        id = jsonObject.getInt("id"),
                        name = jsonObject.getString("name"),
                        goal = jsonObject.getString("goal"),
                        completed = jsonObject.getBoolean("completed"),
                        started = jsonObject.getBoolean("started"),
                        startedAt = jsonObject.optString("started_at", null),
                        completedAt = jsonObject.optString("completed_at", null)
                    )
                )
            }
        }
        return habitList
    }

    suspend fun updateHabitStatus(habit: Habit, completed: Boolean) {
        withContext(Dispatchers.IO) {
            val updatedHabits = getHabitsFromLocal().map {
                if (it.id == habit.id) it.copy(completed = completed) else it
            }
            saveHabitsToLocal(updatedHabits)
        }
    }
}
