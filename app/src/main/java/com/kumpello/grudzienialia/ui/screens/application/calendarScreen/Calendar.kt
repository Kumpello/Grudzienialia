package com.kumpello.grudzienialia.ui.screens.application.calendarScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.himanshoe.kalendar.Kalendar
import com.himanshoe.kalendar.model.KalendarType

@Composable
fun Calendar() {
    Scaffold(content = { padding ->
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Kalendar(kalendarType = KalendarType.Firey)
        }
    })
}

@Composable
fun SelectedDay() {

}

@Preview(showBackground = true)
@Composable
fun CalendarPreview() {
    Calendar()
}
