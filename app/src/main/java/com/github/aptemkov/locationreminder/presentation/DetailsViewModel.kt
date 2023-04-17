package com.github.aptemkov.locationreminder.presentation

import androidx.lifecycle.ViewModel
import com.github.aptemkov.locationreminder.domain.models.Task
import com.github.aptemkov.locationreminder.domain.usecases.DeleteTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val deleteTaskUseCase: DeleteTaskUseCase
): ViewModel() {

    fun delete(task: Task) {
        deleteTaskUseCase.execute(task)
    }

}