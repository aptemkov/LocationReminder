package com.github.aptemkov.locationreminder.di

import com.github.aptemkov.locationreminder.data.repository.AuthorizationRepositoryImpl
import com.github.aptemkov.locationreminder.data.repository.TaskRepositoryImpl
import com.github.aptemkov.locationreminder.data.storage.FirebaseTaskStorage
import com.github.aptemkov.locationreminder.data.storage.TaskStorage
import com.github.aptemkov.locationreminder.domain.repository.AuthorizationRepository
import com.github.aptemkov.locationreminder.domain.repository.TaskRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseTaskStorage(
        firebaseAuth: FirebaseAuth,
        firebaseFirestore: FirebaseFirestore
        /*@ApplicationContext context: Context*/
    ): TaskStorage {
        return FirebaseTaskStorage(firebaseAuth, firebaseFirestore)
    }

    @Provides
    @Singleton
    fun provideTaskRepositoryImpl(taskStorage: TaskStorage): TaskRepository {
        return TaskRepositoryImpl(taskStorage = taskStorage)
    }

    @Provides
    @Singleton
    fun provideAuthRepositoryImpl(auth: FirebaseAuth): AuthorizationRepository {
        return AuthorizationRepositoryImpl(auth)
    }

}