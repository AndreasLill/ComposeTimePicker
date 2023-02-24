package com.andlill.timepicker

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import java.time.LocalTime

@Composable
fun TimePickerDialog(
    state: MutableState<Boolean>,
    initialTime: LocalTime = LocalTime.now(),
    is24h: Boolean = true,
    colors: TimePickerColors = TimePickerDefaults.colors(),
    strings: TimePickerStrings = TimePickerDefaults.strings(),
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
                    .widthIn(
                        400.dp,
                        if (timeInputMode.value == TimeInputMode.ClockDial) 650.dp else 400.dp
                    )
                    .padding(start = 32.dp, end = 32.dp)) {
                    Surface(
                        modifier = Modifier
                            .wrapContentSize()
                            .animateContentSize(),
                        color = colors.background,
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
                            TimePickerLayoutVertical(
                                colors = colors,
                                strings = strings,
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
                                colors = colors,
                                strings = strings,
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