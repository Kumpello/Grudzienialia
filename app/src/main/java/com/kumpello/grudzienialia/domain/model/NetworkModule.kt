package com.kumpello.grudzienialia.domain.model

import com.kumpello.grudzienialia.domain.usecase.FirebaseAuthentication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    //Change to some interface
    @Provides
    fun provideFirebaseAuthentication(): FirebaseAuthentication {
        return FirebaseAuthentication()
    }

}
