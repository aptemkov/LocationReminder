package com.github.aptemkov.locationreminder.di

import com.github.aptemkov.locationreminder.data.repository.TaskRepositoryImpl
import com.github.aptemkov.locationreminder.data.storage.FirebaseTaskStorage
import com.github.aptemkov.locationreminder.data.storage.TaskStorage
import com.github.aptemkov.locationreminder.domain.repository.TaskRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    /*@Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }*/

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideTaskStorage(/*@ApplicationContext context: Context*/): com.github.aptemkov.locationreminder.data.storage.TaskStorage {
        return com.github.aptemkov.locationreminder.data.storage.FirebaseTaskStorage()
    }

    @Provides
    @Singleton
    fun provideTaskRepositoryImpl(taskStorage: com.github.aptemkov.locationreminder.data.storage.TaskStorage): com.github.aptemkov.locationreminder.domain.repository.TaskRepository {
        return com.github.aptemkov.locationreminder.data.repository.TaskRepositoryImpl(taskStorage = taskStorage)
    }

}