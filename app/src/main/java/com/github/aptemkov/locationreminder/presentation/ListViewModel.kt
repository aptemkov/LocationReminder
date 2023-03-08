package com.github.aptemkov.locationreminder.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.github.aptemkov.locationreminder.domain.models.Task
import com.github.aptemkov.locationreminder.domain.usecases.GetTaskListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ListViewModel @Inject constructor(
    getTaskListUseCase: com.github.aptemkov.locationreminder.domain.usecases.GetTaskListUseCase,
) : ViewModel() {

    private val tasksMutable: LiveData<List<com.github.aptemkov.locationreminder.domain.models.Task>> = getTaskListUseCase.execute()
    val tasksLiveData: LiveData<List<com.github.aptemkov.locationreminder.domain.models.Task>> get() = tasksMutable

}