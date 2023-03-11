package com.github.aptemkov.locationreminder.domain.usecases

import com.github.aptemkov.locationreminder.domain.models.Task
import com.github.aptemkov.locationreminder.domain.repository.TaskRepository

class SubscribeToTaskListUseCase(private val taskRepository: TaskRepository) {

    fun execute(result: (List<Task>) -> Unit) {
        return taskRepository.startTasksListener(result)
    }

}