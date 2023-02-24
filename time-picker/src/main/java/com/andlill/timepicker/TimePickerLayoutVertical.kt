package com.andlill.timepicker

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalTime

@Composable
internal fun TimePickerLayoutVertical(
    colors: TimePickerColors,
    strings: TimePickerStrings,
    timeSelected: MutableState<LocalTime>,
    timeInputMode: MutableState<TimeInputMode>,
    timeUnit: MutableState<TimeUnit>,
    timePeriod: MutableState<TimePeriod>,
    is24h: Boolean,
    onNegativeClick: () -> Unit,
    onPositiveClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TimePickerHour(
                modifier = Modifier
                    .width(96.dp)
                    .height(80.dp),
                colors = colors,
                focusRequester = focusRequester,
                timeSelected = timeSelected.value,
                is24h = is24h,
                readOnly = timeInputMode.value == TimeInputMode.ClockDial,
                selected = timeUnit.value == TimeUnit.Hour,
                onSelectTime = {
                    if (!is24h && timePeriod.value == TimePeriod.PM)
                        timeSelected.value = it.withHour(it.hour + 12)
                    else
                        timeSelected.value = it
                },
                onSelect = {
                    timeUnit.value = TimeUnit.Hour
                }
            )
            Text(
                modifier = Modifier.width(24.dp),
                textAlign = TextAlign.Center,
                text = ":",
                fontSize = 48.sp,
                color = colors.timeDisplayText
            )
            TimePickerMinute(
                modifier = Modifier
                    .width(96.dp)
                    .height(80.dp),
                colors = colors,
                timeSelected = timeSelected.value,
                readOnly = timeInputMode.value == TimeInputMode.ClockDial,
                selected = timeUnit.value == TimeUnit.Minute,
                onSelectTime = {
                    timeSelected.value = it
                },
                onSelect = {
                    timeUnit.value = TimeUnit.Minute
                }
            )
            if (!is24h) {
                TimePickerPeriodSelector(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .width(52.dp)
                        .height(80.dp),
                    colors = colors,
                    strings = strings,
                    vertical = true,
                    timePeriod = timePeriod.value,
                    onSelect = {
                        if (timePeriod.value == it)
                            return@TimePickerPeriodSelector

                        timePeriod.value = it
                        if (it == TimePeriod.AM) {
                            // From PM to AM
                            val hour = timeSelected.value.hour - 12
                            timeSelected.value = timeSelected.value.withHour(hour)
                        }
                        else {
                            // From AM to PM
                            val hour = timeSelected.value.hour + 12
                            timeSelected.value = timeSelected.value.withHour(hour)
                        }
                    }
                )
            }
        }
        if (timeInputMode.value == TimeInputMode.ClockDial) {
            Spacer(modifier = Modifier.height(16.dp))
            TimePickerClock(
                colors = colors,
                timeSelected = timeSelected.value,
                timeUnit = timeUnit.value,
                timePeriod = timePeriod.value,
                is24h = is24h,
                onSelectTime = {
                    timeSelected.value = it
                },
                onChangeTimeUnit = {
                    timeUnit.value = it
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        TimePickerActionRow(
            colors = colors,
            strings = strings,
            timeInputMode = timeInputMode.value,
            onInputModeClick = {
                if (timeInputMode.value == TimeInputMode.ClockDial) {
                    timeInputMode.value = TimeInputMode.TextField
                    scope.launch {
                        delay(50)
                        focusRequester.requestFocus()
                    }
                }
                else {
                    timeInputMode.value = TimeInputMode.ClockDial
                }
            },
            onNegativeClick = onNegativeClick,
            onPositiveClick = onPositiveClick
        )
    }
}