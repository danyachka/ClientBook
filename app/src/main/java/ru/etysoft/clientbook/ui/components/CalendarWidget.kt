package ru.etysoft.clientbook.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.etysoft.clientbook.R
import ru.etysoft.clientbook.utils.Logger
import java.time.LocalDate
import kotlin.math.abs
import kotlin.math.absoluteValue


interface CalendarWidgetListener {

    fun onClick(selectedLocalDate: LocalDate)

}


@Composable
fun CalendarWidget(
        listener: CalendarWidgetListener,
        modifier: Modifier = Modifier
) {
    Logger.logDebug("CalendarWidget", "CalendarWidget's been created")
    val localNow = LocalDate.now()
    var calendar by remember { mutableStateOf(LocalDate.now()) }
    var animateContentSize by remember { mutableStateOf(0) }

    Column(
            modifier = modifier
                    .fillMaxWidth()
                    .padding(6.dp)
                    .background(colorResource(id = R.color.accent), shape = RoundedCornerShape(16.dp))
                    .padding(10.dp)
                    .animateContentSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
                modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TopButton(false) {
                animateContentSize++
                calendar = calendar.minusMonths(1)
            }

            Text(
                    "${calendar.month.name} ${calendar.year}",
                    fontSize = 18.sp,
                    fontFamily = montserrat,
                    fontWeight = FontWeight.SemiBold
            )

            TopButton(true) {
                animateContentSize++
                calendar = calendar.plusMonths(1)
            }
        }

        ShowHeaderRow()

        val daysInMonth = calendar.lengthOfMonth()
        val firstDayInMonth = calendar.withDayOfMonth(1)
        val firstDayOfWeek = firstDayInMonth.dayOfWeek.value
        val rows = kotlin.math.ceil((firstDayOfWeek + daysInMonth) / 7.0).toInt()

        val previousMonthLen = calendar.minusMonths(1).lengthOfMonth()

        LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)

        ) {
            items((0 until rows * 7).toList()) { index ->
                val day = index - firstDayOfWeek + 1

                if (day < 1) {
                    val d = previousMonthLen + day

                    CreateDayElement(day = d,
                            isCurrent = false,
                            buttonDate = firstDayInMonth.minusDays(abs(day) + 1L),
                            localDate = localNow,
                            listener = listener)
                } else if (day > daysInMonth) {
                    val d = day - daysInMonth

                    CreateDayElement(day = d,
                            isCurrent = false,
                            buttonDate = firstDayInMonth.plusDays(day - 1L),
                            localDate = localNow,
                            listener = listener)
                } else {
                    CreateDayElement(day = day,
                            isCurrent = true,
                            buttonDate = firstDayInMonth.plusDays(day - 1L),
                            localDate = localNow,
                            listener = listener)
                }
            }
        }
    }
}


@Composable
fun CreateDayElement(day: Int, isCurrent: Boolean, buttonDate: LocalDate, localDate: LocalDate,
                     listener: CalendarWidgetListener) {
    Logger.logDebug("CalendarWidget", "Bound date: $buttonDate, $localDate")
    Box (
            contentAlignment = Alignment.Center,
            modifier = Modifier
                    .padding(4.dp)
                    .background(
                            if (buttonDate.isEqual(localDate)) colorResource(id = R.color.accent_light_light_light)
                            else if (isCurrent) colorResource(id = R.color.accent_dark_dark)
                            else colorResource(id = R.color.accent_dark),
                            shape = RoundedCornerShape(8.dp))
                    .border(width = 4.dp,
                            color = if (isCurrent) colorResource(id = R.color.accent_dark_dark)
                                    else colorResource(id = R.color.accent_dark),
                            shape = RoundedCornerShape(8.dp))
                    .clickable { listener.onClick(buttonDate) }
    ) {

        Text(
                day.toString(),
                fontSize = 13.sp,
                fontFamily = montserrat,
                modifier = Modifier.padding(4.dp),
                fontWeight = if (buttonDate.isEqual(localDate)) FontWeight.ExtraBold
                                else FontWeight.SemiBold,
                color = if (buttonDate.isEqual(localDate)) colorResource(id = R.color.accent_dark_dark)
                        else if (isCurrent) colorResource(id = R.color.white_trans)
                        else colorResource(id = R.color.white_trans)

        )
    }
}


@Composable
fun ShowHeaderRow() {
    Row(
            modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
    ) {
        val dayOfWeekStrings = listOf(
                R.string.monday,
                R.string.tuesday,
                R.string.wednesday,
                R.string.thursday,
                R.string.friday,
                R.string.saturday,
                R.string.sunday
        )

        dayOfWeekStrings.forEach { id ->
            Text(
                    stringResource(id),
                    fontSize = 15.sp,
                    fontFamily = montserrat,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f),
                    color = colorResource(id = R.color.accent_dark_dark)
            )
        }
    }
}


@Composable
fun TopButton(toFuture: Boolean, runnable: Runnable) {
    Box(
            modifier = Modifier
                    .width(30.dp)
                    .height(24.dp)
                    .background(color = colorResource(id = R.color.accent_dark_dark),
                            shape = RoundedCornerShape(16.dp))
                    .padding(2.dp)
                    .clickable { runnable.run() },
            contentAlignment = Alignment.Center,
    ) {
        val drawable = ImageVector.vectorResource(R.drawable.ic_next)
        if (toFuture)
            Icon(
                    imageVector = drawable,
                    contentDescription = "",
                    tint = colorResource(id = R.color.white_trans)
            )
        else
            Icon(
                    imageVector = drawable,
                    modifier = Modifier.scale(scaleX = -1f, scaleY = 1f),
                    contentDescription = "",
                    tint = colorResource(id = R.color.white_trans)
            )
    }

}


@Preview
@Composable
fun Preview() {
    CalendarWidget(listener = object : CalendarWidgetListener {
        override fun onClick(selectedLocalDate: LocalDate) {
        }
    })
}
