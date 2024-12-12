package com.example.habittrackerapp_.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.habittrackerapp_.data.HabitRepository
import kotlinx.coroutines.runBlocking

class HabitWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        return runBlocking {
            try {
                val repository = HabitRepository(applicationContext)
                repository.getHabits() // Получаем обновленные данные с сервера
                Result.success()
            } catch (e: Exception) {
                e.printStackTrace()
                Result.failure()
            }
        }
    }
}
