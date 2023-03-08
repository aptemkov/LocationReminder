package com.github.aptemkov.locationreminder.domain.usecases

import androidx.lifecycle.LiveData
import com.github.aptemkov.locationreminder.domain.models.Task
import com.github.aptemkov.locationreminder.domain.repository.TaskRepository

class GetTaskListUseCase(private val taskRepository: TaskRepository) {

    fun execute(): LiveData<List<Task>> {
        return taskRepository.getTasksList()
    }

}