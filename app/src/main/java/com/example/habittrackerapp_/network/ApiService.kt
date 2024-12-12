package com.example.habittrackerapp_.network

import com.example.habittrackerapp_.data.Habit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

object ApiService {
    private const val BASE_URL = "https://mej1g.wiremockapi.cloud/habits"

    private val client = OkHttpClient()

    suspend fun fetchHabits(): List<Habit>? = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url(BASE_URL)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return@withContext null
            val responseData = response.body?.string() ?: return@withContext null
            parseHabits(responseData)
        }
    }

    fun parseHabits(response: String): List<Habit> {
        val jsonObject = JSONObject(response)
        val habitsArray = jsonObject.getJSONArray("habits") // Access the array within the object
        val habitsList = mutableListOf<Habit>()

        for (i in 0 until habitsArray.length()) {
            val habitObject = habitsArray.getJSONObject(i)
            val habit = Habit(
                id = habitObject.getInt("id"),
                name = habitObject.getString("name"),
                goal = habitObject.getString("goal"),
                completed = habitObject.getBoolean("completed"),
                started = habitObject.getBoolean("started"),
                startedAt = habitObject.optString("started_at", null),
                completedAt = habitObject.optString("completed_at", null)
            )
            habitsList.add(habit)
        }
        return habitsList
    }
}
