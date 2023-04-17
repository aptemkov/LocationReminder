package com.github.aptemkov.locationreminder.domain.usecases

import com.github.aptemkov.locationreminder.domain.models.Task
import com.github.aptemkov.locationreminder.domain.repository.TaskRepository

class DeleteTaskUseCase(private val taskRepository: TaskRepository) {

    fun execute(task: Task) {
        return taskRepository.deleteTask(task)
    }

}