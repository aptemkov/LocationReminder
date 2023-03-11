package com.github.aptemkov.locationreminder.data.storage

import com.github.aptemkov.locationreminder.domain.models.Task

interface TaskStorage {

    fun add(task: Task): Pair<Boolean, Exception?>
    fun delete(task: Task)
    fun edit(task: Task)
    fun get(position: Int): Task
    fun startTasksListener(result: (List<Task>) -> Unit)
}