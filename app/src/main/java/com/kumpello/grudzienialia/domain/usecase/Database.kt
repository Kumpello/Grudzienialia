package com.kumpello.grudzienialia.domain.usecase

import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class Database @Inject constructor(database: DatabaseReference) {


}