package com.github.aptemkov.locationreminder

import android.location.Location
import com.github.aptemkov.locationreminder.domain.models.Task
import kotlinx.coroutines.flow.Flow

interface LocationClient {
    fun getLocationUpdates(interval: Long): Flow<Location>
    fun updateTasksList(list: List<Task>)

    class LocationException(message: String): Exception()
}