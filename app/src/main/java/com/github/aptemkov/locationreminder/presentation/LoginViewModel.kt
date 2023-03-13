package com.github.aptemkov.locationreminder.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.aptemkov.locationreminder.domain.usecases.LogInUseCase
import com.github.aptemkov.locationreminder.domain.usecases.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val logInUseCase: LogInUseCase,
    private val registerUseCase: RegisterUseCase,
) : ViewModel() {

    private val errorMutable = MutableLiveData<Pair<Boolean, Exception?>>(Pair(false, null))
    val errorLiveData: LiveData<Pair<Boolean, Exception?>> get() = errorMutable


    fun createAccount(email: String, password: String) {

        viewModelScope.launch {
            val result = registerUseCase.execute(email, password)
            Log.i("AUTHORIZATION VM", "$result")
            errorMutable.value = result
        }

    }

    fun signIn(email: String, password: String) {

            viewModelScope.launch {
                val result = logInUseCase.execute(email, password)
                Log.i("AUTHORIZATION VM", "$result")
                println(errorMutable)
                errorMutable.value = result
            }

    }


}