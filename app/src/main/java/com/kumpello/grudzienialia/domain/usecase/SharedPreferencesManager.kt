package com.kumpello.grudzienialia.domain.usecase

import android.app.Activity
import android.content.Context
import javax.inject.Inject


class SharedPreferencesManager @Inject constructor(activity: Activity) {
    val sharedPreference =  activity.getSharedPreferences("LOGIN_DATA", Context.MODE_PRIVATE)
    var editor = sharedPreference.edit()
}