package com.kumpello.grudzienialia.ui.screens.login.mainScreen

import androidx.lifecycle.ViewModel
import com.kumpello.grudzienialia.domain.usecase.FirebaseAuthentication
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(val firebaseAuthentication: FirebaseAuthentication): ViewModel() {

}