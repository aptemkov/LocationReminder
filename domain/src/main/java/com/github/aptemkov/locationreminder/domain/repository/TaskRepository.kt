package com.github.aptemkov.locationreminder.domain.repository

import androidx.lifecycle.LiveData
import com.github.aptemkov.locationreminder.domain.models.Task
import java.lang.Exception

interface TaskRepository {

    fun addTask(task: Task): Pair<Boolean, Exception?>
    fun deleteTask(task: Task)
    fun editTask(task: Task)
    fun getTask(position: Int): Task
    fun getTasksList(): LiveData<List<Task>>

}