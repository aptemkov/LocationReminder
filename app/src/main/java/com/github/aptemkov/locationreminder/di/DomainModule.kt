package com.github.aptemkov.locationreminder.di

import com.github.aptemkov.locationreminder.domain.repository.AuthorizationRepository
import com.github.aptemkov.locationreminder.domain.repository.TaskRepository
import com.github.aptemkov.locationreminder.domain.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Singleton

@Module
@InstallIn(ServiceComponent::class, ViewModelComponent::class)
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

    @Provides
    fun provideLogInUseCase(authorizationRepository: AuthorizationRepository): LogInUseCase {
        return LogInUseCase(repository = authorizationRepository)
    }

    @Provides
    fun provideRegisterUseCase(authorizationRepository: AuthorizationRepository): RegisterUseCase {
        return RegisterUseCase(repository = authorizationRepository)
    }

    @Provides
    fun provideLogOutUseCase(authorizationRepository: AuthorizationRepository): LogOutUseCase {
        return LogOutUseCase(repository = authorizationRepository)
    }

}