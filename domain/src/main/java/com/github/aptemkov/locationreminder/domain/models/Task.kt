package com.github.aptemkov.locationreminder.domain.models

import java.io.Serializable
import java.util.*

data class Task(

    val title: String = "",
    val description: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val active: Boolean = true,
    val reminderRange: Double = 0.0,
    val createdAt: Long = Calendar.getInstance().timeInMillis,
    val taskId: Int = (Math.random() * 1_000_000_000).toInt()
): Serializable