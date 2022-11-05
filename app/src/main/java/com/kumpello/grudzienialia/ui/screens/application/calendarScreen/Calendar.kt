package com.kumpello.grudzienialia.ui.screens.application.calendarScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.VerticalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

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
            if (YearMonth.now().month != Month.DECEMBER) {
                //Toast? Blocks app?
            }
            val daysOfWeek = daysOfWeek(firstDayOfWeek = DayOfWeek.MONDAY)
            val currentMonth = remember { YearMonth.now().withMonth(12) }
            val startMonth = remember { YearMonth.now().withMonth(12) }
            val endMonth = remember { YearMonth.now().withMonth(12) }
            var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

            val state = rememberCalendarState(
                startMonth = startMonth,
                endMonth = endMonth,
                firstVisibleMonth = currentMonth,
                firstDayOfWeek = daysOfWeek.first()
            )

            VerticalCalendar(
                // Draw the day content gradient.
                monthBody = { _, content ->
                    Box(
                        modifier = Modifier.background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFFB2EBF2),
                                    Color(0xFFB2B8F2)
                                )
                            )
                        )
                    ) {
                        content() // Render the provided content!
                    }
                },
                // Add the corners/borders and month width.
                monthContainer = { _, container ->
                    val configuration = LocalConfiguration.current
                    val screenWidth = configuration.screenWidthDp.dp
                    Box(
                        modifier = Modifier
                            .width(screenWidth * 0.73f)
                            .padding(8.dp)
                            .clip(shape = RoundedCornerShape(8.dp))
                            .border(
                                color = Color.Black,
                                width = 1.dp,
                                shape = RoundedCornerShape(8.dp)
                            )
                    ) {
                        container() // Render the provided container!
                    }
                },
                state = state,
                dayContent = { day ->
                    Day(day, isSelected = selectedDate == day.date) { day ->
                        selectedDate = if (selectedDate == day.date) null else day.date
                    }
                },
                monthHeader = {
                    DaysOfWeekTitle(daysOfWeek = daysOfWeek)
                }
            )
            SelectedDay()
        }
    })
}

@Composable
fun Day(day: CalendarDay, isSelected: Boolean, SelectDay: (CalendarDay) -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(color = if (isSelected) Color.Green else Color.Transparent)
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                onClick = { SelectDay(day) }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            color = if (day.position == DayPosition.MonthDate) Color.White else Color.Gray
        //Todo Different colors to weekends?
        )
    }
}

@Composable
fun DaysOfWeekTitle(daysOfWeek: List<DayOfWeek>) {
    Row(modifier = Modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
            )
        }
    }
}

@Composable
fun SelectedDay() {

}

@Preview(showBackground = true)
@Composable
fun CalendarPreview() {
    Calendar()
}
