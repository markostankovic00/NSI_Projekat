package com.nsi_projekat.repository.di

import com.nsi_projekat.repository.implementations.AuthRepository
import com.nsi_projekat.repository.implementations.UsersDataRepository
import com.nsi_projekat.repository.interactors.AuthRepositoryInteractor
import com.nsi_projekat.repository.interactors.UsersDataRepositoryInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
open class RepoModule {

    @Provides
    @Singleton
    fun provideAuthRepository(): AuthRepositoryInteractor {
        return AuthRepository()
    }

    @Provides
    @Singleton
    fun provideUserDataRepository(): UsersDataRepositoryInteractor {
        return UsersDataRepository()
    }
}