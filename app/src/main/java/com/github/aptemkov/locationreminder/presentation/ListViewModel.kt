package com.github.aptemkov.locationreminder.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.github.aptemkov.locationreminder.domain.models.Task
import com.github.aptemkov.locationreminder.domain.usecases.GetTaskListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ListViewModel @Inject constructor(
    getTaskListUseCase: GetTaskListUseCase,
) : ViewModel() {

    private val tasksMutable: LiveData<List<Task>> = getTaskListUseCase.execute()
    val tasksLiveData: LiveData<List<Task>> get() = tasksMutable

}