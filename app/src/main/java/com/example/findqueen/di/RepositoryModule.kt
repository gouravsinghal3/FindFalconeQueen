package com.example.findqueen.di

import com.example.findqueen.data.repository.FalconeRepositoryImpl
import com.example.findqueen.domain.repository.FalconeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindFalconeRepository(
        falconeRepositoryImpl: FalconeRepositoryImpl
    ): FalconeRepository
}
