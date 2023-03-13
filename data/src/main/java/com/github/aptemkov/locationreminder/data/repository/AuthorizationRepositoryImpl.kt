package com.github.aptemkov.locationreminder.data.repository

import android.util.Log
import com.github.aptemkov.locationreminder.domain.repository.AuthorizationRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import java.lang.Exception

class AuthorizationRepositoryImpl @Inject constructor(private val auth: FirebaseAuth) :
    AuthorizationRepository {

    override suspend fun logIn(email: String, password: String): Pair<Boolean, Exception?> {

        var result: Pair<Boolean, Exception?> = Pair(false, null)

        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d(TAG, "LogIn:success")
                    } else {
                        Log.w(TAG, "LogIn:failure")
                    }
                }.await()

            result = Pair<Boolean, Exception?>(true, null)
        } catch (e: Exception) {
            Log.i(TAG, "-e: ${e.message}")
            result = Pair<Boolean, Exception?>(false, e)

        } finally {
            Log.i(TAG, "-finally: $result")
            return result
        }
    }

    override suspend fun register(email: String, password: String): Pair<Boolean, Exception?> {
        var result: Pair<Boolean, Exception?> = Pair(false, null)


        try {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d(TAG, "Reg:success")
                    } else {
                        Log.w(TAG, "Reg:failure")
                    }
                }.await()

            result = Pair<Boolean, Exception?>(true, null)
        } catch (e: Exception) {
            Log.i(TAG, "-e: ${e.message}")
            result = Pair<Boolean, Exception?>(false, e)

        } finally {
            Log.i(TAG, "-finally: $result")
            return result
        }
    }

    override fun logout() {
        auth.signOut()
    }

    companion object {
        private const val TAG = "AUTHORIZATION"
    }
}