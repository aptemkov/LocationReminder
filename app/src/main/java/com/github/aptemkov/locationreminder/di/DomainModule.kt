package com.github.aptemkov.locationreminder.di

import com.github.aptemkov.locationreminder.domain.repository.TaskRepository
import com.github.aptemkov.locationreminder.domain.usecases.SubscribeToTaskListUseCase
import com.github.aptemkov.locationreminder.domain.usecases.SaveTaskUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object DomainModule {

    @Provides
    fun provideGetTaskListUseCase(taskRepository: TaskRepository): SubscribeToTaskListUseCase {
        return SubscribeToTaskListUseCase(
            taskRepository = taskRepository)
    }

    @Provides
    fun provideSaveTaskUseCase(taskRepository: TaskRepository): SaveTaskUseCase {
        return SaveTaskUseCase(taskRepository = taskRepository)
    }

}