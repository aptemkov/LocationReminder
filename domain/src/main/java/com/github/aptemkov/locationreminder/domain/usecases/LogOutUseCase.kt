package com.github.aptemkov.locationreminder.domain.usecases

import com.github.aptemkov.locationreminder.domain.repository.AuthorizationRepository

class LogOutUseCase(private val repository: AuthorizationRepository) {

    fun execute() {
        return repository.logout()
    }

}