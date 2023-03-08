package com.github.aptemkov.locationreminder.di

import com.github.aptemkov.locationreminder.domain.repository.TaskRepository
import com.github.aptemkov.locationreminder.domain.usecases.GetTaskListUseCase
import com.github.aptemkov.locationreminder.domain.usecases.SaveTaskUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object DomainModule {

    @Provides
    fun provideGetTaskListUseCase(taskRepository: com.github.aptemkov.locationreminder.domain.repository.TaskRepository): com.github.aptemkov.locationreminder.domain.usecases.GetTaskListUseCase {
        return com.github.aptemkov.locationreminder.domain.usecases.GetTaskListUseCase(
            taskRepository = taskRepository)
    }

    @Provides
    fun provideSaveTaskUseCase(taskRepository: com.github.aptemkov.locationreminder.domain.repository.TaskRepository): com.github.aptemkov.locationreminder.domain.usecases.SaveTaskUseCase {
        return com.github.aptemkov.locationreminder.domain.usecases.SaveTaskUseCase(taskRepository = taskRepository)
    }

}