package com.example.habittrackerapp_.data

data class Habit(
    val id: Int,
    val name: String,
    val goal: String,
    val completed: Boolean,
    val started: Boolean,
    val startedAt: String?,
    val completedAt: String?
)
