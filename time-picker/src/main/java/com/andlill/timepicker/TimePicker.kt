package com.andlill.timepicker

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalTime

@Composable
fun TimePickerDialog(
    state: MutableState<Boolean>,
    initialTime: LocalTime = LocalTime.now(),
    is24h: Boolean = true,
    onSelectTime: (LocalTime) -> Unit
) {
    if (state.value) {

        val config = LocalConfiguration.current
        val timeSelected = remember { mutableStateOf(initialTime) }
        val timeInputMode = remember { mutableStateOf(TimeInputMode.ClockDial) }
        val timePeriod = remember { mutableStateOf(if (initialTime.hour < 12) TimePeriod.AM else TimePeriod.PM) }
        val timeUnit = remember { mutableStateOf(TimeUnit.Hour) }

        @OptIn(ExperimentalComposeUiApi::class)
        Dialog(
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            ),
            onDismissRequest = { state.value = false },
            content = {
                Box(modifier = Modifier
                    .widthIn(400.dp, if (timeInputMode.value == TimeInputMode.ClockDial) 650.dp else 400.dp)
                    .padding(start = 32.dp, end = 32.dp)) {
                    Surface(
                        modifier = Modifier
                            .wrapContentSize()
                            .animateContentSize(),
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
                            TimePickerLayoutVertical(
                                timeSelected = timeSelected,
                                timeInputMode = timeInputMode,
                                timeUnit = timeUnit,
                                timePeriod = timePeriod,
                                is24h = is24h,
                                onNegativeClick = {
                                    state.value = false
                                },
                                onPositiveClick = {
                                    onSelectTime(timeSelected.value)
                                    state.value = false
                                }
                            )
                        }
                        else {
                            TimePickerLayoutHorizontal(
                                timeSelected = timeSelected,
                                timeInputMode = timeInputMode,
                                timeUnit = timeUnit,
                                timePeriod = timePeriod,
                                is24h = is24h,
                                onNegativeClick = {
                                    state.value = false
                                },
                                onPositiveClick = {
                                    onSelectTime(timeSelected.value)
                                    state.value = false
                                }
                            )
                        }
                    }
                }
            }
        )
    }
}

@Composable
internal fun TimePickerLayoutVertical(
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
                color = MaterialTheme.colorScheme.onSurface
            )
            TimePickerMinute(
                modifier = Modifier
                    .width(96.dp)
                    .height(80.dp),
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

@Composable
internal fun TimePickerLayoutHorizontal(
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
    Column(modifier = Modifier.padding(16.dp)) {
        Row {
            Box(modifier = Modifier.width(256.dp).height(if (timeInputMode.value == TimeInputMode.ClockDial) 256.dp else 128.dp), contentAlignment = Alignment.Center) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TimePickerHour(
                            modifier = Modifier
                                .width(96.dp)
                                .height(80.dp),
                            focusRequester = focusRequester,
                            timeSelected = timeSelected.value,
                            is24h = is24h,
                            readOnly = timeInputMode.value == TimeInputMode.ClockDial,
                            selected = timeUnit.value == TimeUnit.Hour,
                            onSelectTime = {
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
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        TimePickerMinute(
                            modifier = Modifier
                                .width(96.dp)
                                .height(80.dp),
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
                    }
                    if (!is24h) {
                        TimePickerPeriodSelector(
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .width(216.dp)
                                .height(32.dp),
                            vertical = false,
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
            }
            if (timeInputMode.value == TimeInputMode.ClockDial) {
                Spacer(modifier = Modifier.width(24.dp))
                TimePickerClock(
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
        }
        Spacer(modifier = Modifier.height(16.dp))
        TimePickerActionRow(
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