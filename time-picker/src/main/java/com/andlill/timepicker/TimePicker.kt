package com.andlill.timepicker

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import java.time.LocalTime
import java.util.*

@Composable
fun TimePickerDialog(
    state: MutableState<Boolean>,
    initialTime: LocalTime = LocalTime.now(),
    is24h: Boolean = true,
    onSelectTime: (LocalTime) -> Unit
) {
    if (state.value) {

        val config = LocalConfiguration.current
        val timeSelected = rememberSaveable { mutableStateOf(initialTime) }
        val hourSelected = rememberSaveable { mutableStateOf(true) }
        val clockDial = rememberSaveable { mutableStateOf(true) }
        val timePeriod = rememberSaveable { mutableStateOf(TimePeriod.AM) }

        Dialog(
            onDismissRequest = { state.value = false },
            content = {
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
                            hourSelected = hourSelected,
                            clockDial = clockDial,
                            timePeriod = timePeriod,
                            is24h = is24h,
                            onNegativeClick = {
                                state.value = false
                            },
                            onPositiveClick = {
                                timeSelected.value.let(onSelectTime)
                                state.value = false
                            }
                        )
                    }
                    else {
                        TimePickerLayoutHorizontal(
                            timeSelected = timeSelected,
                            is24h = is24h,
                            onNegativeClick = {
                                state.value = false
                            },
                            onPositiveClick = {
                                timeSelected.value.let(onSelectTime)
                                state.value = false
                            }
                        )
                    }
                }
            }
        )
    }
}

@Composable
internal fun TimePickerLayoutVertical(
    timeSelected: MutableState<LocalTime>,
    hourSelected: MutableState<Boolean>,
    clockDial: MutableState<Boolean>,
    timePeriod: MutableState<TimePeriod>,
    is24h: Boolean,
    onNegativeClick: () -> Unit,
    onPositiveClick: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TimePickerTextField(
                modifier = Modifier
                    .width(96.dp)
                    .height(80.dp),
                value = timeSelected.value.toString("HH", Locale.getDefault()),
                readOnly = clockDial.value,
                selected = hourSelected.value,
                imeAction = ImeAction.Next,
                onValueChange = {

                },
                onSelect = {
                    hourSelected.value = true
                }
            )
            Text(
                modifier = Modifier.width(24.dp),
                textAlign = TextAlign.Center,
                text = ":",
                fontSize = 48.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            TimePickerTextField(
                modifier = Modifier
                    .width(96.dp)
                    .height(80.dp),
                value = timeSelected.value.toString("mm", Locale.getDefault()),
                readOnly = clockDial.value,
                selected = !hourSelected.value,
                imeAction = ImeAction.Done,
                onValueChange = {

                },
                onSelect = {
                    hourSelected.value = false
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
                        timePeriod.value = it
                    }
                )
            }
        }
        if (clockDial.value) {
            Spacer(modifier = Modifier.height(16.dp))
            TimePickerClockDial(

            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        TimePickerActionRow(
            onNegativeClick = onNegativeClick,
            onPositiveClick = onPositiveClick
        )
    }
}

@Composable
internal fun TimePickerLayoutHorizontal(
    timeSelected: MutableState<LocalTime>,
    is24h: Boolean,
    onNegativeClick: () -> Unit,
    onPositiveClick: () -> Unit
) {
    Row(modifier = Modifier.padding(16.dp)) {

    }
}