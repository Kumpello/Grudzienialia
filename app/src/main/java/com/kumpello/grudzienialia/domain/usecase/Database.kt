package com.kumpello.grudzienialia.domain.usecase

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class Database @Inject constructor() {

    val database = Firebase.database

}