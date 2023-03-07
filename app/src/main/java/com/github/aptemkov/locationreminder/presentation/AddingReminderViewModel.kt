package com.github.aptemkov.locationreminder.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.aptemkov.locationreminder.domain.MapResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddingReminderViewModel @Inject constructor() : ViewModel() {

    init {
        println("TEST $mapResponse")
    }


    private val mutableMapResponse = MutableLiveData<MapResponse>()
    val mapResponse: LiveData<MapResponse> get() = mutableMapResponse

    fun sendResponse(response: MapResponse) {
        println("\n\n\n before " + response)
        mutableMapResponse.value = response
        println("\n\n\n after (mutable) " + mutableMapResponse.value)
        println("\n\n\n after (not mutable) " + mapResponse.value)

    }


}