package com.example.habittrackerapp_.utils

import android.content.Context
import androidx.work.*
import com.example.habittrackerapp_.worker.HabitWorker
import java.util.concurrent.TimeUnit

object Utils {

    fun scheduleHabitWorker(context: Context) {
        val workRequest = PeriodicWorkRequestBuilder<HabitWorker>(
            30, TimeUnit.SECONDS
            //15, TimeUnit.MINUTES // Интервал 15 минут
        ).setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "HabitWorker",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }
}
