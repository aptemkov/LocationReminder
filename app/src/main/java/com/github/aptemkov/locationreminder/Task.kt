package com.github.aptemkov.locationreminder

data class Task(
    /*
    var name: String,
    var lastName: String,
    var age: Int,
    var sex: String,
    */
    val id: Long = 0L,
    val title: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val isActive: Boolean,
    val reminderRange: Int,
    val createdAt: Long,

) {
    constructor() : this(
        title = "Title",
        description = "Description",
        latitude= 0.0,
        longitude = 0.0,
        isActive = true,
        reminderRange = 0,
        createdAt = 0L
    )
}