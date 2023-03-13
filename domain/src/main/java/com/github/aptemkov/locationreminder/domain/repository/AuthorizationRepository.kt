package com.github.aptemkov.locationreminder.domain.repository
import java.lang.Exception

interface AuthorizationRepository {
    suspend fun logIn(email: String, password: String): Pair<Boolean, Exception?>
    suspend fun register(email: String, password: String): Pair<Boolean, Exception?>
    fun logout()
}