package com.github.aptemkov.locationreminder.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.aptemkov.locationreminder.domain.models.Task
import com.github.aptemkov.locationreminder.domain.usecases.SubscribeToTaskListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ListViewModel @Inject constructor(
    getTaskListUseCase: SubscribeToTaskListUseCase,
) : ViewModel() {


    private val tasksMutable = MutableLiveData<List<Task>>()
    val tasksLiveData: LiveData<List<Task>> get() = tasksMutable

    init {
        getTaskListUseCase.execute {
            Log.i("TEST vm got list", "$it")
            tasksMutable.value = it
        }
    }

}