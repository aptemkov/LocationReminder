package com.github.aptemkov.locationreminder.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.aptemkov.locationreminder.domain.models.Task
import com.github.aptemkov.locationreminder.domain.usecases.LogOutUseCase
import com.github.aptemkov.locationreminder.domain.usecases.SubscribeToTaskListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ListViewModel @Inject constructor(
    private val getTaskListUseCase: SubscribeToTaskListUseCase,
    private val logOutUseCase: LogOutUseCase
) : ViewModel() {

    private val tasksMutable = MutableLiveData<List<Task>>()
    val tasksLiveData: LiveData<List<Task>> get() = tasksMutable

    private val isAuthorizatedMutable = MutableLiveData(true)
    val isAuthorizated: LiveData<Boolean> get() = isAuthorizatedMutable

    init {
        getTaskListUseCase.execute {
            Log.i("TEST vm got list", "$it")
            tasksMutable.value = it
        }
    }

    fun signOut() {
        logOutUseCase.execute()
        isAuthorizatedMutable.value = false
    }

}