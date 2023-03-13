package com.github.aptemkov.locationreminder.domain.usecases

import com.github.aptemkov.locationreminder.domain.repository.AuthorizationRepository
import java.lang.Exception

class RegisterUseCase(private val repository: AuthorizationRepository) {

    suspend fun execute(email: String, password: String): Pair<Boolean, Exception?> {
        return repository.register(email, password)
    }

}