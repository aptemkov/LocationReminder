package com.github.aptemkov.locationreminder.data.repository

import com.github.aptemkov.locationreminder.data.storage.TaskStorage
import com.github.aptemkov.locationreminder.domain.models.Task
import com.github.aptemkov.locationreminder.domain.repository.TaskRepository

class TaskRepositoryImpl(private val taskStorage: TaskStorage) : TaskRepository {

    override fun addTask(task: Task): Pair<Boolean, Exception?> {
        return taskStorage.add(task)
    }

    override fun deleteTask(task: Task) {
        return taskStorage.delete(task)
    }

    override fun editTask(task: Task) {
        return taskStorage.edit(task)
    }

    override fun getTask(position: Int): Task {
        return taskStorage.get(position)
    }

    override fun startTasksListener(result: (List<Task>) -> Unit) {
        return taskStorage.startTasksListener(result)
    }
}