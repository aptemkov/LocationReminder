package com.github.aptemkov.locationreminder.domain.usecases

import com.github.aptemkov.locationreminder.domain.repository.AuthorizationRepository

class LogInUseCase(private val repository: AuthorizationRepository) {

    suspend fun execute(email: String, password: String): Pair<Boolean, Exception?> {
        return repository.logIn(email, password)
    }

}