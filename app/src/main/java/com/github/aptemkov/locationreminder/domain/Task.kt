package com.github.aptemkov.locationreminder.domain

import java.util.*

data class Task(

    val title: String = "",
    val description: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val isActive: Boolean = true,
    val reminderRange: Double = 0.0,
    val createdAt: Long = Calendar.getInstance().timeInMillis,

    ) {
    /*constructor() : this(
        title = "Title",
        description = "Description",
        latitude= 0.0,
        longitude = 0.0,
        isActive = true,
        reminderRange = 0,
        createdAt = 0L
    )*/
}