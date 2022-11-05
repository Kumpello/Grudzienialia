package com.kumpello.grudzienialia.domain.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kumpello.grudzienialia.domain.usecase.Database
import com.kumpello.grudzienialia.domain.usecase.FirebaseAuthentication
import com.kumpello.grudzienialia.domain.usecase.FriendsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private val firebaseDatabase : DatabaseReference = Firebase.database.reference
    private val firebaseAuth : FirebaseAuth = Firebase.auth

    //Todo Change to some interfaces
    @Provides
    fun provideFirebaseAuthentication(): FirebaseAuthentication {
        return FirebaseAuthentication(firebaseAuth)
    }

    @Provides
    fun provideDatabase(): Database {
        return Database(firebaseDatabase)
    }

    @Provides
    fun provideFriendsService(): FriendsService {
        return FriendsService(firebaseAuth, firebaseDatabase)
    }

}
