package com.kumpello.grudzienialia.ui.screens.application.applicationScreen

import androidx.lifecycle.ViewModel
import com.kumpello.grudzienialia.domain.usecase.Database
import com.kumpello.grudzienialia.domain.usecase.FriendsService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ApplicationActivityViewModel @Inject constructor(val friendsService: FriendsService, val database: Database): ViewModel() {
}