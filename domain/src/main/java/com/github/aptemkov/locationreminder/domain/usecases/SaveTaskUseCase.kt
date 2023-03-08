package com.github.aptemkov.locationreminder.domain.usecases

import com.github.aptemkov.locationreminder.domain.models.Task
import com.github.aptemkov.locationreminder.domain.repository.TaskRepository

class SaveTaskUseCase(private val taskRepository: TaskRepository) {

    fun execute(task: Task): Pair<Boolean, Exception?> {
        return taskRepository.addTask(task)
    }

}