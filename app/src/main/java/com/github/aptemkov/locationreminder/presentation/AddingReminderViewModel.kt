package com.github.aptemkov.locationreminder.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.aptemkov.locationreminder.domain.models.MapResponse
import com.github.aptemkov.locationreminder.domain.models.Task
import com.github.aptemkov.locationreminder.domain.usecases.SaveTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddingReminderViewModel @Inject constructor(
    private val saveTaskUseCase: SaveTaskUseCase,
) : ViewModel() {

    private val saveResultMutable = MutableLiveData<Pair<Boolean, Exception?>>()
    val saveResultLiveData: LiveData<Pair<Boolean, Exception?>> get() = saveResultMutable

    private val mapResponseMutable = MutableLiveData<MapResponse>()
    val mapResponseLiveData: LiveData<MapResponse> get() = mapResponseMutable

    fun saveLocation(response: MapResponse) {
        mapResponseMutable.value = response
    }

    fun saveTask(task: Task) {
        val result = saveTaskUseCase.execute(task)
        saveResultMutable.value = result
    }
    fun closeViewModel() {
        onCleared()
    }

}