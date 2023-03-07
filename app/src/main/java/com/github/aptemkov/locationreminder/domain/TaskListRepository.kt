package com.github.aptemkov.locationreminder.domain

import androidx.lifecycle.LiveData

interface TasksListRepository {

    fun addTask(task: Task): Pair<Boolean, java.lang.Exception?>
    fun deleteTask(task: Task)
    fun editTask(task: Task)
    fun getTask(position: Int): Task
    fun getTasksList(): LiveData<List<Task>>

}